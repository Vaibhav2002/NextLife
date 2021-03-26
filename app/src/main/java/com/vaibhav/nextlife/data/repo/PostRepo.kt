package com.vaibhav.nextlife.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.vaibhav.nextlife.data.models.PostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PostRepo @Inject constructor(
    private val authRepo: AuthRepo,
    private val fireStore: FirebaseFirestore
) {

    private val userId = authRepo.getCurrentUserId()


    suspend fun getMyPosts(
        successListener: (List<PostModel>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            fireStore.collection("posts").whereEqualTo("userId", userId).get()
                .addOnSuccessListener {
                    val list = mutableListOf<PostModel>()
                    for (document in it.documents) {
                        val post = document.toObject(PostModel::class.java)
                        post?.let {
                            if (post.userId != userId)
                                list.add(post)
                        }
                    }
                    successListener(list)
                }
                .addOnFailureListener {
                    failureListener(it)
                }
        }
    }

    suspend fun getAllPosts(
        bloodType: String,
        state: String,
        successListener: (List<PostModel>) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            if (bloodType != "") {
                Timber.d("with blood")
                fireStore.collection("posts")
                    .whereEqualTo("state", state)
                    .get()
                    .addOnSuccessListener {
                        val list = mutableListOf<PostModel>()
                        for (document in it.documents) {
                            val post = document.toObject(PostModel::class.java)
                            post?.let {
                                if (post.userId != userId)
                                    list.add(post)
                            }
                        }
                        list.sortByDescending { lis -> lis.timeStamp }
                        val newList = list.filter { postModel ->
                            postModel.bloodType == bloodType
                        }
                        successListener(newList)
                    }
                    .addOnFailureListener {
                        failureListener(it)
                    }
            } else {
                Timber.d("no blood")
                fireStore.collection("posts").whereEqualTo("state", state)
                    .get()
                    .addOnSuccessListener {
                        val list = mutableListOf<PostModel>()
                        for (document in it.documents) {
                            val post = document.toObject(PostModel::class.java)
                            post?.let {
                                if (post.userId != userId)
                                    list.add(post)
                            }
                        }
                        list.sortByDescending { lis -> lis.timeStamp }
                        successListener(list)
                    }
                    .addOnFailureListener {
                        failureListener(it)
                    }
            }
        }
    }

    suspend fun postRequest(
        username: String,
        title: String,
        description: String,
        phoneNumber: String,
        bloodType: String,
        lat: Double,
        long: Double,
        address: String,
        locality: String,
        city: String,
        state: String,
        country: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val postModel = PostModel(
                id = "${System.currentTimeMillis()}${userId}",
                userId = userId,
                username,
                title,
                description,
                phoneNumber,
                bloodType,
                System.currentTimeMillis().toString(),
                lat,
                long,
                address,
                locality,
                city,
                state,
                country
            )
            fireStore.collection("posts").document(postModel.id).set(postModel)
                .addOnSuccessListener {
                    successListener()
                }
                .addOnFailureListener {
                    failureListener(it)
                }
        }
    }


}