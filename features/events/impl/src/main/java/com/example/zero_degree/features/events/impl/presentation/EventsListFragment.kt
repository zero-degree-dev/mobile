package com.example.zero_degree.features.events.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.core.ui.adapters.EventAdapter
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.features.events.impl.R
import kotlinx.coroutines.launch

class EventsListFragment : ProtectedFragment() {
    
    private val viewModel by viewModels<EventsViewModel> {
        EventsViewModelFactory(requireActivity().application)
    }
    
    private lateinit var rvEvents: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    
    private val eventAdapter = EventAdapter(
        onItemClick = { event ->
            val fragment = EventDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("eventId", event.id)
                }
            }
            NavigationHelper.replaceFragment(activity, fragment)
        }
    )
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.events_screen, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        rvEvents = view.findViewById(R.id.rvEvents)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        
        rvEvents.layoutManager = LinearLayoutManager(requireContext())
        rvEvents.adapter = eventAdapter
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect { events ->
                eventAdapter.submitList(events)
                tvEmpty.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        viewModel.loadEvents()
    }
}

