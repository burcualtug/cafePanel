package com.example.cafepanel.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.cafepanel.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

    //email and password for the input
    var email: String? = null
    var password: String? = null
    var brandname: String? = null
    var il: String? = null
    var ilce: String? = null

    //auth listener
    var authListener: AuthListener? = null
    //val connectionManager : ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        repository.currentUser()
    }


    //function to perform login
    fun login() {

        //validating email and password
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        //authentication started
        authListener?.onStarted()

        //calling login from repository to perform the actual authentication
        val disposable = repository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                authListener?.onSuccess("Success")
            }, {
                //sending a failure callback
                authListener?.onFailure(it.message!!)
            })

    }

    //Doing same thing with signup
    fun signup() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Please input all values")
            return
        }
        authListener?.onStarted()
        val disposable = repository.register(email!!, password!!,brandname!!,il!!,ilce!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess("Success")
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }
    //goTo functions are calling by xml
    fun goToSignup(view: View) {
        Intent(view.context, SignupActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun goToLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    //disposing the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}