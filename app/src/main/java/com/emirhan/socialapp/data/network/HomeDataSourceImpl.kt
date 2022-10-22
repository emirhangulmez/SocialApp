
package com.emirhan.socialapp.data.network

import android.graphics.Bitmap
import android.net.Uri
import com.emirhan.socialapp.core.Constants.Companion.COMMENTS_COLLECTION
import com.emirhan.socialapp.core.Constants.Companion.IMAGES_PATH
import com.emirhan.socialapp.core.Constants.Companion.POSTDATE_FIELD
import com.emirhan.socialapp.core.Constants.Companion.POSTS_COLLECTION
import com.emirhan.socialapp.core.Constants.Companion.STORIES_COLLECTION
import com.emirhan.socialapp.core.Constants.Companion.STORIES_PATH
import com.emirhan.socialapp.domain.model.Comment
import com.emirhan.socialapp.domain.model.Post
import com.emirhan.socialapp.domain.model.Story
import com.emirhan.socialapp.domain.network.HomeDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Class that handles get, create and delete documents w/ document ids and retrieves document information.
 * In the same way snapshot w/ get documents
 */
class HomeDataSourceImpl(
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : HomeDataSource {

    override suspend fun getPosts(): Flow<MutableList<Post>> =
        fireStore.collection(POSTS_COLLECTION).orderBy(POSTDATE_FIELD, Query.Direction.DESCENDING)
            .snapshotFlow()
            .map { querySnapshot -> querySnapshot.toObjects(Post::class.java)  }

    override suspend fun getPost(postId: String): Post? =
        fireStore.collection(POSTS_COLLECTION)
            .document(postId)
            .get()
            .await()
            .toObject(Post::class.java)

    override suspend fun createPost(post: Post): Post? =
        fireStore.collection(POSTS_COLLECTION)
            .add(post)
            .await()
            .get()
            .await()
            .toObject(Post::class.java)

    override suspend fun getStories(): Flow<MutableList<Story>> =
        fireStore.collection(STORIES_COLLECTION)
            .snapshotFlow()
            .map { querySnapshot -> querySnapshot.toObjects(Story::class.java) }

    override suspend fun getStory(storyId: String): Story? =
        fireStore.collection(STORIES_COLLECTION)
            .document(storyId)
            .get()
            .await()
            .toObject(Story::class.java)

    override suspend fun createStory(story: Story): Story?  {
        // Get storage link and initialize story picture url variable.
        if (story.pictureBitmap != null) {
            story.pictureURL = getImageLink(story.pictureBitmap).toString()
        }
        // Create unique id for the story collection.
        story.storyID =
            fireStore.collection(STORIES_COLLECTION).document().id

        hashMapOf(
            "storyID" to story.storyID,
            "pictureURL" to story.pictureURL,
            "userUID" to story.userUID,
        ).let {
            return fireStore.collection(STORIES_COLLECTION)
                .add(it)
                .await()
                .get()
                .await()
                .toObject(Story::class.java)
        }
    }

    override suspend fun getComments(): MutableList<Comment> =
        fireStore.collection(COMMENTS_COLLECTION)
            .get()
            .await()
            .toObjects(Comment::class.java)

    override suspend fun createComment(comment: Comment): Comment? =
        fireStore.collection(COMMENTS_COLLECTION)
            .add(comment)
            .await()
            .get()
            .await()
            .toObject(Comment::class.java)

    /*
    * Addition Firebase Functions
    */
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

    private suspend fun getImageLink(pictureBitmap: Bitmap) : Uri =
        // Save picture on storage
            toByteArray(pictureBitmap).let { byteArray ->
                storage.reference.child(IMAGES_PATH)
                    .child(STORIES_PATH)
                    .child(UUID.randomUUID().toString())
                    .putBytes(byteArray)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }

    private fun toByteArray(pictureBitmap: Bitmap) : ByteArray =
        ByteArrayOutputStream().let {
            pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            return it.toByteArray()
        }
}