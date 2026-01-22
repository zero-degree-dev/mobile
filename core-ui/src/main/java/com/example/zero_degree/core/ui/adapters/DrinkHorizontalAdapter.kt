package com.example.zero_degree.core.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.zero_degree.core.api.model.Drink
import com.example.zero_degree.core.ui.R as CoreUiR

class DrinkHorizontalAdapter(
    private val onItemClick: (Drink) -> Unit
) : ListAdapter<Drink, DrinkHorizontalAdapter.DrinkViewHolder>(DrinkDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(CoreUiR.layout.item_drink_horizontal, parent, false)
        return DrinkViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class DrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDrink: ImageView = itemView.findViewById(CoreUiR.id.ivDrink)
        private val tvDrinkName: TextView = itemView.findViewById(CoreUiR.id.tvDrinkName)
        private val tvDrinkPrice: TextView = itemView.findViewById(CoreUiR.id.tvDrinkPrice)
        
        fun bind(drink: Drink) {
            tvDrinkName.text = drink.name
            tvDrinkPrice.text = "${drink.price.toInt()} ₽"
            
            drink.imageUrl?.let { url ->
                try {
                    ivDrink.load(url)
                } catch (e: Exception) {
                    ivDrink.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } ?: run {
                ivDrink.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            
            itemView.setOnClickListener {
                onItemClick(drink)
            }
        }
    }
    
    class DrinkDiffCallback : DiffUtil.ItemCallback<Drink>() {
        override fun areItemsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Drink, newItem: Drink): Boolean {
            return oldItem == newItem
        }
    }
}

