package com.example.zero_degree.features.events.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil3.load
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.features.events.impl.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class EventDetailFragment : ProtectedFragment() {
    
    private val viewModel by viewModels<EventDetailViewModel> {
        EventDetailViewModelFactory(requireActivity().application)
    }
    
    private lateinit var ivEvent: ImageView
    private lateinit var tvEventName: TextView
    private lateinit var tvEventDate: TextView
    private lateinit var tvEventDescription: TextView
    private lateinit var progressBar: ProgressBar
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        ivEvent = view.findViewById(R.id.ivEvent)
        tvEventName = view.findViewById(R.id.tvEventName)
        tvEventDate = view.findViewById(R.id.tvEventDate)
        tvEventDescription = view.findViewById(R.id.tvEventDescription)
        progressBar = view.findViewById(R.id.progressBar)
        
        val eventId = arguments?.getString("eventId") ?: ""
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect { event ->
                event?.let {
                    tvEventName.text = it.name
                    tvEventDate.text = formatDate(it.date)
                    tvEventDescription.text = it.description ?: "Описание отсутствует"
                    
                    it.imageUrl?.let { url ->
                        ivEvent.load(url)
                    } ?: run {
                        ivEvent.setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                }
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
                    // Можно показать Toast или Snackbar
                }
            }
        }
        
        if (eventId.isNotEmpty()) {
            viewModel.loadEvent(eventId)
        }
    }
    
    private fun formatDate(dateString: String): String {
        return try {
            // Пытаемся распарсить как timestamp (миллисекунды или секунды)
            val timestamp = dateString.toLongOrNull()
            if (timestamp != null) {
                // Если число меньше 10^10, считаем что это секунды, иначе миллисекунды
                val timestampMs = if (timestamp < 1_000_000_000_000L) {
                    timestamp * 1000
                } else {
                    timestamp
                }
                val date = java.util.Date(timestampMs)
                val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                format.format(date)
            } else {
                // Если не timestamp, пытаемся распарсить как ISO 8601
                if (dateString.contains("T")) {
                    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val date = isoFormat.parse(dateString)
                    if (date != null) {
                        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                        format.format(date)
                    } else {
                        dateString.split("T")[0]
                    }
                } else {
                    dateString
                }
            }
        } catch (e: Exception) {
            dateString
        }
    }
}

