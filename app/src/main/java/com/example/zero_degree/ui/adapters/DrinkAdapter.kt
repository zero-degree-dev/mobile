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
import com.example.zero_degree.data.model.Drink

class DrinkAdapter(
    private val onItemClick: (Drink) -> Unit
) : ListAdapter<Drink, DrinkAdapter.DrinkViewHolder>(DrinkDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_drink, parent, false)
        return DrinkViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class DrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivDrink: ImageView = itemView.findViewById(R.id.ivDrink)
        private val tvDrinkName: TextView = itemView.findViewById(R.id.tvDrinkName)
        private val tvDrinkDescription: TextView = itemView.findViewById(R.id.tvDrinkDescription)
        private val tvDrinkPrice: TextView = itemView.findViewById(R.id.tvDrinkPrice)
        
        fun bind(drink: Drink) {
            tvDrinkName.text = drink.name
            tvDrinkDescription.text = drink.description
            tvDrinkPrice.text = "${drink.price} ₽"
            
            ivDrink.load(drink.imageUrl) {
                placeholder(R.drawable.ic_launcher_background)
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



