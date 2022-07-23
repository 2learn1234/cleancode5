package com.example.androidcleanarchitecture.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcleanarchitecture.R
import com.example.androidcleanarchitecture.model.School
import com.squareup.picasso.Picasso
import java.util.ArrayList
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

class SchoolsListAdapter(private val schoolList: ArrayList<School>) :
    RecyclerView.Adapter<SchoolsListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(school: School) {
            val imgNews = itemView.findViewById(R.id.imgNewsPhoto) as ImageView
            val txtHeader = itemView.findViewById(R.id.txtNewsHeader) as TextView
            val txtSubHeader = itemView.findViewById(R.id.txtSubHeader) as TextView

            txtHeader.text = school.school_name
            txtSubHeader.text = school.getOverview_paragraph()
            Picasso.get().load(school.getWebsite()).into(imgNews)
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<School>() {
        override fun areItemsTheSame(oldItem: School, newItem: School): Boolean {
            return (oldItem.dbn == newItem.dbn)
        }

        override fun areContentsTheSame(oldItem: School, newItem: School): Boolean {
            return oldItem == newItem
        }
    }
    val diff = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_school, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val school = diff.currentList[position]
        holder.bindItems(school)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(school)
        }
    }


    override fun getItemCount(): Int {
        return diff.currentList.size
    }


     var onItemClick:((School) -> Unit)? = null
}