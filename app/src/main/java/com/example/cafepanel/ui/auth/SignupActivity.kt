package com.example.cafepanel.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.cafepanel.R
import com.example.cafepanel.databinding.ActivitySignupBinding
import com.example.cafepanel.ui.home.HomeActivity
import com.example.cafepanel.utils.startHomeActivity
import com.google.protobuf.LazyStringArrayList
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.IOException
import java.io.InputStream


class SignupActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory : AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel

    var ilList = ArrayList<String>()
    var ilceList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this

        ilceList.add("KADIKÖY")
        val states = readJson()
        val states2 = ilceList
        // access the spinner
        val ilSpinner = findViewById<Spinner>(R.id.ilspinner)
        val ilceSpinner = findViewById<Spinner>(R.id.ilcespinner)
        if (ilSpinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, states)
            ilSpinner.adapter = adapter

            ilSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    //readJson(states[position])
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }

            }
        }
        if (ilceSpinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, states2)
            ilceSpinner.adapter = adapter

            ilceSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    viewModel.il=states[position]
                    viewModel.ilce=ilceList[0]
                    //readJson(states[position])
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        /*if (ilceSpinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            ilceSpinner.adapter = adapter

            ilceSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(this@SignupActivity,
                        getString(R.string.selected_item) + " " +
                                "" + languages[position], Toast.LENGTH_SHORT).show()
                    readJson(languages[position])
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }

            }
        } */
    }

    fun readJson(): ArrayList<String> {
        var json : String?=null
        try{
            val inputStream: InputStream? =assets?.open("il-ilce.json")
            json=inputStream?.bufferedReader()?.readText()
            var jsonarr= JSONArray(json)
            for(i in 0 until jsonarr.length()){
                var jsonobj=jsonarr.getJSONObject(i)
                ilList.add(jsonobj.getString("il_adi"))
            }
        }
        catch (e: IOException){
        }
        return ilList
    }

    fun readJson(il:String): ArrayList<String> {
        var json : String?=null
        try{
            val inputStream: InputStream? =assets?.open("il-ilce.json")
            json=inputStream?.bufferedReader()?.readText()
            var jsonarr= JSONArray(json)
            for(i in 0 until jsonarr.length()){
                var jsonobj=jsonarr.getJSONObject(i)
                if(il==jsonobj.getString("il_adi")){
                    //Log.d("TAG",jsonobj.getString("ilce_adi"))
                }
               // ilceList.add(jsonobj.get(il))
                /*if(il == jsonarr.getJSONObject(i).getString("il_adi"))
                {

                    ilceList.add(jsonobj.getString("ilce_adi"))
                } */

            }
        }catch (e: IOException){
        }
        return ilceList
    }

    override fun onStarted() {
        progressbar.visibility = View.VISIBLE
        Intent(this, HomeActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun onSuccess(message: String) {
        progressbar.visibility = View.GONE
        startHomeActivity()
    }

    override fun onFailure(message: String) {
        progressbar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}