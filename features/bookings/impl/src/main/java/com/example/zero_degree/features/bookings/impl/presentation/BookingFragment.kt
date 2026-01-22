package com.example.zero_degree.features.bookings.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.core.api.TokenManager
import com.example.zero_degree.core.ui.adapters.BarSelectionAdapter
import com.example.zero_degree.features.bookings.impl.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class BookingFragment : Fragment() {
    
    private val viewModel by viewModels<BookingViewModel> {
        BookingViewModelFactory(requireActivity().application)
    }
    
    private lateinit var rvBars: RecyclerView
    private lateinit var progressBar: View
    private lateinit var etDate: TextInputEditText
    private lateinit var etTime: TextInputEditText
    private lateinit var tvGuestsCount: TextView
    private lateinit var btnBook: Button
    
    private val barAdapter = BarSelectionAdapter { bar ->
        viewModel.setSelectedBar(bar)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        rvBars = view.findViewById(R.id.rvBars)
        progressBar = view.findViewById(R.id.progressBar)
        etDate = view.findViewById(R.id.etDate)
        etTime = view.findViewById(R.id.etTime)
        tvGuestsCount = view.findViewById(R.id.tvGuestsCount)
        btnBook = view.findViewById(R.id.btnBook)
        
        rvBars.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvBars.adapter = barAdapter
        
        // Проверяем, редактируем ли существующее бронирование
        val bookingId = arguments?.getString("bookingId")
        val barId = arguments?.getString("barId")
        val date = arguments?.getString("date")
        val time = arguments?.getString("time")
        val guestsCount = arguments?.getInt("guestsCount", 1) ?: 1
        
        if (bookingId != null && barId != null && date != null && time != null) {
            // Режим редактирования - заполняем поля
            viewModel.setEditingBookingId(bookingId)
            etDate.setText(date)
            etTime.setText(time)
            viewModel.setDate(date)
            viewModel.setTime(time)
            viewModel.setGuestsCount(guestsCount)
            
            // Устанавливаем заголовок кнопки
            btnBook.text = "Сохранить изменения"
        }
        
        view.findViewById<View>(R.id.btnDecreaseGuests).setOnClickListener {
            val current = viewModel.guestsCount.value
            if (current > 1) {
                viewModel.setGuestsCount(current - 1)
            }
        }
        
        view.findViewById<View>(R.id.btnIncreaseGuests).setOnClickListener {
            val current = viewModel.guestsCount.value
            viewModel.setGuestsCount(current + 1)
        }
        
        // Обновляем значения при изменении текста
        etDate.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val dateText = etDate.text?.toString()?.trim() ?: ""
                if (dateText.isNotEmpty()) {
                    viewModel.setDate(dateText)
                }
            }
        }
        
        etTime.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val timeText = etTime.text?.toString()?.trim() ?: ""
                if (timeText.isNotEmpty()) {
                    viewModel.setTime(timeText)
                }
            }
        }
        
        btnBook.setOnClickListener {
            // Берем значения напрямую из полей ввода
            val dateText = etDate.text?.toString()?.trim() ?: ""
            val timeText = etTime.text?.toString()?.trim() ?: ""
            val selectedBar = viewModel.selectedBar.value
            
            // Обновляем значения в ViewModel
            if (dateText.isNotEmpty()) {
                viewModel.setDate(dateText)
            }
            if (timeText.isNotEmpty()) {
                viewModel.setTime(timeText)
            }
            
            if (selectedBar == null) {
                Toast.makeText(requireContext(), "Выберите бар", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (dateText.isEmpty()) {
                Toast.makeText(requireContext(), "Укажите дату", Toast.LENGTH_SHORT).show()
                etDate.requestFocus()
                return@setOnClickListener
            }
            
            if (timeText.isEmpty()) {
                Toast.makeText(requireContext(), "Укажите время", Toast.LENGTH_SHORT).show()
                etTime.requestFocus()
                return@setOnClickListener
            }
            
            // Создаем бронирование
            viewModel.createBooking()
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bars.collect { bars ->
                barAdapter.submitList(bars)
                
                // Если редактируем, выбираем бар по barId
                val barId = arguments?.getString("barId")
                if (barId != null && viewModel.selectedBar.value == null) {
                    bars.find { it.id == barId }?.let { bar ->
                        viewModel.setSelectedBar(bar)
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedBar.collect { bar ->
                barAdapter.setSelectedBar(bar)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedDate.collect { date ->
                if (etDate.text.toString() != date) {
                    etDate.setText(date)
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedTime.collect { time ->
                if (etTime.text.toString() != time) {
                    etTime.setText(time)
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.guestsCount.collect { count ->
                tvGuestsCount.text = count.toString()
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                btnBook.isEnabled = !isLoading
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingSuccess.collect { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Бронирование создано!", Toast.LENGTH_SHORT).show()
                    viewModel.resetBookingSuccess()
                }
            }
        }
        
        viewModel.loadBars()
    }
}

