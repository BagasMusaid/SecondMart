package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.Category_Class
import com.example.myapplication.Models.Posting_Class
import com.example.myapplication.rvadapter.MyAdapter
import com.example.myapplication.rvadapter.categoryAdapter
import com.example.myapplication.rvadapter.produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryActivity : AppCompatActivity(){
    private lateinit var categoryAdapter: categoryAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val contentValue = intent.getStringExtra("content_value")

// Set teks pada TextView berdasarkan nilai content_value
        val textView : TextView = findViewById(R.id.tv_cate)
        textView.text = contentValue


        recyclerView = findViewById(R.id.RV_Cate)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        categoryAdapter = categoryAdapter(this, mutableListOf())
        recyclerView.adapter = categoryAdapter
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Barang")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val barangList = mutableListOf<Category_Class>()

                for (barangSnapshot in snapshot.children) {
                    val barangCategory =
                        barangSnapshot.child("category").getValue(String::class.java)

                    if (barangCategory == contentValue) {
                        val barangName = barangSnapshot.getValue(Category_Class::class.java)

                        if (barangName != null) {
                            barangList.add(barangName)
                        }

                    }
                }

                categoryAdapter.setCategoryList(barangList)
                categoryAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

}


