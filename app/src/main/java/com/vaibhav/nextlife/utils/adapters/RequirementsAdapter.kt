package com.vaibhav.nextlife.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.nextlife.data.models.PostModel
import com.vaibhav.nextlife.databinding.RequirementsItemBinding

class RequirementsAdapter(private val onItemCLick: (PostModel) -> Unit) :
    ListAdapter<PostModel, RequirementsAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = RequirementsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: RequirementsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                onItemCLick(currentList[adapterPosition])
            }
        }

        fun bind(data: PostModel) {
            binding.post = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<PostModel>() {
        override fun areItemsTheSame(
            oldItem: PostModel,
            newItem: PostModel
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: PostModel,
            newItem: PostModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}