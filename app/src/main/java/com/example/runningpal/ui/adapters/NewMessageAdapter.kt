package com.example.runningpal.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.activities.ChatActivity
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.DatabaseUtility
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
                val pic = DatabaseUtility.convertStringToBitmap(user.profilePic!!)

                Glide.with(this).load(pic).into(ivUserItem)
            }

            setOnClickListener{
//
                val intent = Intent(context, ChatActivity::class.java)
                        .also {

                            Timber.d("New message adapter"+ user.uid)
                            it.putExtra("pic",user.profilePic)
                            it.putExtra("id",user.uid)
                            it.putExtra("name",user.nick)
                        }

                context.startActivity(intent)

            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
