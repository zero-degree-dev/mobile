package com.example.zero_degree.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.zero_degree.R
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    
    private val viewModel by viewModels<ProfileViewModel>()
    
    private lateinit var usernameText: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnMyBookings: Button
    private lateinit var btnSettings: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.profile_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Инициализация views
        usernameText = view.findViewById(R.id.usernameText)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnMyBookings = view.findViewById(R.id.btnMyBookings)
        btnSettings = view.findViewById(R.id.btnSettings)
        btnLogout = view.findViewById(R.id.btnLogout)
        
        // Подписка на данные пользователя
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.username.collect { name ->
                usernameText.text = name
            }
        }
        
        // Обработчики кнопок
        btnEditProfile.setOnClickListener {
            // TODO: открыть экран редактирования профиля
        }
        
        btnMyBookings.setOnClickListener {
            // TODO: открыть список бронирований
        }
        
        btnSettings.setOnClickListener {
            // TODO: открыть настройки
        }
        
        btnLogout.setOnClickListener {
            // TODO: выход из аккаунта
        }
        
        // Загружаем данные пользователя
        viewModel.loadUser()
    }
}