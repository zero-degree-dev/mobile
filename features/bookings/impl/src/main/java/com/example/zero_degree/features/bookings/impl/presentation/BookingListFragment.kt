package com.example.zero_degree.features.bookings.impl.presentation

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
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.core.ui.adapters.BookingAdapter
import com.example.zero_degree.features.bookings.impl.R
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class BookingListFragment : ProtectedFragment() {
    
    private val viewModel by viewModels<BookingListViewModel> {
        BookingListViewModelFactory(requireActivity().application)
    }
    
    private lateinit var tabLayout: TabLayout
    private lateinit var rvBookings: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    
    private val bookingAdapter = BookingAdapter(
        onEditClick = { booking ->
            val fragment = BookingFragment().apply {
                arguments = Bundle().apply {
                    putString("bookingId", booking.id)
                }
            }
            NavigationHelper.replaceFragment(activity, fragment)
        },
        onCancelClick = { booking ->
            viewModel.cancelBooking(booking.id)
        }
    )
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_booking_list, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        tabLayout = view.findViewById(R.id.tabLayout)
        rvBookings = view.findViewById(R.id.rvBookings)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        
        rvBookings.layoutManager = LinearLayoutManager(requireContext())
        rvBookings.adapter = bookingAdapter
        
        tabLayout.addTab(tabLayout.newTab().setText("Текущие"))
        tabLayout.addTab(tabLayout.newTab().setText("История"))
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.showCurrentBookings()
                    1 -> viewModel.showHistoryBookings()
                }
            }
            
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookings.collect { bookings ->
                bookingAdapter.submitList(bookings)
                tvEmpty.visibility = if (bookings.isEmpty()) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        viewModel.loadBookings()
    }
}

