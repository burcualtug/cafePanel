package com.example.cafepanel.ui.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cafepanel.repositories.UserRepository
import com.example.cafepanel.utils.startLoginActivity

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {
    var brandname: String? = null

    var status = MutableLiveData<Boolean?>()
    val user by lazy {
        repository.currentUser()
    }

    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }

    fun viewToast(){
//In your network successfull response
        status.value = true
    }

}