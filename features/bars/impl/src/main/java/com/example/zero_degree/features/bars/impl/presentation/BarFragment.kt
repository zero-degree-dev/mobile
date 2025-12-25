package com.example.zero_degree.features.bars.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.zero_degree.core.api.model.OpenHours
import com.example.zero_degree.core.api.model.TimeRange
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.core.ui.adapters.ReviewAdapter
import com.example.zero_degree.features.bars.impl.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.util.Log
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.zero_degree.core.ui.R as CoreUiR
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BarFragment: ProtectedFragment() {
    
    companion object {
        private const val TAG = "BarFragment"
    }
    
    private val viewModel by viewModels<BarViewModel> {
        BarViewModelFactory(requireActivity().application)
    }
    
    private lateinit var ivBar: ImageView
    private lateinit var tvBarName: TextView
    private lateinit var tvBarAddress: TextView
    private lateinit var tvBarStatus: TextView
    private lateinit var tvBarWorkingHours: TextView
    private lateinit var tvBarDescription: TextView
    private lateinit var tvBarOpenHours: TextView
    private lateinit var btnBookTable: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var rvReviews: RecyclerView
    private var mapView: MapView? = null
    private var mapObjectCollection: MapObjectCollection? = null
    
    private val reviewAdapter = ReviewAdapter()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bar_screen, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        ivBar = view.findViewById(R.id.ivBar)
        tvBarName = view.findViewById(R.id.tvBarName)
        tvBarAddress = view.findViewById(R.id.tvBarAddress)
        tvBarStatus = view.findViewById(R.id.tvBarStatus)
        tvBarWorkingHours = view.findViewById(R.id.tvBarWorkingHours)
        tvBarDescription = view.findViewById(R.id.tvBarDescription)
        tvBarOpenHours = view.findViewById(R.id.tvBarOpenHours)
        btnBookTable = view.findViewById(R.id.btnBookTable)
        progressBar = view.findViewById(R.id.progressBar)
        rvReviews = view.findViewById(R.id.rvReviews)
        val btnAddReview = view.findViewById<MaterialButton>(R.id.btnAddReview)
        
        rvReviews.layoutManager = LinearLayoutManager(requireContext())
        rvReviews.adapter = reviewAdapter
        
        val barId = arguments?.getString("barId") ?: ""
        
        btnAddReview?.setOnClickListener {
            showAddReviewDialog(barId)
        }
        
        // Инициализируем карту - делаем это после того, как view полностью создан
        try {
            mapView = view.findViewById(R.id.mapView)
            Log.d(TAG, "mapView найден: ${mapView != null}")

            mapView?.let { mv ->
                try {
                    // Запрещаем скролл карты
                    mv.isClickable = false
                    mv.isFocusable = false
                    mv.isFocusableInTouchMode = false
                    
                    mapObjectCollection = mv.map.mapObjects.addCollection()
                    Log.d(TAG, "mapObjectCollection создан для карты бара")
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка при создании mapObjectCollection", e)
                    e.printStackTrace()
                }
            } ?: Log.w(TAG, "mapView не найден в layout")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при поиске mapView", e)
            e.printStackTrace()
        }
        
        btnBookTable.setOnClickListener {
            try {
                val bookingFragmentClass = Class.forName("com.example.zero_degree.features.bookings.impl.presentation.BookingFragment")
                val fragment = bookingFragmentClass.getDeclaredConstructor().newInstance() as Fragment
                NavigationHelper.replaceFragment(activity, fragment)
            } catch (e: Exception) {
                // Fallback if BookingFragment is not available
                e.printStackTrace()
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bar.collect { bar ->
                bar?.let {
                    tvBarName.text = it.name
                    tvBarAddress.text = it.address ?: "Адрес не указан"
                    tvBarDescription.text = it.description ?: "Описание отсутствует"
                    
                    // Отображаем статус и время работы
                    val (isOpen, currentHours) = getBarStatus(it.openHours)
                    if (isOpen) {
                        tvBarStatus.text = "Открыто"
                        tvBarStatus.setTextColor(ContextCompat.getColor(requireContext(), com.example.zero_degree.core.ui.R.color.success))
                        val cardView = tvBarStatus.parent as? com.google.android.material.card.MaterialCardView
                        cardView?.setCardBackgroundColor(ContextCompat.getColor(requireContext(), com.example.zero_degree.core.ui.R.color.success_container))
                    } else {
                        tvBarStatus.text = "Закрыто"
                        tvBarStatus.setTextColor(ContextCompat.getColor(requireContext(), com.example.zero_degree.core.ui.R.color.error))
                        val cardView = tvBarStatus.parent as? com.google.android.material.card.MaterialCardView
                        cardView?.setCardBackgroundColor(ContextCompat.getColor(requireContext(), com.example.zero_degree.core.ui.R.color.error_container))
                    }
                    tvBarWorkingHours.text = currentHours
                    
                    // Отображаем полное время работы
                    tvBarOpenHours.text = formatOpenHours(it.openHours)
                    
                    it.imageUrl?.let { url ->
                        ivBar.load(url)
                    } ?: run {
                        ivBar.setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                    
                    // Обновляем карту с маркером бара
                    updateMapWithBar(it)
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviews.collect { reviews ->
                reviewAdapter.submitList(reviews)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviewCreated.collect { created ->
                if (created) {
                    Toast.makeText(requireContext(), "Отзыв добавлен", Toast.LENGTH_SHORT).show()
                    viewModel.resetReviewCreated()
                }
            }
        }
        
        if (barId.isNotEmpty()) {
            viewModel.loadBar(barId)
            viewModel.loadReviews(barId)
        } else {
            viewModel.loadFirstBar()
        }
    }
    
    private fun showAddReviewDialog(barId: String) {
        val dialogView = layoutInflater.inflate(CoreUiR.layout.dialog_add_review, null)
        val ratingBar = dialogView.findViewById<RatingBar>(CoreUiR.id.ratingBar)
        val commentEditText = dialogView.findViewById<TextInputEditText>(CoreUiR.id.etComment)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Добавить отзыв")
            .setView(dialogView)
            .setPositiveButton("Отправить", null)
            .setNegativeButton("Отмена", null)
            .create()
        
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val rating = ratingBar.rating.toInt()
                val comment = commentEditText.text?.toString()?.trim() ?: ""
                
                if (comment.isEmpty()) {
                    Toast.makeText(requireContext(), "Введите комментарий", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                if (rating < 1 || rating > 5) {
                    Toast.makeText(requireContext(), "Выберите рейтинг", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                viewModel.createReview(barId, rating, comment)
                dialog.dismiss()
            }
        }
        
        dialog.show()
    }
    
    private fun updateMapWithBar(bar: com.example.zero_degree.core.api.model.Bar) {
        mapView?.let { mv ->
            try {
                mapObjectCollection?.clear()
                
                val point = Point(bar.latitude, bar.longitude)
                val placemark = mapObjectCollection?.addPlacemark(point)
                
                if (placemark != null) {
                    // Создаем маркер
                    val markerIcon = createMarkerIcon()
                    placemark.setIcon(ImageProvider.fromBitmap(markerIcon))
                    placemark.setIconStyle(
                        com.yandex.mapkit.map.IconStyle().apply {
                            scale = 1.2f
                        }
                    )
                    
                    // Центрируем карту на баре
                    mv.map.move(
                        CameraPosition(point, 16.0f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 0f),
                        null
                    )
                    
                    Log.d(TAG, "Карта обновлена для бара: ${bar.name}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при обновлении карты", e)
            }
        } ?: Log.w(TAG, "mapView не инициализирован, пропускаем обновление карты")
    }
    
    private fun createMarkerIcon(): Bitmap {
        val size = 50
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        val paint = Paint().apply {
            color = Color.parseColor("#FFB300")
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4, paint)
        
        paint.color = Color.WHITE
        canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)
        
        paint.color = Color.parseColor("#FFB300")
        canvas.drawCircle(size / 2f, size / 2f, size / 6f, paint)
        
        return bitmap
    }
    
    override fun onStart() {
        super.onStart()
        try {
            MapKitFactory.getInstance().onStart()
            mapView?.onStart()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в onStart", e)
        }
    }
    
    override fun onStop() {
        try {
            mapView?.onStop()
            MapKitFactory.getInstance().onStop()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в onStop", e)
        }
        super.onStop()
    }
    
    /**
     * Определяет, открыт ли бар сейчас, и возвращает текущее время работы
     */
    private fun getBarStatus(openHours: OpenHours?): Pair<Boolean, String> {
        if (openHours == null) {
            return Pair(false, "Время работы не указано")
        }
        
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
        
        val dayRanges = when (currentDayOfWeek) {
            Calendar.MONDAY -> openHours.monday
            Calendar.TUESDAY -> openHours.tuesday
            Calendar.WEDNESDAY -> openHours.wednesday
            Calendar.THURSDAY -> openHours.thursday
            Calendar.FRIDAY -> openHours.friday
            Calendar.SATURDAY -> openHours.saturday
            Calendar.SUNDAY -> openHours.sunday
            else -> null
        }
        
        if (dayRanges.isNullOrEmpty()) {
            return Pair(false, "Сегодня закрыто")
        }
        
        // Проверяем, попадает ли текущее время в один из диапазонов
        val isOpen = dayRanges.any { range ->
            currentTime >= range.from && currentTime <= range.to
        }
        
        // Формируем строку с текущим временем работы
        val hoursText = if (dayRanges.size == 1) {
            "${dayRanges[0].from} - ${dayRanges[0].to}"
        } else {
            dayRanges.joinToString(", ") { "${it.from} - ${it.to}" }
        }
        
        return Pair(isOpen, hoursText)
    }
    
    /**
     * Форматирует полное время работы бара
     */
    private fun formatOpenHours(openHours: OpenHours?): String {
        if (openHours == null) {
            return "Время работы не указано"
        }
        
        val days = listOf(
            "Понедельник" to openHours.monday,
            "Вторник" to openHours.tuesday,
            "Среда" to openHours.wednesday,
            "Четверг" to openHours.thursday,
            "Пятница" to openHours.friday,
            "Суббота" to openHours.saturday,
            "Воскресенье" to openHours.sunday
        )
        
        val formattedDays = days.mapNotNull { (dayName, ranges) ->
            if (ranges.isNullOrEmpty()) {
                null
            } else {
                val hoursText = ranges.joinToString(", ") { "${it.from} - ${it.to}" }
                "$dayName: $hoursText"
            }
        }
        
        return if (formattedDays.isEmpty()) {
            "Время работы не указано"
        } else {
            formattedDays.joinToString("\n")
        }
    }
}

