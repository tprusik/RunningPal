package com.example.runningpal.ui.adapters

import android.content.Intent
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
import com.example.runningpal.db.User
import com.example.runningpal.others.Utils
import kotlinx.android.synthetic.main.user_item.view.*
import timber.log.Timber

class NewMessageAdapter: RecyclerView.Adapter<NewMessageAdapter.NewMessageViewHolder>(){

    inner class NewMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    val diffCallback = object : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<User>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMessageViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent,false)

        return NewMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewMessageViewHolder, position: Int) {


        val user = differ.currentList[position]

        holder.itemView.apply {

            tvUserItemNick.text = user.nick

            if(user.profilePic == null)
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivUserItem)
            else
            {
                val pic = Utils.convertStringToBitmap(user.profilePic!!)

                Glide.with(this).load(pic).into(ivUserItem)
            }

            setOnClickListener{

                val messageContact = MessageContact(user.nick,null,null,user.uid,user.profilePic)

                Timber.d(" new message ad " + messageContact.name + "  " +  messageContact.uid)
               ChatActivity.contact.postValue(messageContact)

                Navigation.createNavigateOnClickListener(R.id.action_messageFragment_to_chatActivity).onClick(holder.itemView)
            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
