package com.example.cafepanel

import android.app.Application
import com.example.cafepanel.firebase.FirebaseSource
import com.example.cafepanel.repositories.UserRepository
import com.example.cafepanel.ui.auth.AuthViewModelFactory
import com.example.cafepanel.ui.category.CategoryViewModelFactory
import com.example.cafepanel.ui.home.HomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class FirebaseApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@FirebaseApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton { UserRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from provider { CategoryViewModelFactory(instance()) }
    }
}