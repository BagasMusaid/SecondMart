package com.example.myapplication.rvadapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.Models.stored_class

class storedAdapter(
    private val context: Context,
    private val dataList: List<stored_class>,
    private val productClickInterface: ProductOnClickInterface,
    private val likeClickInterface: Like,
    ): RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_recycle_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val orange = ContextCompat.getColor(context, R.color.orange)
        val currentItem = dataList[position]
        Glide.with(context).load(currentItem.imageuri).into(holder.recImage)
        holder.reqName.text = currentItem.name
        holder.reqLocation.text = currentItem.location
        holder.reqCategory.text = currentItem.category
        holder.reqbookmart.backgroundTintList = ColorStateList.valueOf(orange)



        holder.itemView.setOnClickListener {
            productClickInterface.onClickProduct(dataList[position])
        }

        holder.reqbookmart.setOnClickListener {
            likeClickInterface.onclike(currentItem)
            holder.reqbookmart.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

            likeClickInterface.onclike(currentItem)
        }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }


}

interface ProductOnClickInterface {
    fun onClickProduct(item: stored_class)
}

interface Like{
    fun onclike(item : stored_class)
}
