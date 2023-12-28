package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.databinding.FragmentPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dataList: ArrayList<Posting_Class>
    private lateinit var adapter: MyAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var btnIntent: Button
    private lateinit var circleImageView: CircleImageView
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

        // Baca nama pengguna dari SharedPreferences
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "") ?: ""

        // Atur teks pada TextView
        val fullNameTextView: TextView = view.findViewById(R.id.full_name)
        fullNameTextView.text = "Hai, $username"



//        profile
        binding.profileImage.setOnClickListener(this)
        binding.iconLocation.setOnClickListener(this)

        dataList= ArrayList()
        adapter= MyAdapter(requireContext(), dataList)
        binding.RVCard.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("Barang")
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
}