package com.example.zero_degree.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_degree.R
import com.example.zero_degree.ui.adapters.BarSelectionAdapter
import com.example.zero_degree.ui.viewmodel.BookingViewModel

class BookingFragment : Fragment() {
    
    private lateinit var viewModel: BookingViewModel
    private lateinit var rvBars: RecyclerView
    private lateinit var progressBar: View
    private lateinit var etDate: com.google.android.material.textfield.TextInputEditText
    private lateinit var etTime: com.google.android.material.textfield.TextInputEditText
    private lateinit var tvGuestsCount: android.widget.TextView
    private lateinit var btnBook: View
    
    private val barAdapter = BarSelectionAdapter { bar ->
        viewModel.setSelectedBar(bar)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[BookingViewModel::class.java]
        
        rvBars = view.findViewById(R.id.rvBars)
        progressBar = view.findViewById(R.id.progressBar)
        etDate = view.findViewById(R.id.etDate)
        etTime = view.findViewById(R.id.etTime)
        tvGuestsCount = view.findViewById(R.id.tvGuestsCount)
        btnBook = view.findViewById(R.id.btnBook)
        
        // Настройка RecyclerView
        rvBars.layoutManager = LinearLayoutManager(requireContext())
        rvBars.adapter = barAdapter
        
        // Кнопки для количества гостей
        view.findViewById<View>(R.id.btnDecreaseGuests).setOnClickListener {
            val current = viewModel.guestsCount.value ?: 1
            if (current > 1) {
                viewModel.setGuestsCount(current - 1)
            }
        }
        
        view.findViewById<View>(R.id.btnIncreaseGuests).setOnClickListener {
            val current = viewModel.guestsCount.value ?: 1
            viewModel.setGuestsCount(current + 1)
        }
        
        // Поля ввода
        etDate.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.setDate(etDate.text.toString())
            }
        }
        
        etTime.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.setTime(etTime.text.toString())
            }
        }
        
        // Кнопка бронирования
        btnBook.setOnClickListener {
            viewModel.createBooking(1) // TODO: получить реальный userId
        }
        
        // Наблюдаем за данными
        viewModel.bars.observe(viewLifecycleOwner) { bars ->
            barAdapter.submitList(bars)
        }
        
        viewModel.selectedBar.observe(viewLifecycleOwner) { bar ->
            barAdapter.setSelectedBar(bar)
        }
        
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            if (etDate.text.toString() != date) {
                etDate.setText(date)
            }
        }
        
        viewModel.selectedTime.observe(viewLifecycleOwner) { time ->
            if (etTime.text.toString() != time) {
                etTime.setText(time)
            }
        }
        
        viewModel.guestsCount.observe(viewLifecycleOwner) { count ->
            tvGuestsCount.text = count.toString()
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnBook.isEnabled = !isLoading
        }
        
        viewModel.bookingSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Бронирование создано!", Toast.LENGTH_SHORT).show()
                viewModel.resetBookingSuccess()
            }
        }
        
        // Загружаем данные
        viewModel.loadBars()
    }
}



