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

class SchoolsListAdapter(private val schoolList: ArrayList<School>, val onItemClick: (School) -> Unit) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_school, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(schoolList[position])
        holder.itemView.setOnClickListener {
            val articles : School = schoolList[position]
            onItemClick(articles)
        }
    }


    override fun getItemCount(): Int {
        return schoolList.size
    }
}