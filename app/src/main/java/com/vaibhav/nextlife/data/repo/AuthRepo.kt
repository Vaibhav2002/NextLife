package com.vaibhav.nextlife.data.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vaibhav.nextlife.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {


    fun isLoggedIn() = auth.currentUser != null

    fun logOut() {
        auth.signOut()
    }

    fun getCurrentUserId() = auth.currentUser!!.uid


    suspend fun loginUser(
        email: String,
        password: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO)
        {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    successListener.invoke()
                }
                .addOnFailureListener { exception ->
                    failureListener.invoke(exception)
                }
        }
    }

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        image: Uri,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO)
        {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { auth ->
                    Timber.d("Registered")
                    addUserImageToStorage(
                        name,
                        email,
                        password,
                        image,
                        successListener, failureListener
                    )
                }
        }
    }

    suspend fun getUserDetails(
        userId: String,
        successListener: (User) -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            fireStore.collection("users").document(userId).get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    user?.let { successListener(user) }
                        ?: failureListener(Exception("User not found"))
                }
                .addOnFailureListener { failureListener.invoke(it) }
        }
    }


    private fun addUserToFireStore(
        user: User,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        Timber.d(user.toString())
        fireStore.collection("users").document(user.uid).set(user)
            .addOnSuccessListener {
                Timber.d("user stored")
                successListener.invoke()
            }
            .addOnFailureListener { failureListener.invoke(it) }
    }

    private fun addUserImageToStorage(
        name: String,
        email: String,
        password: String,
        image: Uri,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        val filename = getCurrentUserId()
        storage.reference.child(filename).putFile(image)
            .addOnSuccessListener {
                Timber.d("image uploaded")
                getUserImageUrl(
                    name,
                    email,
                    password,
                    filename, successListener, failureListener
                )
            }
            .addOnFailureListener {
                failureListener.invoke(it)
            }

    }

    private fun getUserImageUrl(
        name: String,
        email: String,
        password: String,
        filename: String,
        successListener: () -> Unit,
        failureListener: (Exception) -> Unit
    ) {
        storage.reference.child(filename).downloadUrl
            .addOnSuccessListener { url ->
                Timber.d("download url fetched")
                val user = User(getCurrentUserId(), name, email, url.toString())
                addUserToFireStore(user, successListener, failureListener)
            }
            .addOnFailureListener {
                failureListener.invoke(it)
            }
    }

}