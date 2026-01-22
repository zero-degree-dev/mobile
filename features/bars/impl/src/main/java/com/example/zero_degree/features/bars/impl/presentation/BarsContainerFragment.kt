package com.example.zero_degree.features.bars.impl.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.commit
import com.google.android.material.button.MaterialButton
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.features.bars.impl.R

class BarsContainerFragment : ProtectedFragment() {
    
    companion object {
        private const val TAG = "BarsContainerFragment"
        private const val KEY_CURRENT_VIEW = "current_view"
    }
    
    private var currentView: ViewType? = null
    
    private lateinit var btnShowList: MaterialButton
    private lateinit var btnShowMap: MaterialButton
    private lateinit var progressBar: ProgressBar
    
    enum class ViewType {
        LIST, MAP
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bars_container, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreatedProtected вызван")
        
        btnShowList = view.findViewById(R.id.btnShowList)
        btnShowMap = view.findViewById(R.id.btnShowMap)
        progressBar = view.findViewById(R.id.progressBar)
        
        // Восстанавливаем состояние
        savedInstanceState?.let {
            val savedView = it.getString(KEY_CURRENT_VIEW, ViewType.LIST.name)
            currentView = ViewType.valueOf(savedView)
            Log.d(TAG, "Восстановлено состояние: $currentView")
        }
        
        btnShowList.setOnClickListener {
            Log.d(TAG, "Кнопка 'Список' нажата")
            switchToView(ViewType.LIST)
        }
        
        btnShowMap.setOnClickListener {
            Log.d(TAG, "Кнопка 'Карта' нажата")
            switchToView(ViewType.MAP)
        }
        
        // Показываем начальный фрагмент
        val initialView = currentView ?: ViewType.LIST
        Log.d(TAG, "Инициализация с view: $initialView, savedInstanceState == null: ${savedInstanceState == null}")
        
        // Принудительно устанавливаем начальный view для первого запуска
        if (savedInstanceState == null) {
            currentView = null // Сбрасываем, чтобы switchToView точно добавил фрагмент
        }
        
        switchToView(initialView)
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentView?.let {
            outState.putString(KEY_CURRENT_VIEW, it.name)
        }
    }
    
    private fun switchToView(viewType: ViewType) {
        if (currentView == viewType) {
            Log.d(TAG, "Уже на view: ${viewType.name}, пропускаем")
            return
        }
        
        Log.d(TAG, "Переключаемся с ${currentView} на ${viewType.name}")
        val isFirstFragment = currentView == null
        currentView = viewType
        updateButtonStates()
        
        val fragment = when (viewType) {
            ViewType.LIST -> {
                Log.d(TAG, "Создаем BarsListFragment")
                BarsListFragment()
            }
            ViewType.MAP -> {
                Log.d(TAG, "Создаем BarsMapFragment")
                BarsMapFragment()
            }
        }
        
        try {
            childFragmentManager.commit {
                if (isFirstFragment) {
                    // Для первого фрагмента используем add
                    add(R.id.fragmentContainer, fragment)
                    Log.d(TAG, "Первый фрагмент добавлен через add()")
                } else {
                    // Для последующих используем replace
                    replace(R.id.fragmentContainer, fragment)
                    Log.d(TAG, "Фрагмент заменен через replace()")
                }
                setReorderingAllowed(true)
            }
            Log.d(TAG, "Фрагмент ${viewType.name} успешно добавлен")
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при добавлении фрагмента", e)
            e.printStackTrace()
        }
    }
    
    private fun updateButtonStates() {
        when (currentView) {
            ViewType.LIST -> {
                btnShowList.isEnabled = false
                btnShowMap.isEnabled = true
                Log.d(TAG, "Кнопки обновлены: Список disabled, Карта enabled")
            }
            ViewType.MAP -> {
                btnShowList.isEnabled = true
                btnShowMap.isEnabled = false
                Log.d(TAG, "Кнопки обновлены: Список enabled, Карта disabled")
            }
            null -> {
                // Инициализация - обе кнопки активны
                btnShowList.isEnabled = true
                btnShowMap.isEnabled = true
                Log.d(TAG, "Кнопки в начальном состоянии: обе enabled")
            }
        }
    }
}
