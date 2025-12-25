package com.example.zero_degree

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.zero_degree.R
import com.example.zero_degree.core.api.TokenManager
import com.example.zero_degree.features.auth.impl.presentation.LoginFragment
import com.example.zero_degree.features.home.impl.presentation.HomeFragment
import android.util.Log
import com.yandex.mapkit.MapKitFactory

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
        private const val YANDEX_MAPKIT_API_KEY = "b9d3bf95-e208-4eb6-8090-212a45cd8e3a"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Инициализируем Yandex MapKit с API ключом
        Log.d(TAG, "Инициализация MapKit...")
        Log.d(TAG, "API ключ: ${YANDEX_MAPKIT_API_KEY.take(10)}...${YANDEX_MAPKIT_API_KEY.takeLast(5)}")
        
        try {
            // ВАЖНО: setApiKey должен быть вызван ДО initialize
            Log.d(TAG, "Устанавливаем API ключ: ${YANDEX_MAPKIT_API_KEY.take(10)}...${YANDEX_MAPKIT_API_KEY.takeLast(5)}")
            MapKitFactory.setApiKey(YANDEX_MAPKIT_API_KEY)
            Log.d(TAG, "API ключ установлен успешно")
            
            Log.d(TAG, "Инициализируем MapKit...")
            MapKitFactory.initialize(this)
            Log.d(TAG, "MapKit инициализирован успешно")
            
            // Проверяем, что MapKit действительно инициализирован
            val mapKit = MapKitFactory.getInstance()
            Log.d(TAG, "MapKit instance получен: ${mapKit != null}")
            
            // Проверяем, что ключ действительно установлен
            // К сожалению, нет публичного метода для проверки, но можем проверить через логи
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при инициализации MapKit", e)
            e.printStackTrace()
        }
        
        // Включаем edge-to-edge отображение (контент под системными панелями)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContentView(R.layout.activity_main)
        val fragmentContainer = findViewById<android.view.View>(R.id.fragment_container)
        ViewCompat.setOnApplyWindowInsetsListener(fragmentContainer) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                top = systemBars.top,
                bottom = systemBars.bottom,
                left = systemBars.left,
                right = systemBars.right
            )
            insets
        }
        
        if (savedInstanceState == null) {
            // Проверяем авторизацию и показываем соответствующий фрагмент
            if (TokenManager.isLoggedIn(applicationContext)) {
                replaceFragment(HomeFragment())
            } else {
                replaceFragment(LoginFragment())
            }
        }
    }
    
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}