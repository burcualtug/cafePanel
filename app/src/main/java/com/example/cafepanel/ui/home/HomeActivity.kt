package com.example.cafepanel.ui.home

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.cafepanel.R
import com.example.cafepanel.databinding.ActivityHomeBinding
import com.example.cafepanel.ui.auth.AuthListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.math.BigInteger
import java.util.*

class HomeActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory : HomeViewModelFactory by instance()

    private lateinit var viewModel: HomeViewModel

    var db = Firebase.firestore

    var authListener: AuthListener? = null

    lateinit var binding: ActivityHomeBinding
    private val firebaseAuth2: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val binding: ActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.brandname = firebaseAuth2.currentUser?.email.toString()
        replaceFragment(com.example.cafepanel.ui.category.CategoryFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.home -> replaceFragment(com.example.cafepanel.ui.category.CategoryFragment())
                R.id.orders -> replaceFragment(com.example.cafepanel.ui.category.CategoryFragment())
                R.id.products -> replaceFragment(com.example.cafepanel.ui.category.CategoryFragment())
                R.id.settings -> replaceFragment(com.example.cafepanel.ui.category.CategoryFragment())
                else -> {

                }
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

}