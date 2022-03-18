package com.example.runningpal.ui.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.runningpal.R
import com.example.runningpal.db.RoomHistory
import com.example.runningpal.db.Runner
import com.example.runningpal.fragments.RunRoomStatisticsFragment
import kotlinx.android.synthetic.main.item_room_history.view.*


class RoomHistoryAdapter : RecyclerView.Adapter<RoomHistoryAdapter.RoomHistoryViewHolder>() {

    inner class RoomHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallback = object : DiffUtil.ItemCallback<RoomHistory>() {

        override fun areItemsTheSame(oldItem: RoomHistory, newItem: RoomHistory): Boolean {
            return oldItem.hashCode() == newItem.hashCode()}

        override fun areContentsTheSame(oldItem: RoomHistory, newItem: RoomHistory): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<RoomHistory>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHistoryViewHolder {
        return RoomHistoryViewHolder(
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

    override fun onBindViewHolder(holder: RoomHistoryViewHolder, position: Int) {

        val run = differ.currentList[position]

        holder.itemView.apply {


            tvRoomHistoryTimestamp.text = (tvRoomHistoryTimestamp.text.toString() + run.timestamp)
            tvRoomHistoryNumber.text = (tvRoomHistoryNumber.text.toString() + run.runners!!.size.toString())
            tvRoomHistoryPlace.text = (tvRoomHistoryPlace.text.toString() + run.place.toString())

            RunRoomStatisticsFragment.runners = run.runners!!

            llRunRoomHistory.setOnClickListener{

                Navigation.createNavigateOnClickListener(R.id.action_runRoomFragment_to_runRoomStatisticsFragment).onClick(holder.itemView)


            }

        }
    }


}