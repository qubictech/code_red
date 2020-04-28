package com.tarmsbd.coronavirus.prediction.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tarmsbd.coronavirus.prediction.app.data.models.RecentStatus
import com.tarmsbd.coronavirus.prediction.app.databinding.ItemStatusListBinding

class RecentStatusAdapter() :
    ListAdapter<RecentStatus, RecentStatusAdapter.StatusHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<RecentStatus>() {
        override fun areItemsTheSame(oldItem: RecentStatus, newItem: RecentStatus): Boolean {
            return oldItem.age == newItem.age && oldItem.area == newItem.area && oldItem.gender == newItem.gender && oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: RecentStatus, newItem: RecentStatus): Boolean {
            return oldItem.age == newItem.age && oldItem.area == newItem.area && oldItem.gender == newItem.gender && oldItem.time == newItem.time
        }

    }

    class StatusHolder(private val itemStatusListBinding: ItemStatusListBinding) :
        RecyclerView.ViewHolder(itemStatusListBinding.root) {

        fun bind(recentStatus: RecentStatus) {
            itemStatusListBinding.status = recentStatus
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusHolder {
        val itemStatusListBinding = ItemStatusListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return StatusHolder(itemStatusListBinding)
    }

    override fun onBindViewHolder(holder: StatusHolder, position: Int) {
        holder.bind(getItem(position))
    }
}