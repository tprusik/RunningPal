package com.example.runningpal

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runningpal.db.User
import kotlinx.android.synthetic.main.contact_cell.view.*

class ContactsAdapter (

        var contacts: List<User>

) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(){

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_cell,parent,false)

        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {

        holder.itemView.apply {

             val choosenContact = contacts[position]

            tvContact.text = contacts[position].nick

            setOnClickListener{

                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("name",choosenContact.nick)
                intent.putExtra("uid",choosenContact.uid)
                context.startActivity(intent)

            }

        }


    }

    override fun getItemCount(): Int {
        return  contacts.size
    }


}