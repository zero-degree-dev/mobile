package com.example.zero_degree.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.zero_degree.R
import com.example.zero_degree.ui.adapters.BarAdapter
import com.example.zero_degree.ui.viewmodel.BarsMapViewModel

class BarsMapFragment : Fragment() {
    
    private lateinit var viewModel: BarsMapViewModel
    private lateinit var rvBars: RecyclerView
    private lateinit var progressBar: View
    private lateinit var cardSelectedBar: View
    
    private val barAdapter = BarAdapter { bar ->
        viewModel.selectBar(bar)
        // Можно добавить навигацию к детальной странице бара
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bars_map, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[BarsMapViewModel::class.java]
        
        rvBars = view.findViewById(R.id.rvBars)
        progressBar = view.findViewById(R.id.progressBar)
        cardSelectedBar = view.findViewById(R.id.cardSelectedBar)
        
        // Настройка RecyclerView
        rvBars.layoutManager = LinearLayoutManager(context)
        rvBars.adapter = barAdapter
        
        // Кнопка "Подробнее"
        view.findViewById<View>(R.id.btnBarDetails).setOnClickListener {
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
                view.findViewById<android.widget.TextView>(R.id.tvBarName).text = it.name
                view.findViewById<android.widget.TextView>(R.id.tvBarAddress).text = it.address
                view.findViewById<android.widget.TextView>(R.id.tvBarCapacity).text = "Вместимость: ${it.capacity} человек"
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

