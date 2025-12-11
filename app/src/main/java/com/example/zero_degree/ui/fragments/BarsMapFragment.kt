package com.example.zero_degree.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.R
import com.example.zero_degree.ui.adapters.BarAdapter
import com.example.zero_degree.ui.viewmodel.BarsMapViewModel

class BarsMapFragment : Fragment() {
    
    private val viewModel by viewModels<BarsMapViewModel>()
    
    private lateinit var rvBars: RecyclerView
    private lateinit var progressBar: View
    private lateinit var cardSelectedBar: View
    private lateinit var tvBarName: TextView
    private lateinit var tvBarAddress: TextView
    private lateinit var tvBarCapacity: TextView
    private lateinit var btnBarDetails: Button
    
    private val barAdapter = BarAdapter { bar ->
        viewModel.selectBar(bar)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bars_map, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Инициализация views
        rvBars = view.findViewById(R.id.rvBars)
        progressBar = view.findViewById(R.id.progressBar)
        cardSelectedBar = view.findViewById(R.id.cardSelectedBar)
        tvBarName = view.findViewById(R.id.tvBarName)
        tvBarAddress = view.findViewById(R.id.tvBarAddress)
        tvBarCapacity = view.findViewById(R.id.tvBarCapacity)
        btnBarDetails = view.findViewById(R.id.btnBarDetails)
        
        // Настройка RecyclerView
        rvBars.layoutManager = LinearLayoutManager(requireContext())
        rvBars.adapter = barAdapter
        
        // Кнопка "Забронировать столик"
        btnBarDetails.setOnClickListener {
            val bar = viewModel.selectedBar.value
            bar?.let {
                val fragment = BookingFragment()
                (activity as? com.example.zero_degree.MainActivity)?.replaceFragment(fragment)
            }
        }
        
        // Наблюдаем за данными
        viewModel.bars.observe(viewLifecycleOwner) { bars ->
            barAdapter.submitList(bars)
        }
        
        viewModel.selectedBar.observe(viewLifecycleOwner) { bar ->
            bar?.let {
                cardSelectedBar.visibility = View.VISIBLE
                tvBarName.text = it.name
                tvBarAddress.text = it.address
                tvBarCapacity.text = "Вместимость: ${it.capacity} человек"
            } ?: run {
                cardSelectedBar.visibility = View.GONE
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Загружаем данные
        viewModel.loadBars()
    }
}

