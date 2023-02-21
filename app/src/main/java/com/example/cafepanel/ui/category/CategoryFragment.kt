package com.example.cafepanel.ui.category

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafepanel.R
import com.example.cafepanel.databinding.FragmentCategoryBinding
import com.example.cafepanel.firebase.ModelCategory
import com.example.cafepanel.ui.auth.AuthListener
import com.example.cafepanel.ui.auth.LoginActivity
import com.example.cafepanel.ui.auth.SignupActivity
import com.example.cafepanel.ui.home.HomeActivity
import com.example.cafepanel.utils.startHomeActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_category.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

var _bindingCategory: FragmentCategoryBinding? = null

val binding get() = _bindingCategory!!
class CategoryFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by kodein()
    private val factory: CategoryViewModelFactory by instance()

    private lateinit var viewModel: CategoryViewModel
    private lateinit var binding: FragmentCategoryBinding

    var categoryList = arrayListOf<ModelCategory>()

    val adapterCategory2 = AdapterCategory2(this, categoryList,this)
    private val firebaseAuth2: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    fun addCategoryBtn() {
        Intent(activity, AddCategoryActivity::class.java).also {
            startActivity(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BURCU", "onCreate: ")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        viewModel = ViewModelProviders.of(this, factory)[CategoryViewModel::class.java]
        if (binding != null) {
            binding.viewmodel = viewModel
        }
        viewModel.brandname = firebaseAuth2.currentUser?.email.toString()
        //viewModel.authListener = this

        binding.addCategoryBtn.setOnClickListener{
                addCategoryBtn()
            }

        binding.categoriesRv.removeAllViewsInLayout()
        val layout = LinearLayoutManager(activity)
        binding.categoriesRv.layoutManager = layout
        binding.categoriesRv.adapter = adapterCategory2
        binding.categoriesRv.setHasFixedSize(true)
        categoryList.clear()
        viewModel.categoryDataAdded.observe(viewLifecycleOwner) { it ->
            categoryList.addAll(it)
            adapterCategory2.notifyDataSetChanged()
        }

        adapterCategory2.setItemClickListener(object : AdapterCategory2.ItemClickListener {
            override fun onItemClick(position: Int) {
            }

            override fun onItemClicked(item: ModelCategory) {
                TODO("Not yet implemented")
            }
        })

        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(charSequence: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterCategory2.filter.filter(s)
            }
        })
        return binding.root
    }

}