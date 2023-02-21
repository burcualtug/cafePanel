package com.example.cafepanel.ui.category

import android.content.Intent
import android.graphics.ColorSpace.Model
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cafepanel.firebase.ModelCategory
import com.example.cafepanel.repositories.UserRepository
import com.example.cafepanel.ui.auth.AuthListener
import com.example.cafepanel.utils.startAddCategoryActivity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: UserRepository
) : ViewModel() {
    var brandname: String? = null
    var category: String? = null

    var authListener: AuthListener? = null

    private val _categoryDataAdded  = MutableLiveData<List<ModelCategory>>()
    val categoryDataAdded : LiveData<List<ModelCategory>>
        get() = _categoryDataAdded

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun addCategoryFirebase(){
        if (category.isNullOrEmpty()) {
            authListener?.onFailure("Enter category name")
            return
        }
        else{
            repository.addCategoryFirebase(firebaseAuth.currentUser?.email.toString(),category!!)
        }
    }
    init {
        getDataAdded(firebaseAuth.currentUser?.email.toString())
    }
    fun getDataAdded(mail:String) {
        viewModelScope.launch {
            _categoryDataAdded.value = repository.getCategoryFirebase(mail)
        }

    }
}