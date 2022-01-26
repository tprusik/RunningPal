package com.example.runningpal.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.R
import com.example.runningpal.db.RoomHistory
import com.example.runningpal.db.Run
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.others.Utils
import kotlinx.android.synthetic.main.item_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class RoomHistoryAdapter : RecyclerView.Adapter<RoomHistoryAdapter.RunViewHolder>() {

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallback = object : DiffUtil.ItemCallback<RoomHistory>() {

        override fun areItemsTheSame(oldItem: RoomHistory, newItem: RoomHistory): Boolean {
            return oldItem.hashCode() == newItem.hashCode()}

        override fun areContentsTheSame(oldItem: RoomHistory, newItem: RoomHistory): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<RoomHistory>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_room_history,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {

        val run = differ.currentList[position]

        holder.itemView.apply {




        }
    }
}