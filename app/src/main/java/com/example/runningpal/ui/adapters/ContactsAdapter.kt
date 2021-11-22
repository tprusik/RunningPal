package com.example.runningpal.ui.adapters

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.ChatActivity
import com.example.runningpal.R
import com.example.runningpal.db.User
import kotlinx.android.synthetic.main.item_run.view.*
import kotlinx.android.synthetic.main.user_item.view.*

class ContactsAdapter (


) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(){

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    val diffCallback = object : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
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
                val pic =user.profilePic
                val decodedString: ByteArray = Base64.decode(pic, Base64.URL_SAFE)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                Glide.with(this).load(decodedByte).into(ivUserItem)
            }

            setOnClickListener{

                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("name",user.nick)
                intent.putExtra("uid",user.id)
                context.startActivity(intent)

            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}