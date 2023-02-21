package com.example.cafepanel.firebase

import android.graphics.ColorSpace.Model
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FirebaseSource {

    var db = Firebase.firestore

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    var categoryList = arrayListOf<ModelCategory>()

    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }


    fun register(email: String, password: String, brandname: String, il: String, ilce: String)
    = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else
                    emitter.onError(it.exception!!)
            }
        }
        var db = Firebase.firestore
        val user = hashMapOf(
            "email" to email,
            "password" to password,
            "brandname" to brandname,
            "il" to il,
            "ilce" to ilce
        )
        db.collection("cafes").document(email)
            .collection("cafeinfos").add(user)

        /*UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
            .setDisplayName (brandname)
            .build();
        FirebaseUser firebaseUser = authResult.getUser();
        firebaseUser.update Profile (userProfileChangeRequest); */

    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun addCategoryFirebase(mail:String,category: String){

        Log.d("TAG1",mail)
        Log.d("TAG2",category)
        var db = Firebase.firestore
        val categoryObj = hashMapOf(
            "category" to category
        )
        db.collection("cafes").document(mail)
            .collection("menu").add(categoryObj)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    suspend fun getCategoryFirebase(mail:String) =
        suspendCoroutine<List<ModelCategory>> {
            db.collection("cafes").document(mail).collection("menu")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        val word = ModelCategory(
                            document.data["category"].toString()
                        )
                        categoryList.add(word)
                    }
                    it.resume(categoryList)
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                    it.resume(categoryList)
                }
        }
}