package com.vaibhav.nextlife.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.data.models.BloodGroupModel
import com.vaibhav.nextlife.databinding.CategoryItemBinding
import timber.log.Timber

class BloodGroupAdapter(
    private val context: Context,
    private val onCLickListener: (BloodGroupModel) -> Unit
) :
    ListAdapter<BloodGroupModel, BloodGroupAdapter.viewHolder>(DiffCall()) {

    private var posLastClicked = 0;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = CategoryItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun getPressedBloodGroup(): BloodGroupModel? {
        var indexVal: BloodGroupModel? = null;
        currentList.forEachIndexed { index, bloodGroupModel ->
            if (bloodGroupModel.isChecked)
                indexVal = bloodGroupModel
        }
        return indexVal
    }

    inner class viewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.bloodCard.setOnClickListener {
                Timber.d(currentList.toString())
                val newList = mutableListOf<BloodGroupModel>()
                currentList.forEachIndexed { index, bloodGroupModel ->
                    var isChecked = bloodGroupModel.isChecked
                    if (index == posLastClicked)
                        isChecked = false
                    else if (index == adapterPosition)
                        isChecked = true
                    newList.add(
                        BloodGroupModel(
                            bloodGroupModel.id,
                            bloodGroupModel.text,
                            isChecked
                        )
                    )
                }
                posLastClicked = adapterPosition
                Timber.d(newList.toString())
                submitList(newList)
                onCLickListener(newList[adapterPosition])
            }
        }


        fun bind(data: BloodGroupModel) {
            if (data.isChecked)
                binding.bloodCard.setCardBackgroundColor(context.getColor(R.color.bluegray))
            else
                binding.bloodCard.setCardBackgroundColor(context.getColor(R.color.white))
            binding.bloodGroup = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<BloodGroupModel>() {
        override fun areItemsTheSame(
            oldItem: BloodGroupModel,
            newItem: BloodGroupModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BloodGroupModel,
            newItem: BloodGroupModel
        ): Boolean {
            Timber.d((oldItem == newItem).toString())
            return oldItem.isChecked == newItem.isChecked
        }
    }

}