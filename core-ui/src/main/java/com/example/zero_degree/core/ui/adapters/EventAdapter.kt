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
import com.example.zero_degree.core.api.model.Event
import com.example.zero_degree.core.ui.R as CoreUiR

class EventAdapter(
    private val onItemClick: ((Event) -> Unit)? = null,
    private val onRegisterClick: ((Event) -> Unit)? = null
) : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(CoreUiR.layout.item_event, parent, false)
        return EventViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivEvent: ImageView = itemView.findViewById(CoreUiR.id.ivEvent)
        private val tvEventName: TextView = itemView.findViewById(CoreUiR.id.tvEventName)
        private val tvEventDate: TextView = itemView.findViewById(CoreUiR.id.tvEventDate)
        
        fun bind(event: Event) {
            tvEventName.text = event.name
            tvEventDate.text = formatDate(event.date)
            
            event.imageUrl?.let { url ->
                ivEvent.load(url)
            } ?: run {
                ivEvent.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            
            itemView.setOnClickListener {
                onItemClick?.invoke(event)
            }
        }
        
        private fun formatDate(dateString: String): String {
            return try {
                // Пытаемся распарсить как timestamp (миллисекунды или секунды)
                val timestamp = dateString.toLongOrNull()
                if (timestamp != null) {
                    // Если число меньше 10^10, считаем что это секунды, иначе миллисекунды
                    val timestampMs = if (timestamp < 1_000_000_000_000L) {
                        timestamp * 1000
                    } else {
                        timestamp
                    }
                    val date = java.util.Date(timestampMs)
                    val format = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
                    format.format(date)
                } else {
                    // Если не timestamp, пытаемся распарсить как ISO 8601
                    if (dateString.contains("T")) {
                        val isoFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
                        val date = isoFormat.parse(dateString)
                        if (date != null) {
                            val format = java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault())
                            format.format(date)
                        } else {
                            dateString.split("T")[0]
                        }
                    } else {
                        dateString
                    }
                }
            } catch (e: Exception) {
                dateString
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

