package com.example.cafepanel.ui.category

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.cafepanel.R
import com.example.cafepanel.databinding.ActivityAddCategoryBinding
import com.example.cafepanel.databinding.ActivityLoginBinding
import com.example.cafepanel.databinding.FragmentCategoryBinding
import com.example.cafepanel.firebase.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.reactivex.internal.operators.observable.ObservableError
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddCategoryActivity : AppCompatActivity() , KodeinAware {
    override val kodein: Kodein by kodein()
    private val factory : CategoryViewModelFactory by instance()

    private lateinit var viewModel: CategoryViewModel
    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var progressDialog: ProgressDialog

    //arraylist to hold categories
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    //adapter
    private lateinit var adapterCategory: AdapterCategory
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    var categoryList = arrayListOf<ModelCategory>()

    val customAdapterAddedWords = AdapterCategory(this, categoryList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_category)

        viewModel = ViewModelProviders.of(this, factory)[CategoryViewModel::class.java]
        binding.viewmodel = viewModel

        progressDialog = ProgressDialog( this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside (false)

        binding.backBtn.setOnClickListener{
            Intent(this, CategoryFragment::class.java).also {
                startActivity(it)
            }
        }
        viewModel.categoryDataAdded.observe(this, Observer { it ->
            categoryList.addAll(it)
            customAdapterAddedWords.notifyDataSetChanged()
        })
        //loadCategories()
    }
/*
    var Addedlist = arrayListOf<ModelCategory>()
    private suspend fun loadCategories(){
        categoryArrayList = ArrayList()
        val mail = firebaseAuth.currentUser?.email.toString()
        suspendCoroutine<List<ModelCategory>> {
            var db = Firebase.firestore
            db.collection("cafes").document(mail)
                .collection("menu").get().addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        val word = ModelCategory(
                            document.data["category"].toString()
                        )
                        Addedlist.add(word)
                    }
                    it.resume(Addedlist)
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents.", exception)
                    it.resume(Addedlist)
                }
        }
    } */
}