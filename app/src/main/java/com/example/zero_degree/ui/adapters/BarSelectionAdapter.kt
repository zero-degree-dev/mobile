package com.example.zero_degree.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.R
import com.example.zero_degree.data.model.Bar

class BarSelectionAdapter(
    private val onItemClick: (Bar) -> Unit
) : ListAdapter<Bar, BarSelectionAdapter.BarViewHolder>(BarDiffCallback()) {
    
    private var selectedBar: Bar? = null
    
    fun setSelectedBar(bar: Bar?) {
        val oldSelected = selectedBar
        selectedBar = bar
        oldSelected?.let { notifyItemChanged(currentList.indexOf(it)) }
        bar?.let { notifyItemChanged(currentList.indexOf(it)) }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bar_selection, parent, false)
        return BarViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class BarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBarName: TextView = itemView.findViewById(R.id.tvBarName)
        private val tvBarAddress: TextView = itemView.findViewById(R.id.tvBarAddress)
        private val tvSelected: TextView = itemView.findViewById(R.id.tvSelected)
        
        fun bind(bar: Bar) {
            tvBarName.text = bar.name
            tvBarAddress.text = bar.address
            
            val isSelected = selectedBar?.id == bar.id
            tvSelected.visibility = if (isSelected) View.VISIBLE else View.GONE
            
            itemView.setOnClickListener {
                onItemClick(bar)
            }
        }
    }
    
    class BarDiffCallback : DiffUtil.ItemCallback<Bar>() {
        override fun areItemsTheSame(oldItem: Bar, newItem: Bar): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Bar, newItem: Bar): Boolean {
            return oldItem == newItem
        }
    }
}


