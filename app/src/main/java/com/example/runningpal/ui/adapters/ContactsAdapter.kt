package com.example.runningpal.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.activities.FindContactActivity
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants
import com.example.runningpal.others.DatabaseUtility.convertStringToBitmap
import kotlinx.android.synthetic.main.user_item.view.*
import timber.log.Timber

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

        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent,false)

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

                val intent = Intent(context, FindContactActivity::class.java)
                    .also {
                        it.action = Constants.ACTION_SHOW_FRIEND_PROFILE
                        Timber.d("ter" + user.uid)
                        it.putExtra("id",user.uid)
                    }

                context.startActivity(intent)

            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}