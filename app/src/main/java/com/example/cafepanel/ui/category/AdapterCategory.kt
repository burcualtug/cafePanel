package com.example.cafepanel.ui.category

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.cafepanel.databinding.RowCategoryBinding
import com.example.cafepanel.firebase.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterCategory: RecyclerView.Adapter<AdapterCategory.HolderCategory>, Filterable {
    private val context: Context
    private var categoryArrayList: ArrayList<ModelCategory>
    private var filterList: ArrayList<ModelCategory>

    private var filter: FilterCategory?=null

    private lateinit var binding: RowCategoryBinding
    constructor (context: Context, categoryArrayList: ArrayList<ModelCategory>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList=categoryArrayList
         }

    inner class HolderCategory (itemView: View): RecyclerView.ViewHolder(itemView) {
        var categoryTv: TextView = binding.categoryTv
        var deleteBtn: ImageButton = binding.deleteBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderCategory(binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryArrayList[position]
        val category = model.category
        holder.categoryTv.text=category

        holder.deleteBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Delete this category?")
                .setPositiveButton("Confirm"){a, d->
                    Toast.makeText(context,"Deleting...", Toast.LENGTH_SHORT).show()
                    deleteCategory(model,holder)
                }
                .setNegativeButton("Cancel"){a, d->
                    a.dismiss()
                }
        }
    }
    override fun getItemCount(): Int {
        return categoryArrayList.size
    }
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun deleteCategory(model: ModelCategory, holder: HolderCategory){
        var db = Firebase.firestore
        val category=model.category
        val mail = firebaseAuth.currentUser?.email.toString()

        db.collection("cafes").document(mail)
            .collection("menu")
            .whereEqualTo("category",category)
            .get()
            .addOnSuccessListener {
                for(document in it){
                    db.collection("cafes").document(mail).collection("menu")
                        .document(document.id).delete().addOnSuccessListener {
                            Toast.makeText(context,"Deleted...",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{ e ->
                            Toast.makeText(context,"Unable to delete due to ${e.message}",Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

    override fun getFilter(): Filter {
        if(filter==null){
            filter=FilterCategory(filterList,this)
        }
        return filter as FilterCategory
    }
}