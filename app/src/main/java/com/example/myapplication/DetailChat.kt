package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.ImageView

class DetailChat : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private val chatMessages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_chat)

        val getData = intent.getParcelableExtra<DataClass>("android")
        if (getData != null) {
            val detailTitle: TextView = findViewById(R.id.detailNama)
            val detailImage: ImageView = findViewById(R.id.detailImage)
            detailTitle.text = getData.dataNama
            detailImage.setImageResource(getData.dataImage)
        }

        val imageBack: ImageView = findViewById(R.id.backButton)
        imageBack.setOnClickListener {
            // Implementasi untuk kembali ke Home
            finish()
        }

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.detailPesan)
        messageAdapter = MessageAdapter(chatMessages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = messageAdapter

        // Set up Send button click listener
        val sendButton: ImageButton = findViewById(R.id.buttonSend)
        val messageEditText: EditText = findViewById(R.id.editText)

        sendButton.setOnClickListener {
            val message = messageEditText.text.toString().trim()
            if (message.isNotEmpty()) {
                val sender = "You"  // You can replace this with the actual sender's name
                val chatMessage = ChatMessage(sender, message)

                // Add the message to the list and notify the adapter
                chatMessages.add(chatMessage)
                messageAdapter.notifyItemInserted(chatMessages.size - 1)

                // Clear the message input
                messageEditText.text.clear()
            }
        }
    }
}
