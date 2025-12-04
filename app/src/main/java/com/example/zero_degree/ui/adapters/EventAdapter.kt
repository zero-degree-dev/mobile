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
import com.example.zero_degree.data.model.Event

class EventAdapter : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivEvent: ImageView = itemView.findViewById(R.id.ivEvent)
        private val tvEventName: TextView = itemView.findViewById(R.id.tvEventName)
        private val tvEventDate: TextView = itemView.findViewById(R.id.tvEventDate)
        
        fun bind(event: Event) {
            tvEventName.text = event.name
            tvEventDate.text = event.date
            
            ivEvent.load(event.imageUrl) {
                placeholder(R.drawable.ic_launcher_background)
            }
        }
    }
    
    class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}


