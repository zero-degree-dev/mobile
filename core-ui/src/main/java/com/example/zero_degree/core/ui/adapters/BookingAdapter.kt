package com.example.zero_degree.core.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.ui.R as CoreUiR
import com.google.android.material.button.MaterialButton

class BookingAdapter(
    private val onEditClick: (Booking) -> Unit,
    private val onCancelClick: (Booking) -> Unit
) : ListAdapter<Booking, BookingAdapter.BookingViewHolder>(BookingDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(CoreUiR.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvBarName: TextView = itemView.findViewById(CoreUiR.id.tvBarName)
        private val tvDate: TextView = itemView.findViewById(CoreUiR.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(CoreUiR.id.tvTime)
        private val tvGuests: TextView = itemView.findViewById(CoreUiR.id.tvGuests)
        private val tvStatus: TextView = itemView.findViewById(CoreUiR.id.tvStatus)
        private val btnEdit: MaterialButton = itemView.findViewById(CoreUiR.id.btnEdit)
        private val btnCancel: MaterialButton = itemView.findViewById(CoreUiR.id.btnCancel)
        
        fun bind(booking: Booking) {
            // Используем название бара, если есть, иначе ID
            val barName = booking.bar?.name ?: "Бар ID: ${booking.barId.take(8)}..."
            tvBarName.text = barName
            
            // Форматируем дату и время
            tvDate.text = booking.date
            // Убираем секунды из времени, если есть
            val timeDisplay = if (booking.time.length > 5) {
                booking.time.substring(0, 5)
            } else {
                booking.time
            }
            tvTime.text = timeDisplay
            tvGuests.text = "${booking.guestsCount} ${if (booking.guestsCount == 1) "гость" else "гостей"}"
            
            val statusText = when (booking.status) {
                "pending" -> "Ожидает подтверждения"
                "confirmed" -> "Подтверждено"
                "cancelled" -> "Отменено"
                else -> booking.status
            }
            tvStatus.text = statusText
            
            btnEdit.setOnClickListener {
                onEditClick(booking)
            }
            
            btnCancel.setOnClickListener {
                onCancelClick(booking)
            }
        }
    }
    
    class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem == newItem
        }
    }
}

