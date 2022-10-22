
package com.emirhan.socialapp.data.network

import com.emirhan.socialapp.core.Constants.Companion.USERS_COLLECTION
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.network.LoginDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 * In the same way register w/ credentials and retrieves user information
 */

class LoginDataSourceImpl(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
): LoginDataSource {

    // Login user on Firebase Authentication
    override suspend fun authentication(email: String, password: String): FirebaseUser? =
         auth
            .signInWithEmailAndPassword(email, password)
            .await()
            .user

    // Create user on Firebase Authentication
    override suspend fun createUser(username: String, email: String, password: String): FirebaseUser? =
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
}