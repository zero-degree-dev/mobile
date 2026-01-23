package com.example.zero_degree.features.bars.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.zero_degree.core.api.model.Bar
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.features.bars.impl.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.util.Log
import com.yandex.mapkit.mapview.MapView
import com.example.zero_degree.core.ui.NavigationHelper
import kotlinx.coroutines.launch

class BarsMapFragment : ProtectedFragment() {
    
    companion object {
        private const val TAG = "BarsMapFragment"
    }
    
    private val viewModel by viewModels<BarsMapViewModel> {
        BarViewModelFactory(requireActivity().application)
    }
    
    private lateinit var mapView: MapView
    private lateinit var progressBar: ProgressBar
    private lateinit var cardSelectedBar: View
    private lateinit var tvBarName: TextView
    private lateinit var tvBarAddress: TextView
    private lateinit var tvBarCapacity: TextView
    private lateinit var btnBarDetails: MaterialButton
    
    private var mapObjectCollection: MapObjectCollection? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView вызван")
        // MapKit уже инициализирован в MainActivity
        val view = inflater.inflate(R.layout.fragment_bars_map, container, false)
        Log.d(TAG, "Layout загружен, view: ${view != null}")
        return view
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreatedProtected вызван")
        
        mapView = view.findViewById(R.id.mapView)
        Log.d(TAG, "mapView найден: ${mapView != null}")
        
        progressBar = view.findViewById(R.id.progressBar)
        cardSelectedBar = view.findViewById(R.id.cardSelectedBar)
        tvBarName = view.findViewById(R.id.tvBarName)
        tvBarAddress = view.findViewById(R.id.tvBarAddress)
        tvBarCapacity = view.findViewById(R.id.tvBarCapacity)
        btnBarDetails = view.findViewById(R.id.btnBarDetails)
        
        try {
            mapObjectCollection = mapView.map.mapObjects.addCollection()
            Log.d(TAG, "mapObjectCollection создан успешно")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при создании mapObjectCollection", e)
        }
        
        // Настройка карты - центрируем на Сочи (координаты из API)
        try {
            Log.d(TAG, "Устанавливаем позицию камеры на Сочи (43.4, 39.9)")
            mapView.map.move(
                CameraPosition(Point(43.4, 39.9), 12.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null
            )
            Log.d(TAG, "Позиция камеры установлена")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при установке позиции камеры", e)
        }
        
        btnBarDetails.setOnClickListener {
            val bar = viewModel.selectedBar.value
            bar?.let {
                val fragment = BarFragment().apply {
                    arguments = Bundle().apply {
                        putString("barId", it.id)
                    }
                }
                NavigationHelper.replaceFragment(activity, fragment)
            }
        }
        
        // Наблюдаем за данными
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bars.collect { bars ->
                updateMapMarkers(bars)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedBar.collect { bar ->
                bar?.let {
                    cardSelectedBar.visibility = View.VISIBLE
                    tvBarName.text = it.name
                    tvBarAddress.text = it.address
                           tvBarCapacity.text = "" // capacity больше не используется
                    
                    // Переместить камеру к выбранному бару
                    mapView.map.move(
                        CameraPosition(Point(it.latitude, it.longitude), 15.0f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 0.5f),
                        null
                    )
                } ?: run {
                    cardSelectedBar.visibility = View.GONE
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        // Загружаем данные
        viewModel.loadBars()
    }
    
    private fun updateMapMarkers(bars: List<Bar>) {
        Log.d(TAG, "updateMapMarkers вызван, количество баров: ${bars.size}")
        
        try {
            mapObjectCollection?.clear()
            Log.d(TAG, "mapObjectCollection очищен")
            
            bars.forEach { bar ->
                try {
                    Log.d(TAG, "Добавляем маркер для бара: ${bar.name}, координаты: (${bar.latitude}, ${bar.longitude})")
                    val point = Point(bar.latitude, bar.longitude)
                    val placemark = mapObjectCollection?.addPlacemark(point)
                    
                    if (placemark != null) {
                        // Создаем большую жирную точку
                        val markerIcon = createMarkerIcon()
                        placemark.setIcon(ImageProvider.fromBitmap(markerIcon))
                        placemark.setIconStyle(
                            com.yandex.mapkit.map.IconStyle().apply {
                                scale = 1.5f // Увеличиваем размер
                            }
                        )
                        
                        Log.d(TAG, "Маркер добавлен успешно для ${bar.name}")
                        placemark.addTapListener { _, _ ->
                            Log.d(TAG, "Маркер нажат для бара: ${bar.name}")
                            // Переходим на страницу бара
                            val fragment = BarFragment().apply {
                                arguments = Bundle().apply {
                                    putString("barId", bar.id)
                                }
                            }
                            NavigationHelper.replaceFragment(activity, fragment)
                            true
                        }
                    } else {
                        Log.w(TAG, "Не удалось добавить маркер для ${bar.name}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка при добавлении маркера для ${bar.name}", e)
                }
            }
            
            Log.d(TAG, "Все маркеры обработаны")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в updateMapMarkers", e)
        }
    }
    
    private fun createMarkerIcon(): Bitmap {
        val size = 60 // Размер маркера в пикселях
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Рисуем большой жирный круг
        val paint = Paint().apply {
            color = Color.parseColor("#FFB300") // Янтарный цвет
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        
        // Внешний круг (большой)
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4, paint)
        
        // Внутренний белый круг
        paint.color = Color.WHITE
        canvas.drawCircle(size / 2f, size / 2f, size / 3f, paint)
        
        // Центральная точка
        paint.color = Color.parseColor("#FFB300")
        canvas.drawCircle(size / 2f, size / 2f, size / 6f, paint)
        
        return bitmap
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart вызван")
        try {
            MapKitFactory.getInstance().onStart()
            Log.d(TAG, "MapKitFactory.onStart() вызван")
            mapView.onStart()
            Log.d(TAG, "mapView.onStart() вызван")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в onStart", e)
        }
    }
    
    override fun onStop() {
        Log.d(TAG, "onStop вызван")
        try {
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
            super.onStop()
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в onStop", e)
            super.onStop()
        }
    }
}

