package com.example.runningpal.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.fragments.FriendProfileFragment
import com.example.runningpal.others.Utils.convertStringToBitmap
import kotlinx.android.synthetic.main.user_item.view.*


class ContactsAdapter (

) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(){

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)

        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {


        val user = differ.currentList[position]

        holder.itemView.apply {

            tvUserItemNick.text = user.nick

            if(user.profilePic == null)
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivUserItem)
            else
            {
                val pic =convertStringToBitmap(user.profilePic!!)
                Glide.with(this).load(pic).into(ivUserItem)
            }

            setOnClickListener{
                FriendProfileFragment.friend.postValue(user)
                Navigation.createNavigateOnClickListener(R.id.friendProfileFragment).onClick(holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}