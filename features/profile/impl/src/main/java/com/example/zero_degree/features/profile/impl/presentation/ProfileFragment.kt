package com.example.zero_degree.features.profile.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil3.load
import com.google.android.material.button.MaterialButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.features.auth.impl.presentation.AuthorizationFragment
import com.example.zero_degree.features.bookings.impl.presentation.BookingListFragment
import com.example.zero_degree.features.profile.impl.R
import kotlinx.coroutines.launch

class ProfileFragment : ProtectedFragment() {
    
    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory(requireActivity().application)
    }
    
    private lateinit var ivProfileAvatar: ImageView
    private lateinit var usernameText: TextView
    private lateinit var btnEditProfile: MaterialButton
    private lateinit var btnMyBookings: MaterialButton
    private lateinit var btnSettings: MaterialButton
    private lateinit var btnLogout: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.profile_screen, container, false)
    }

    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        ivProfileAvatar = view.findViewById(R.id.ivProfileAvatar)
        usernameText = view.findViewById(R.id.usernameText)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnMyBookings = view.findViewById(R.id.btnMyBookings)
        btnSettings = view.findViewById(R.id.btnSettings)
        btnLogout = view.findViewById(R.id.btnLogout)
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.username.collect { name ->
                usernameText.text = name
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.user.collect { user ->
                user?.let {
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
        
        btnEditProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Функция редактирования профиля в разработке", Toast.LENGTH_SHORT).show()
        }
        
        btnMyBookings.setOnClickListener {
            NavigationHelper.replaceFragment(activity, BookingListFragment())
        }
        
        btnSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Настройки в разработке", Toast.LENGTH_SHORT).show()
        }
        
        btnLogout.setOnClickListener {
            showLogoutDialog()
        }
        
        view.findViewById<View>(R.id.btnDeleteAccount)?.setOnClickListener {
            showDeleteAccountDialog()
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.logoutSuccess.collect { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Выход выполнен", Toast.LENGTH_SHORT).show()
                    // Очищаем back stack и переходим на экран авторизации
                    NavigationHelper.replaceFragmentAndClearBackStack(activity, AuthorizationFragment())
                    viewModel.resetLogoutSuccess()
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteSuccess.collect { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Аккаунт удалён", Toast.LENGTH_SHORT).show()
                    // Очищаем back stack и переходим на экран авторизации
                    NavigationHelper.replaceFragmentAndClearBackStack(activity, AuthorizationFragment())
                    viewModel.resetDeleteSuccess()
                }
            }
        }
        
        viewModel.loadUser()
    }
    
    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Выход")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Выйти") { _, _ ->
                viewModel.logout(requireContext())
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    
    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление аккаунта")
            .setMessage("Вы уверены, что хотите удалить аккаунт? Это действие нельзя отменить.")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteAccount(requireContext())
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}

