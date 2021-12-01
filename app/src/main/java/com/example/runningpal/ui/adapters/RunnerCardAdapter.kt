package com.example.runningpal.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.R
import com.example.runningpal.db.Run
import com.example.runningpal.db.Runner
import com.example.runningpal.others.DatabaseUtility
import kotlinx.android.synthetic.main.runner_card_item.view.*

class RunnerCardAdapter(

) : RecyclerView.Adapter<RunnerCardAdapter.RunnerCardViewHolder>(){

    inner class RunnerCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    val diffCallback = object : DiffUtil.ItemCallback<Runner>() {

        override fun areItemsTheSame(oldItem: Runner, newItem: Runner): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Runner, newItem: Runner): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Runner>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunnerCardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.runner_card_item,parent,false)

        return RunnerCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunnerCardViewHolder, position: Int) {

        val user = differ.currentList[position]

        holder.itemView.apply {

            tvRunnerCardName.setText("Jarek")
            val temp = user.avgSpeedKmh
            tvRunnerCardTemp.setText(temp.toString())
            val distance =  user.distanceMetres
            tvRunnerCardDistance.setText(distance.toString())


            if(user.pic == null)
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivRunnerCardItem)
            else
            {
                val pic = DatabaseUtility.convertStringToBitmap(user.pic!!)

                Glide.with(this).load(pic).into(ivRunnerCardItem)
            }

        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
