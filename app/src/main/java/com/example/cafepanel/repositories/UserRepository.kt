package com.example.cafepanel.repositories

import com.example.cafepanel.firebase.FirebaseSource
import com.example.cafepanel.firebase.ModelCategory

class UserRepository (
    private val firebase: FirebaseSource
){
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String, brandname: String, il: String, ilce: String)
    = firebase.register(email, password,brandname,il,ilce)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun addCategoryFirebase(mail:String,category:String) = firebase.addCategoryFirebase(mail,category)

    suspend fun getCategoryFirebase(mail: String) : List<ModelCategory> = firebase.getCategoryFirebase(mail)


}