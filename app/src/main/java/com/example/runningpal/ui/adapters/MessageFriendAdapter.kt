package com.example.runningpal.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.activities.ChatActivity
import com.example.runningpal.R
import com.example.runningpal.db.MessageContact
import com.example.runningpal.others.Utils
import kotlinx.android.synthetic.main.contact_message_item.view.*
import timber.log.Timber

class MessageFriendAdapter (


        ) : RecyclerView.Adapter<MessageFriendAdapter.MessageFriendViewHolder>(){

    inner class MessageFriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    val diffCallback = object : DiffUtil.ItemCallback<MessageContact>() {

        override fun areItemsTheSame(oldItem: MessageContact, newItem: MessageContact): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: MessageContact, newItem: MessageContact): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<MessageContact>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageFriendViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_message_item,parent,false)

        return MessageFriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageFriendViewHolder, position: Int) {

        val user = differ.currentList[position]

        holder.itemView.apply {

            tvContactMessageUser.text = user.name
            tvContactMessage.text = user.lastMessage
            Timber.d("MessageFriendAdapter  " +  user.uid)
            if(user.profilePic == null)
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivContactMessage)
            else
            {
                val pic = Utils.convertStringToBitmap(user.profilePic!!)
                Glide.with(this).load(pic).into(ivContactMessage)
            }

            setOnClickListener{

                ChatActivity.contact.postValue(user)
                Navigation.createNavigateOnClickListener(R.id.action_messageFragment_to_chatActivity).onClick(holder.itemView)
            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}