package com.emirhan.socialapp.data.network

import android.os.Build
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import com.emirhan.socialapp.SocialApp.Companion.currentActivityContext
import com.emirhan.socialapp.core.Constants.Companion.USERS_COLLECTION
import com.emirhan.socialapp.core.LoggingInterceptor
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.network.LoginDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * In the same way register w/ credentials and retrieves user information
 */
class LoginDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : LoginDataSource {

    // Login user on Firebase Authentication
    override suspend fun authentication(email: String, password: String): FirebaseUser? =
        auth
            .signInWithEmailAndPassword(email, password)
            .await()
            .user

    // Create user on Firebase Authentication
    override suspend fun createUser(
        username: String,
        email: String,
        password: String
    ): FirebaseUser? =
        auth
            .createUserWithEmailAndPassword(email, password)
            .await()
            .user.apply {
                createUserForFirestore(
                    username = username,
                    uid = this?.uid
                )
            }

    // Utility Firebase Authentication Functions
    override fun currentUser(): FirebaseUser? = auth.currentUser
    override fun signOut() = auth.signOut()

    // Create user on Firestore users collection
    private suspend fun createUserForFirestore(username: String, uid: String?) =
        hashMapOf(
            "uid" to uid,
            "displayName" to username
        ).let {
            uid?.let {
                fireStore
                    .collection(USERS_COLLECTION)
                    .document(uid)
                    .set(it)
                    .await()
            }
        }

    // Get user data with Firestore users collection
    override suspend fun getUser(uid: String?): User? =
        uid?.let {
            fireStore.collection(USERS_COLLECTION)
                .document(it)
                .get()
                .await()
                .toObject(User::class.java)
        }

    // Get users data with Firestore users collection
    override suspend fun getUsers(uid: String?): Flow<MutableList<User>> =
        fireStore.collection(USERS_COLLECTION)
            .whereEqualTo("uid", uid)
            .snapshotFlow().map { it.toObjects(User::class.java) }

    // Additional Firebase Functions
    private fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener { value, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (value != null)
                trySend(value)
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    override suspend fun authenticateWithPasskey(): Credential =
        currentActivityContext?.let { activityContext ->
            CredentialManager.create(activityContext).getCredential(
                context = activityContext,
                GetCredentialRequest(credentialOptions = listOf(GetPasswordOption()))
            ).credential
        } ?: throw IllegalStateException("Current activity is null")


    override suspend fun registerWithPasskey(username: String): FirebaseUser? =
        currentActivityContext?.let { activityContext ->

            withContext(Dispatchers.IO) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val okHttpClient = OkHttpClient.Builder()
                        .addInterceptor(LoggingInterceptor())
                        .build()
                    val startRegisterUrl = HttpUrl.Builder()
                        .scheme("https")
                        .host("3000-firebase-mobilexcelerate-1753614601129.cluster-jbb3mjctu5cbgsi6hwq6u4btwe.cloudworkstations.dev")
                        .addPathSegment("register")
                        .addPathSegment("start")
                        .build()

                    val requestBody = FormBody.Builder()
                        .add("username", UUID.randomUUID().toString())
                        .build()

                    val startRegisterRequest = Request.Builder()
                        .url(startRegisterUrl)
                        .post(requestBody)
                        .build()

                    val startRegisterResponse = okHttpClient.newCall(startRegisterRequest).execute()
                    val requestJson = startRegisterResponse.body?.string()
                        ?: throw Exception("startRegisterResponse -> No response body")

                    // Handle passkey registration for Android P and above
                    val createCredentialRequest = CreatePublicKeyCredentialRequest(
                        requestJson = requestJson,
                        preferImmediatelyAvailableCredentials = false
                    )
                    val createCredentialResponse =
                        CredentialManager.create(currentActivityContext!!).createCredential(
                            currentActivityContext!!,
                            createCredentialRequest
                        )

                    if (createCredentialResponse !is CreatePublicKeyCredentialResponse) {
                        throw Exception("Incorrect response type")
                    }

                    val registerFinishUrl = HttpUrl.Builder()
                        .scheme("https")
                        .host("3000-firebase-mobilexcelerate-1753614601129.cluster-jbb3mjctu5cbgsi6hwq6u4btwe.cloudworkstations.dev")
                        .addPathSegment("register")
                        .addPathSegment("finish")
                        .build()

                    val registerFinishRequest = Request.Builder()
                        .url(registerFinishUrl)
                        .post(createCredentialResponse.registrationResponseJson.toRequestBody("application/json".toMediaType()))
                        .build()

                    val registerFinishResponse = okHttpClient.newCall(registerFinishRequest).execute()
                    val newResponse = registerFinishResponse.body?.string()
                        ?: throw Exception("registerFinishResponse -> No response body")
                    println(newResponse)
                }
            }

            return null
        }


}