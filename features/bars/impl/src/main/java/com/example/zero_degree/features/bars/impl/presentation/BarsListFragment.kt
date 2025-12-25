package com.example.zero_degree.features.bars.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.core.ui.adapters.BarAdapter
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.features.bars.impl.R
import kotlinx.coroutines.launch

class BarsListFragment : ProtectedFragment() {
    
    companion object {
        private const val TAG = "BarsListFragment"
    }
    
    private val viewModel by viewModels<BarsMapViewModel> {
        BarViewModelFactory(requireActivity().application)
    }
    
    private lateinit var rvBars: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    
    private val barAdapter = BarAdapter { bar ->
        val fragment = BarFragment().apply {
            arguments = Bundle().apply {
                putString("barId", bar.id)
            }
        }
        NavigationHelper.replaceFragment(activity, fragment)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bars_list, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreatedProtected вызван")
        
        rvBars = view.findViewById(R.id.rvBars)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        
        rvBars.layoutManager = LinearLayoutManager(requireContext())
        rvBars.adapter = barAdapter
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bars.collect { bars ->
                Log.d(TAG, "Бары получены, количество: ${bars.size}")
                barAdapter.submitList(bars)
                tvEmpty.visibility = if (bars.isEmpty()) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        Log.d(TAG, "Загружаем бары...")
        viewModel.loadBars()
    }
}

