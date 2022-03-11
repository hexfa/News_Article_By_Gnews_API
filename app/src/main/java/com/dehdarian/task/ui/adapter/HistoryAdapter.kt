package com.dehdarian.task.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dehdarian.task.databinding.ItemHistoryBinding

/**
 * Map data keyword of history searches on recyclerview
 */
class HistoryAdapter :
    RecyclerView.Adapter<HistoryAdapter.CustomViewHolder>() {

    private val dataList : ArrayList<String> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(histories : ArrayList<String>){
        dataList.clear()
        dataList.addAll(histories)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {

        val itemBinding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemBinding)
    }
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(private val itemHistoryBinding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(itemHistoryBinding.root) {
        fun bind(history: String)
        {
            itemHistoryBinding.tvHistory.text = history
        }
    }
}