package com.example.zero_degree.features.home.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.core.ui.ProtectedFragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.zero_degree.core.api.model.Booking
import com.example.zero_degree.core.ui.adapters.BarHorizontalAdapter
import com.example.zero_degree.core.ui.adapters.BookingAdapter
import com.example.zero_degree.core.ui.adapters.DrinkHorizontalAdapter
import com.example.zero_degree.core.ui.adapters.EventHorizontalAdapter
import com.example.zero_degree.features.bars.impl.presentation.BarFragment
import com.example.zero_degree.features.bookings.impl.presentation.BookingFragment
import com.example.zero_degree.features.bars.impl.presentation.BarsContainerFragment
import com.example.zero_degree.features.drinks.impl.presentation.DrinkCatalogFragment
import com.example.zero_degree.features.drinks.impl.presentation.DrinkDetailFragment
import com.example.zero_degree.features.events.impl.presentation.EventDetailFragment
import com.example.zero_degree.features.events.impl.presentation.EventsListFragment
import com.example.zero_degree.features.home.impl.R
import kotlinx.coroutines.launch

class HomeFragment : ProtectedFragment() {
    
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(requireActivity().application)
    }
    
    private lateinit var ivProfileAvatar: ImageView
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileBalance: TextView
    private lateinit var rvBars: RecyclerView
    private lateinit var rvDrinks: RecyclerView
    private lateinit var rvEvents: RecyclerView
    private lateinit var rvBookings: RecyclerView
    private lateinit var progressBar: View
    private lateinit var tvAllBars: TextView
    private lateinit var tvAllDrinks: TextView
    private lateinit var tvAllEvents: TextView
    
    private val barAdapter = BarHorizontalAdapter { bar ->
        val fragment = BarFragment().apply {
            arguments = Bundle().apply {
                putString("barId", bar.id)
            }
        }
        NavigationHelper.replaceFragment(activity, fragment)
    }
    
    private val drinkAdapter = DrinkHorizontalAdapter { drink ->
        val fragment = DrinkDetailFragment().apply {
            arguments = Bundle().apply {
                putString("drinkId", drink.id)
            }
        }
        NavigationHelper.replaceFragment(activity, fragment)
    }
    
    private val eventAdapter = EventHorizontalAdapter { event ->
        val fragment = EventDetailFragment().apply {
            arguments = Bundle().apply {
                putString("eventId", event.id)
            }
        }
        NavigationHelper.replaceFragment(activity, fragment)
    }
    
    private val bookingAdapter = BookingAdapter(
        onEditClick = { booking ->
            // Открываем форму редактирования с заполненными полями
            val fragment = BookingFragment().apply {
                arguments = Bundle().apply {
                    putString("bookingId", booking.id)
                    putString("barId", booking.barId)
                    putString("date", booking.date)
                    putString("time", booking.time.substring(0, 5)) // Убираем секунды
                    putInt("guestsCount", booking.guestsCount)
                }
            }
            NavigationHelper.replaceFragment(activity, fragment)
        },
        onCancelClick = { booking ->
            // Показываем диалог подтверждения отмены
            AlertDialog.Builder(requireContext())
                .setTitle("Отмена бронирования")
                .setMessage("Вы уверены, что хотите отменить бронирование?")
                .setPositiveButton("Отменить бронирование") { _, _ ->
                    viewModel.cancelBooking(booking.id)
                }
                .setNegativeButton("Нет", null)
                .show()
        }
    )
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        try {
            ivProfileAvatar = view.findViewById(R.id.ivProfileAvatar)
            tvProfileName = view.findViewById(R.id.tvProfileName)
            tvProfileBalance = view.findViewById(R.id.tvProfileBalance)
            rvBars = view.findViewById(R.id.rvBars)
            rvDrinks = view.findViewById(R.id.rvDrinks)
            rvEvents = view.findViewById(R.id.rvEvents)
            rvBookings = view.findViewById(R.id.rvBookings)
            progressBar = view.findViewById(R.id.progressBar)
            tvAllBars = view.findViewById(R.id.tvAllBars)
            tvAllDrinks = view.findViewById(R.id.tvAllDrinks)
            tvAllEvents = view.findViewById(R.id.tvAllEvents)
            
            rvBars.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvBars.adapter = barAdapter
            
            rvDrinks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvDrinks.adapter = drinkAdapter
            
            rvEvents.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvEvents.adapter = eventAdapter
            
            rvBookings.layoutManager = LinearLayoutManager(requireContext())
            rvBookings.adapter = bookingAdapter
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        
        tvAllBars.setOnClickListener {
            NavigationHelper.replaceFragment(activity, BarsContainerFragment())
        }
        
        tvAllDrinks.setOnClickListener {
            NavigationHelper.replaceFragment(activity, DrinkCatalogFragment())
        }
        
        tvAllEvents.setOnClickListener {
            NavigationHelper.replaceFragment(activity, EventsListFragment())
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let {
                    tvProfileName.text = it.name
                    tvProfileBalance.text = "${it.bonusBalance} баллов"
                    // Загружаем аватар пользователя, если есть
                    if (!it.avatarUrl.isNullOrBlank()) {
                        try {
                            ivProfileAvatar.load(it.avatarUrl)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            ivProfileAvatar.setImageResource(android.R.drawable.ic_menu_myplaces)
                        }
                    } else {
                        // Если аватара нет, показываем дефолтную иконку
                        ivProfileAvatar.setImageResource(android.R.drawable.ic_menu_myplaces)
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recentBars.collect { bars ->
                barAdapter.submitList(bars)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recentDrinks.collect { drinks ->
                drinkAdapter.submitList(drinks)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.activeEvents.collect { events ->
                eventAdapter.submitList(events)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.upcomingBookings.collect { bookings ->
                bookingAdapter.submitList(bookings)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingCancelled.collect { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Бронирование отменено", Toast.LENGTH_SHORT).show()
                    viewModel.resetBookingCancelled()
                    viewModel.loadHomeData() // Перезагружаем данные
                }
            }
        }
        
        viewModel.loadHomeData()
    }
}
