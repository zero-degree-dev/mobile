package com.example.zero_degree.features.drinks.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.core.ui.adapters.DrinkAdapter
import com.example.zero_degree.features.drinks.impl.R
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class DrinkCatalogFragment : ProtectedFragment() {
    
    private val viewModel by viewModels<DrinkCatalogViewModel> {
        DrinkCatalogViewModelFactory(requireActivity().application)
    }
    
    private lateinit var rvDrinks: RecyclerView
    private lateinit var progressBar: View
    private lateinit var chipBeer: Chip
    private lateinit var chipLemonade: Chip
    private lateinit var chipSweet: Chip
    private lateinit var chipBitter: Chip
    
    private val drinkAdapter = DrinkAdapter { drink ->
        val fragment = DrinkDetailFragment().apply {
            arguments = Bundle().apply {
                putString("drinkId", drink.id)
            }
        }
        NavigationHelper.replaceFragment(activity, fragment)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_drink_catalog, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        rvDrinks = view.findViewById(R.id.rvDrinks)
        progressBar = view.findViewById(R.id.progressBar)
        chipBeer = view.findViewById(R.id.chipBeer)
        chipLemonade = view.findViewById(R.id.chipLemonade)
        chipSweet = view.findViewById(R.id.chipSweet)
        chipBitter = view.findViewById(R.id.chipBitter)
        
        rvDrinks.layoutManager = LinearLayoutManager(requireContext())
        rvDrinks.adapter = drinkAdapter
        
        chipBeer.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTypeFilter(if (isChecked) "пиво" else null)
        }
        
        chipLemonade.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTypeFilter(if (isChecked) "лимонад" else null)
        }
        
        chipSweet.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTasteFilter(if (isChecked) "сладкий" else null)
        }
        
        chipBitter.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setTasteFilter(if (isChecked) "горький" else null)
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.drinks.collect { drinks ->
                drinkAdapter.submitList(drinks)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        viewModel.loadDrinks()
    }
}

