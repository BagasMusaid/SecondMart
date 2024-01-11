package com.example.myapplication.rvadapter


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
import com.example.myapplication.Models.Posting_Class
import com.example.myapplication.Models.stored_class
import com.example.myapplication.R

class MyAdapter(
    private val context: android.content.Context,
    private val dataList: List<stored_class>,
    private val productClickInterface: produk,
    private val likeClickInterface: LikedOnClickInterface,
    ):RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.card_recycle_item, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        Glide.with(context).load(currentItem.imageuri).into(holder.recImage)
        holder.reqName.text = currentItem.name
        holder.reqLocation.text = currentItem.location
        holder.reqCategory.text = currentItem.category

        holder.itemView.setOnClickListener {
            productClickInterface.onClickProduct(dataList[position])
        }
        holder.reqbookmart.setOnClickListener {
            val slate = ContextCompat.getColor(context, R.color.bookmart)
            val orange = ContextCompat.getColor(context, R.color.orange)
            if(holder.reqbookmart.isChecked){
                holder.reqbookmart.backgroundTintList = ColorStateList.valueOf(orange)
                likeClickInterface.onClickLike(currentItem)
            }
            else{
                holder.reqbookmart.backgroundTintList = ColorStateList.valueOf(slate)
            }

        }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }

}



class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    var recImage : ImageView
    var reqName : TextView
    var reqCategory : TextView
    var reqLocation : TextView
    var reqbookmart : ToggleButton


    init {
        recImage = itemView.findViewById(R.id.IV_Post)
        reqName = itemView.findViewById(R.id.TV_name)
        reqCategory = itemView.findViewById(R.id.TV_Category)
        reqLocation = itemView.findViewById(R.id.TV_Location)
        reqbookmart = itemView.findViewById(R.id.icon_stored)
    }
}
interface produk {
    fun onClickProduct(item: stored_class)
}

interface LikedOnClickInterface{
    fun onClickLike(item : stored_class)
}