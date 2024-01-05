package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class profileActivity : AppCompatActivity() {

    lateinit var textFullName: TextView
    lateinit var textEmail: TextView
    lateinit var btnLogout: ImageView
    lateinit var imageProfile: CircleImageView

    val firebaseAuth = FirebaseAuth.getInstance()
    private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

    private val GALLERY_REQUEST_CODE = 123
    private val CAMERA_REQUEST_CODE = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        textFullName = findViewById(R.id.full_name)
        textEmail = findViewById(R.id.email)
        btnLogout = findViewById(R.id.btn_logout)
        imageProfile = findViewById(R.id.profile_image)

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            textFullName.text = firebaseUser.displayName
            textEmail.text = firebaseUser.email

            val userReference =
                FirebaseDatabase.getInstance().reference.child("users").child(firebaseUser.uid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
                    if (!profileImageUrl.isNullOrEmpty()) {
                        Glide.with(this@profileActivity)
                            .load(profileImageUrl)
                            .into(imageProfile)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@profileActivity,
                        "Failed to retrieve profile image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(this, "Anda Belum Login", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        val imageBack: ImageView = findViewById(R.id.image_back)
        imageBack.setOnClickListener {
            // Implementasi untuk kembali ke Home
            startActivity(Intent(this, Home::class.java))
            finish()
        }

        val gantiProfilTextView: TextView = findViewById(R.id.gantiprofil)
        gantiProfilTextView.setOnClickListener {
            val options = arrayOf<CharSequence>("Galeri", "Kamera")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Sumber Gambar")
            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                    }
                    1 -> {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                    }
                }
            }
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val selectedImage = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        this.contentResolver,
                        selectedImage
                    )
                    uploadImageToFirebase(bitmap)
                }
                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    uploadImageToFirebase(imageBitmap)
                }
            }
        }
    }

    private fun uploadImageToFirebase(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageName = "profile_picture.jpg"
        val imageRef = storageReference.child("images/$imageName")

        imageRef.putBytes(data)
            .addOnSuccessListener { taskSnapshot ->
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveImageUrlToDatabase(uri.toString())
                    saveImageUrlToSharedPreferences(uri.toString()) // Simpan URL ke SharedPreferences
                    imageProfile.setImageBitmap(bitmap)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUrlToSharedPreferences(imageUrl: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("profileImageUrl", imageUrl)
        editor.apply()
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val userReference = databaseReference.child(firebaseUser.uid)
            userReference.child("profileImageUrl").setValue(imageUrl)
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Apakah Anda yakin ingin Logout?")
        builder.setPositiveButton("Ya") { dialog, which ->
            // User memilih Ya, lakukan proses logout
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        builder.setNegativeButton("Tidak") { dialog, which ->
            // User memilih Tidak, tutup dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
