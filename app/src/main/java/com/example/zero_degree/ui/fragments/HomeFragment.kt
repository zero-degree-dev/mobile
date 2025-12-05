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
import com.example.zero_degree.data.model.Bar
import com.example.zero_degree.data.model.Event
import com.example.zero_degree.ui.adapters.BarHorizontalAdapter
import com.example.zero_degree.ui.adapters.EventAdapter
import com.example.zero_degree.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var rvRecentBars: RecyclerView
    private lateinit var rvEvents: RecyclerView
    private lateinit var progressBar: View
    
    private val barAdapter = BarHorizontalAdapter { bar ->
        (activity as? com.example.zero_degree.MainActivity)?.replaceFragment(
            BarsMapFragment()
        )
    }
    
    private val eventAdapter = EventAdapter()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        
        rvRecentBars = view.findViewById(R.id.rvRecentBars)
        rvEvents = view.findViewById(R.id.rvEvents)
        progressBar = view.findViewById(R.id.progressBar)
        
        // Настройка RecyclerView для баров
        rvRecentBars.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvRecentBars.adapter = barAdapter
        
        // Настройка RecyclerView для событий
        rvEvents.layoutManager = LinearLayoutManager(requireContext())
        rvEvents.adapter = eventAdapter
        
        // Кнопки быстрых действий
        view.findViewById<View>(R.id.btnBars).setOnClickListener {
            (activity as? com.example.zero_degree.MainActivity)?.replaceFragment(
                BarsMapFragment()
            )
        }
        
        view.findViewById<View>(R.id.btnMenu).setOnClickListener {
            (activity as? com.example.zero_degree.MainActivity)?.replaceFragment(
                DrinkCatalogFragment()
            )
        }
        
        view.findViewById<View>(R.id.btnEvents).setOnClickListener {
            // TODO: навигация к событиям
        }
        
        // Наблюдаем за данными
        viewModel.recentBars.observe(viewLifecycleOwner) { bars ->
            barAdapter.submitList(bars)
        }
        
        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Загружаем данные
        viewModel.loadHomeData()
    }
}

