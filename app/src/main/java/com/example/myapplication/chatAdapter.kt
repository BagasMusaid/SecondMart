package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.chat_class

class chatAdapter(private val context: android.content.Context,private val dataList: ArrayList<chat_class>): RecyclerView.Adapter<MyView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.chat_recycle_item, parent, false)
        return MyView(view)
    }
    override fun onBindViewHolder(holder: MyView, position: Int) {
        val currentItem = dataList[position]
        holder.reqFoto.setImageResource(currentItem.profile)
        holder.reqName.text = currentItem.name
        holder.reqTopik.text = currentItem.topik
        holder.reqNotif.text = currentItem.notif
    }
    override fun getItemCount(): Int {
        return dataList.size
    }



}

class MyView(itemView: View):RecyclerView.ViewHolder(itemView) {
    var reqFoto : ImageView
    var reqName : TextView
    var reqTopik : TextView
    var reqNotif : TextView

    init {
        reqFoto = itemView.findViewById(R.id.foto_profil_chat)
        reqName = itemView.findViewById(R.id.name_chat)
        reqTopik = itemView.findViewById(R.id.topic_chat)
        reqNotif = itemView.findViewById(R.id.notif_chat)
    }
}



