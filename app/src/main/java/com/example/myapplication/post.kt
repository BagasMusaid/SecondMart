package com.example.myapplication


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.FragmentPostBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [post.newInstance] factory method to
 * create an instance of this fragment.
 */
class post : Fragment () {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var binding: FragmentPostBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageuri: Uri? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        val view = binding.root



        val item = listOf("Elektronik","Sport","Fashion","Pets")
        val autoComplate : AutoCompleteTextView = view.findViewById(R.id.autoComplate)
        val adapter = ArrayAdapter(requireContext(), R.layout.list_items, item)

        storageReference = FirebaseStorage.getInstance().reference.child("images")
        databaseReference = FirebaseDatabase.getInstance().getReference("Barang")
        binding.btnPost.setOnClickListener{
            val name = binding.nameBrg.text.toString()
            val category = binding.autoComplate.text.toString()
            val location = binding.location.text.toString()
            val condition = binding.kondisiBrg.text.toString()
            val description = binding.descBrg.text.toString()
//            val barang = Posting_Class(name,category,location,condition,description)
//            val newReference = databaseReference.push()
            uploadImageAndSaveData(
                name,
                category,
                location,
                condition,
                description,
                imageuri
            )
        }
        autoComplate.setAdapter(adapter)
        autoComplate.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, i, l ->
            val itemSelected = adapterView.getItemIdAtPosition(i)
        }

        imageView = view.findViewById(R.id.IV_image)
        textView = view.findViewById(R.id.pilih)

        textView.setOnClickListener {
            ImagePicker.with(this)
                .crop(16f, 9f)
                .compress(1024)
                .maxResultSize(500, 400)
                .start()
        }

        return view

    }

    private fun uploadImageAndSaveData(name: String, category: String, location: String, condition: String, description: String, imageuri: Uri?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progres_layout)
        val dialog = builder.create()
        val placeholderImageResId = R.drawable.icon_image
        if (imageuri != null) {
            val imageRef =
                storageReference.child("images/" + UUID.randomUUID().toString())
            imageRef.putFile(imageuri)
                .addOnSuccessListener { taskSnapshot ->
                    dialog.show()
                    // Get the download URL from the task snapshot
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save other data along with the download URL

                        val barang = Posting_Class(
                            name,
                            category,
                            location,
                            condition,
                            description,
                            uri.toString() // Save the download URL to Firebase Database
                        )
                        val newReference = databaseReference.push()
                        newReference.setValue(barang)
                            .addOnSuccessListener {
                                dialog.dismiss()
                                binding.nameBrg.text = null
                                binding.autoComplate.text = null
                                binding.location.text = null
                                binding.kondisiBrg.text = null
                                binding.descBrg.text = null
                                val alertView = layoutInflater.inflate(R.layout.alert_succes, null)
                                val tvAlertMessage: TextView = alertView.findViewById(R.id.decSucces)
                                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                                alertDialogBuilder.setView(alertView)
                                val alertDialog = alertDialogBuilder.create()

                                tvAlertMessage.text = "Data berhasil disimpan"

                                // Tampilkan alert
                                alertDialog.show()

                                // Sesuaikan durasi tampilan alert sesuai kebutuhan
                                Handler(Looper.getMainLooper()).postDelayed({
                                    alertDialog.dismiss()
                                }, 5000)

                                Toast.makeText(
                                    requireContext(),
                                    "Data berhasil disimpan",
                                    Toast.LENGTH_SHORT
                                ).show()
                                imageView.setImageResource(placeholderImageResId)
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal menyimpan data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Gagal mengunggah gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(requireContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
        }
    }


    @Deprecated("Use registerForActivityResult", ReplaceWith("registerForActivityResult"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageuri: Uri? = data?.data
        imageView.setImageURI(imageuri)
        this.imageuri = imageuri
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment post.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            post().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}