package com.example.zero_degree.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.R
import com.example.zero_degree.ui.adapters.DrinkAdapter
import com.example.zero_degree.ui.viewmodel.DrinkCatalogViewModel
import com.google.android.material.chip.Chip

class DrinkCatalogFragment : Fragment() {
    
    private lateinit var viewModel: DrinkCatalogViewModel
    private lateinit var rvDrinks: RecyclerView
    private lateinit var progressBar: View
    private lateinit var chipBeer: Chip
    private lateinit var chipLemonade: Chip
    private lateinit var chipSweet: Chip
    private lateinit var chipBitter: Chip
    
    private val drinkAdapter = DrinkAdapter { drink ->
        val fragment = DrinkDetailFragment().apply {
            arguments = Bundle().apply {
                putInt("drinkId", drink.id)
            }
        }
        (activity as? com.example.zero_degree.MainActivity)?.replaceFragment(fragment)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drink_catalog, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[DrinkCatalogViewModel::class.java]
        
        rvDrinks = view.findViewById(R.id.rvDrinks)
        progressBar = view.findViewById(R.id.progressBar)
        chipBeer = view.findViewById(R.id.chipBeer)
        chipLemonade = view.findViewById(R.id.chipLemonade)
        chipSweet = view.findViewById(R.id.chipSweet)
        chipBitter = view.findViewById(R.id.chipBitter)
        
        // Настройка RecyclerView
        rvDrinks.layoutManager = LinearLayoutManager(context)
        rvDrinks.adapter = drinkAdapter
        
        // Фильтры по типу
        chipBeer.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTypeFilter(if (isChecked) "пиво" else null)
        }
        
        chipLemonade.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTypeFilter(if (isChecked) "лимонад" else null)
        }
        
        // Фильтры по вкусу
        chipSweet.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTasteFilter(if (isChecked) "сладкий" else null)
        }
        
        chipBitter.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTasteFilter(if (isChecked) "горький" else null)
        }
        
        // Наблюдаем за данными
        viewModel.drinks.observe(viewLifecycleOwner) { drinks ->
            drinkAdapter.submitList(drinks)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Загружаем данные
        viewModel.loadDrinks()
    }
}

