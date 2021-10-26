package com.example.runningpal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runningpal.db.Message
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chat_message_receive.view.*
import kotlinx.android.synthetic.main.chat_message_send.view.*


class MessageAdapter(
    var messages: List<Message>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private const val SEND_MESSAGE = 1
        private const val RECEIVE_MESSAGE = 2
    }

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType.equals(RECEIVE_MESSAGE)) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_message_receive, parent, false)
            return ReceiveViewHolder(view)
        }
        else{

            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_message_send, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messages[position].message


        holder.itemView.apply {

            if(holder.javaClass == SentViewHolder::class.java)
                tvChatCellSend.text = currentMessage

            if(holder.javaClass == ReceiveViewHolder::class.java)
                tvChatCellReceive.text = currentMessage



            }


    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messages[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderID))
        return RECEIVE_MESSAGE
        else
            return SEND_MESSAGE
    }

    override fun getItemCount(): Int {
      return messages.size
    }
}