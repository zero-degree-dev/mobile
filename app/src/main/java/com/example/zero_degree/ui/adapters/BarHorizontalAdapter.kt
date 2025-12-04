package com.example.zero_degree.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.zero_degree.R
import com.example.zero_degree.data.model.Bar

class BarHorizontalAdapter(
    private val onItemClick: (Bar) -> Unit
) : ListAdapter<Bar, BarHorizontalAdapter.BarViewHolder>(BarDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bar_horizontal, parent, false)
        return BarViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class BarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBar: ImageView = itemView.findViewById(R.id.ivBar)
        private val tvBarName: TextView = itemView.findViewById(R.id.tvBarName)
        
        fun bind(bar: Bar) {
            tvBarName.text = bar.name
            
            ivBar.load(bar.imageUrl) {
                placeholder(R.drawable.ic_launcher_background)
            }
            
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


