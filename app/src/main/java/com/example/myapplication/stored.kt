package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.Extensions.toast
import com.example.myapplication.Models.stored_class
import com.example.myapplication.databinding.FragmentStoredBinding
import com.example.myapplication.rvadapter.Like
import com.example.myapplication.rvadapter.ProductOnClickInterface
import com.example.myapplication.rvadapter.storedAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class stored : Fragment(), ProductOnClickInterface, Like {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentStoredBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: storedAdapter
    private lateinit var likedProductList: ArrayList<stored_class>


    private var likeDBRef = Firebase.firestore.collection("LikedProducts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStoredBinding.inflate(inflater, container, false)
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progres_layout)
        val dialog = builder.create()
        dialog.show()
        val view = binding.root
        binding = FragmentStoredBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        likedProductList = ArrayList()
        adapter = storedAdapter(requireContext(),likedProductList,this,this)





        val productLayoutManager = GridLayoutManager(context, 2)
        binding.RVStored.layoutManager = productLayoutManager
        binding.RVStored.adapter = adapter


        displayLikedProducts()
        dialog.dismiss()
        return view
    }

    private fun displayLikedProducts() {
        likeDBRef
            .whereEqualTo("uid" , auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (item in querySnapshot) {
                    val likedProduct = item.toObject<stored_class>()
                    likedProductList.add(likedProduct)
                    adapter.notifyDataSetChanged()

                }

            }
            .addOnFailureListener{
                requireActivity().toast(it.localizedMessage!!)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment stored.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            stored().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClickProduct(item: stored_class) {
        TODO("Not yet implemented")
    }

    override fun onclike(item: stored_class) {
        likeDBRef
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("pid", item.pid)
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (queryDocumentSnapshot in querySnapshot) {
                    // Hapus item dari Firebase
                    likeDBRef.document(queryDocumentSnapshot.id).delete()

                    // Hapus item dari likedProductList
                    likedProductList.remove(item)

                    // Perbarui tampilan
                    adapter.notifyDataSetChanged()

                    // Tampilkan pesan sukses
                    requireActivity().toast("Removed From the Stored Items")

                    // Keluar dari loop setelah menghapus satu item
                    break
                }
            }
            .addOnFailureListener {
                requireActivity().toast("Failed To Remove From Liked Items")
            }
    }
}