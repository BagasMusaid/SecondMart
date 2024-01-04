package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Models.chat_class
import com.example.myapplication.databinding.FragmentSearchBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Search.newInstance] factory method to
 * create an instance of this fragment.
 */
class Search : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<chat_class>
    private lateinit var adapter: chatAdapter
    private lateinit var binding: FragmentSearchBinding

    lateinit var imageList: Array<Int>
    lateinit var nameList: Array<String>
    lateinit var topikList: Array<String>
    lateinit var notifList: Array<String>

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
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        imageList = arrayOf(R.drawable.tegar, R.drawable.ponyo, R.drawable.feya2)
        nameList = arrayOf("Tegar maliisa","Catur Hendy","Freya Nasifa")
        topikList = arrayOf("Iphone barter sama nokia?","kipas masih ada ga","Lokasi pasnya mana??")
        notifList = arrayOf("1","2","")

        recyclerView = binding.RVChat
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        dataList = arrayListOf<chat_class>()
        adapter = chatAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter
        getData()
        // Inflate the layout for this fragment
       return view
    }

    private fun getData() {
        for(i in imageList.indices){
            val dataClass = chat_class(imageList[i], nameList[i], topikList[i], notifList[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = chatAdapter(requireContext(), dataList)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Search.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Search().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}