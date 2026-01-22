package com.example.zero_degree.core.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.ui.R as CoreUiR

class BarHorizontalAdapter(
    private val onItemClick: (Bar) -> Unit
) : ListAdapter<Bar, BarHorizontalAdapter.BarViewHolder>(BarDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(CoreUiR.layout.item_bar_horizontal, parent, false)
        return BarViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: BarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class BarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivBar: ImageView = itemView.findViewById(CoreUiR.id.ivBar)
        private val tvBarName: TextView = itemView.findViewById(CoreUiR.id.tvBarName)
        
        fun bind(bar: Bar) {
            tvBarName.text = bar.name
            
            bar.imageUrl?.let { url ->
                try {
                    ivBar.load(url)
                } catch (e: Exception) {
                    ivBar.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } ?: run {
                ivBar.setImageResource(android.R.drawable.ic_menu_gallery)
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

