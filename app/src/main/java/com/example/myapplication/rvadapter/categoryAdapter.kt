package com.example.myapplication.rvadapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Models.Category_Class
import com.example.myapplication.R

data class categoryAdapter(
    private val context: Context,
    private val categoryList: MutableList<Category_Class> = mutableListOf()
) : RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_recycle_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory =  categoryList[position]
        Glide.with(context).load(currentCategory.imageuri).into(holder.recImage)
        holder.reqName.text = currentCategory.name
        holder.reqLocation.text = currentCategory.location
        holder.reqCategory.text = currentCategory.category
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
    fun setCategoryList(newList: List<Category_Class>) {
        categoryList.clear()
        categoryList.addAll(newList)
        notifyDataSetChanged()
    }


}

