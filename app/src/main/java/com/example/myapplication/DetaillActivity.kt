package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.myapplication.Models.Display_Class
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetaillActivity : AppCompatActivity() {
    private lateinit var orderImageUrl:String
    private lateinit var orderName:String
    private lateinit var orderLocation:String
    private lateinit var orderCondition: String
    private lateinit var orderDeskripsion:String
    private lateinit var productDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detaill)
        // Mengambil ID produk dari intent
        val reqImage : ImageView = findViewById(R.id.image_det)
        val reqName : TextView = findViewById(R.id.names)
        val reqDesc : TextView = findViewById(R.id.descrip)
        val reqCon : TextView = findViewById(R.id.kondisi)
        val reqLoc : TextView = findViewById(R.id.lokasi)
        val productId = intent.getStringExtra("productId")
        reqName.text = productId
        productDatabaseReference = FirebaseDatabase.getInstance().getReference("Barang")
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(Display_Class::class.java)

                        if (products!!.name == productId) {
                            Glide
                                .with(this@DetaillActivity)
                                .load(products.imageuri)
                                .into(reqImage)

                            orderImageUrl = products.imageuri!!
                            orderName = products.name!!
                            orderCondition = products.condition!!
                            orderLocation = products.location!!
                            orderDeskripsion = products.description!!

                            reqName.text = products.name
                            reqCon.text = products.condition
                            reqLoc.text = products.location
                            reqDesc.text = products.description
                        }


                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        productDatabaseReference.addValueEventListener(valueEvent)
    }


}