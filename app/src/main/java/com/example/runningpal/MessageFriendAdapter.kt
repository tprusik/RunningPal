package com.example.runningpal
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_cell.view.*
import kotlinx.android.synthetic.main.contact_message_row.view.*

class MessageFriendAdapter  (

        var messageContacts: List<MessageContact>

) : RecyclerView.Adapter<MessageFriendAdapter.MessageFriendViewHolder>(){

    inner class MessageFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageFriendViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_message_row,parent,false)

        return MessageFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageFriendViewHolder, position: Int) {

        holder.itemView.apply {

            val messageContact = messageContacts[position]

            tvContactMessageRowUser.text = messageContacts[position].name
            tvContactMessageRowMessage.text = messageContacts[position].message

            setOnClickListener{

                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("name",messageContact.name)
                intent.putExtra("uid",messageContact.uid)
                context.startActivity(intent)

            }

        }

    }

    override fun getItemCount(): Int {
        return  messageContacts.size
    }


}