package com.example.zero_degree.core.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.core.api.model.Review
import com.example.zero_degree.core.ui.R as CoreUiR

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(CoreUiR.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserName: TextView = itemView.findViewById(CoreUiR.id.tvUserName)
        private val tvRating: TextView = itemView.findViewById(CoreUiR.id.tvRating)
        private val tvComment: TextView = itemView.findViewById(CoreUiR.id.tvComment)
        private val tvDate: TextView = itemView.findViewById(CoreUiR.id.tvDate)
        
        fun bind(review: Review) {
            tvUserName.text = review.userName ?: "Пользователь"
            // Отображаем рейтинг звездочками (заполненные и пустые)
            val filledStars = "★".repeat(review.rating.coerceIn(0, 5))
            val emptyStars = "☆".repeat(5 - review.rating.coerceIn(0, 5))
            tvRating.text = "$filledStars$emptyStars"
            tvComment.text = review.comment ?: ""
            // Форматируем дату, если есть
            val date = review.date
            tvDate.text = if (!date.isNullOrBlank()) {
                try {
                    // Если дата в формате ISO 8601, извлекаем дату
                    if (date.contains("T")) {
                        date.split("T")[0]
                    } else {
                        date
                    }
                } catch (e: Exception) {
                    date
                }
            } else {
                ""
            }
        }
    }
    
    class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}

