package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityChatBinding
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var binding: ActivityChatBinding

    private lateinit var imageList: Array<Int>
    private lateinit var titleList: Array<String>
    private lateinit var descriptionList: Array<String>
    private lateinit var descList: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityChatBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf() // Initialize dataList here

        imageList = arrayOf(
            R.drawable.gojo,
            R.drawable.tegar,
            R.drawable.feya2,
            R.drawable.ponyo,
            R.drawable.feya2
        )

        titleList = arrayOf(
            "Gojo Kiko",
            "Hoys Anggara",
            "Ayy",
            "Bagas",
            "Dava"
        )

        descriptionList = arrayOf(
            "Share lok om",
            "Barter iphone 11",
            "Barter mau ga?",
            "Otw",
            "Up jauh",
        )

        descList = arrayOf(
            getString(R.string.satoru),
            getString(R.string.sukuna),
            getString(R.string.yuta),
            getString(R.string.toji),
            getString(R.string.geto),
        )

        getData() // Call getData to populate dataList

        val myAdapter = AdapterClass(dataList)
        recyclerView.adapter = myAdapter

        // Set item click listener
        myAdapter.onItemClick = { data ->
            val intent = Intent(requireContext(), DetailChat::class.java)
            intent.putExtra("android", data)
            startActivity(intent)
        }

        return view
    }

    private fun getData() {
        dataList.clear() // Clear dataList before adding new data
        for (i in imageList.indices) {
            val dataClass = DataClass(imageList[i], titleList[i], descriptionList[i], descList[i])
            dataList.add(dataClass)
        }
    }
}
