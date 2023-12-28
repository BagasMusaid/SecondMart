package com.example.myapplication


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(private val context: android.content.Context, private val dataList: List<Posting_Class>):RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.card_recycle_item, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(dataList[position].imageuri).into(holder.recImage)
        holder.reqName.text = dataList[position].name
        holder.reqLocation.text = dataList[position].location
        holder.reqCategory.text = dataList[position].category

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


    init {
        recImage = itemView.findViewById(R.id.IV_Post)
        reqName = itemView.findViewById(R.id.TV_name)
        reqCategory = itemView.findViewById(R.id.TV_Category)
        reqLocation = itemView.findViewById(R.id.TV_Location)
    }
}
