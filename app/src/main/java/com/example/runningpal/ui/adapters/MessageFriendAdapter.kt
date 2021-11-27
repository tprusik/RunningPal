package com.example.runningpal.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.ChatActivity
import com.example.runningpal.FindContactActivity
import com.example.runningpal.R
import com.example.runningpal.db.MessageContact
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants
import com.example.runningpal.others.DatabaseUtility
import kotlinx.android.synthetic.main.contact_message_item.view.*
import kotlinx.android.synthetic.main.user_item.view.*
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
            tvContactMessage.text = user.message

            if(user.profilePic == null)
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivContactMessage)
            else
            {
                val pic = DatabaseUtility.convertStringToBitmap(user.profilePic!!)

                Glide.with(this).load(pic).into(ivContactMessage)
            }


            setOnClickListener{

                val intent = Intent(context, ChatActivity::class.java)
                        .also {
                            it.putExtra("id",user.uid)
                            it.putExtra("name",user.name)
                        }

                context.startActivity(intent)

            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}