package com.example.cafepanel.ui.category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cafepanel.R
import com.example.cafepanel.firebase.ModelCategory


class AdapterCategory2(
    private val context: CategoryFragment,
    private val Wordlist: ArrayList<ModelCategory>,
    private val listener: CategoryFragment,
) :
    RecyclerView.Adapter<AdapterCategory2.ItemViewHolder>(), Filterable {
    private lateinit var mListener: ItemClickListener

    interface ItemClickListener {
        fun onItemClicked(item: ModelCategory)
        fun onItemClick(position: Int)
    }

    //temporary list for search
    private var WordSearchlist = arrayListOf<ModelCategory>()

    fun setItemClickListener(clickListener: ItemClickListener) {
        mListener = clickListener
    }
    fun submitList(wordList: ArrayList<ModelCategory>) {
        Wordlist.clear()
        Wordlist.addAll(wordList)
        WordSearchlist = wordList
        notifyDataSetChanged()
    }

    fun getItem(position: Int): ModelCategory? {
        return WordSearchlist.get(position)
    }


    inner class ItemViewHolder(itemView: View, clickListener: ItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val categoryText = itemView.findViewById<TextView>(R.id.categoryTv)
        fun bind(wordlist: ModelCategory, context: CategoryFragment) {
            categoryText.text = wordlist.category;
        }

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    init {
        this.WordSearchlist = Wordlist
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_category, parent, false);
        return ItemViewHolder(view, mListener);
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(WordSearchlist[position], context)
    }

    override fun getItemCount(): Int {
        return WordSearchlist.size;
    }

    //function to filter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    WordSearchlist = Wordlist
                } else {
                    val filteredList = ArrayList<ModelCategory>()
                    //The part to search for the desired data
                    for (word in Wordlist) {
                        if (word.category.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(word)
                        }
                    }
                    WordSearchlist = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = WordSearchlist
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                WordSearchlist = filterResults.values as ArrayList<ModelCategory>
                notifyDataSetChanged()
            }

        }
    }


}