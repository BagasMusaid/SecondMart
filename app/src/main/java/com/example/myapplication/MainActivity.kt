package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputBinding
import android.widget.ListView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
//    val db = Firebase.firestore
//    val user = hashMapOf(
//        "first" to "Ada",
//        "last" to "Lovelace",
//        "born" to 1815
//    );


    override fun onCreate(savedInstanceState: Bundle?) {

        //Splashscreen
        Thread.sleep(1000)
        installSplashScreen()
//        val usersCollection = db.collection("users")
//
//// Menambahkan data ke koleksi "users"
//        usersCollection
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                println("DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                println("Error adding document: $e")
//            }
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            replaceFragment(Home())
            binding.btmNavigasi?.setOnItemSelectedListener {menuItem->
                when(menuItem.itemId){
                    R.id.Home -> replaceFragment(Home())
                    R.id.chat -> replaceFragment(Search())
                    R.id.Post -> replaceFragment(post())
                    else -> {

                    }
                }
                true
            }
        }else {

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentLayout,fragment)
        fragmentTransaction.commit()
    }
}