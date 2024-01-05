package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.Extensions.toast
import com.example.myapplication.Models.Posting_Class
import com.example.myapplication.Models.stored_class
import com.example.myapplication.databinding.FragmentHomeBinding

import com.example.myapplication.rvadapter.MyAdapter
import com.example.myapplication.rvadapter.produk

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

interface LikedOnClickInterface {

}

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment(), View.OnClickListener, LikedOnClickInterface, produk,
    com.example.myapplication.rvadapter.LikedOnClickInterface {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dataList: ArrayList<Posting_Class>
    private lateinit var adapter: MyAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private var likeDBRef = Firebase.firestore.collection("LikedProducts")
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.RVCard.layoutManager = gridLayoutManager
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)
        builder.setView(R.layout.progres_layout)
        val dialog = builder.create()
        dialog.show()


//        start category
        val cardEle: CardView = view.findViewById(R.id.C_elektronik)
        val cardSport: CardView = view.findViewById(R.id.C_Sports)
        val cardFashion: CardView = view.findViewById(R.id.C_Fashion)
        val cardPets: CardView = view.findViewById(R.id.C_Pets)

        val tvEle : TextView = view.findViewById(R.id.tvElec)
        val tvSport : TextView = view.findViewById(R.id.tvSport)
        val tvFashion : TextView = view.findViewById(R.id.tvFashion)
        val tvPets : TextView = view.findViewById(R.id.tvPets)

        cardEle.setOnClickListener(){
            val intent = Intent(requireContext(),CategoryActivity::class.java)
            intent.putExtra("content_value",tvEle.text.toString())
            startActivity(intent)
        }

        cardSport.setOnClickListener(){
            val intent = Intent(requireContext(),CategoryActivity::class.java)
            intent.putExtra("content_value",tvSport.text.toString())
            startActivity(intent)
        }
        cardFashion.setOnClickListener(){
            val intent = Intent(requireContext(),CategoryActivity::class.java)
            intent.putExtra("content_value",tvFashion.text.toString())
            startActivity(intent)
        }
        cardPets.setOnClickListener(){
            val intent = Intent(requireContext(),CategoryActivity::class.java)
            intent.putExtra("content_value",tvPets.text.toString())
            startActivity(intent)
        }
//        end category

        // Baca nama pengguna dari SharedPreferences
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "") ?: ""

        // Atur teks pada TextView
        val fullNameTextView: TextView = view.findViewById(R.id.full_name)
        fullNameTextView.text = "Hai, $username"


        // Muat URL foto profil dari SharedPreferences
        val profileImageUrl = sharedPreferences?.getString("profileImageUrl", "")

        // Muat foto profil menggunakan Glide
        if (!profileImageUrl.isNullOrEmpty()) {
            Glide.with(requireContext())
                .load(profileImageUrl)
                .into(binding.profileImage)
        }

        // Atur teks pada TextView tanggal
        val tanggalTextView: TextView = view.findViewById(R.id.TV_tanggal)
        val currentDate = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date())
        tanggalTextView.text = currentDate


//      profile
        binding.profileImage.setOnClickListener(this)
        binding.iconLocation.setOnClickListener(this)

        dataList= ArrayList()
        adapter= MyAdapter(requireContext(),dataList, this, this)

        binding.RVCard.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Barang")
        auth = FirebaseAuth.getInstance()
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    dataList.clear()

                    for (itemSnapshot in snapshot.children) {
                        val dataClass = itemSnapshot.getValue(Posting_Class::class.java)
                        if (dataClass != null) {
                            dataList.add(dataClass)
                        }
                    }

                    adapter.notifyDataSetChanged()
                    dialog.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Log the error message
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.profile_image -> run {
                val intentProfile = Intent(requireContext(), profileActivity::class.java )
                startActivity(intentProfile)
            }
            R.id.icon_location -> run{
                val intentLocation = Intent(requireContext(), locationActivity::class.java)
                startActivity(intentLocation)
            }
        }
    }

    override fun onClickLike(item: Posting_Class) {
        likeDBRef
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("name", item.name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // Barang belum disukai sebelumnya, tambahkan ke LikedProducts
                    likeDBRef
                        .add(stored_class(item.id, auth.currentUser!!.uid, item.name, item.category, item.location, item.condition, item.description, item.imageuri))
                        .addOnSuccessListener {
                            requireActivity().toast("Postingan Tersimpan")
                        }
                        .addOnFailureListener {
                            requireActivity().toast("Gagal Menyimpan Postingan")
                        }
                } else {
                    // Barang sudah disukai sebelumnya, beri informasi kepada pengguna
                    requireActivity().toast("Postingan sudah Tersimpan")
                }
            }
            .addOnFailureListener {
                requireActivity().toast("Gagal memeriksa simpan Postingan")
            }
    }

    override fun onClickProduct(item: Posting_Class) {

    }


}