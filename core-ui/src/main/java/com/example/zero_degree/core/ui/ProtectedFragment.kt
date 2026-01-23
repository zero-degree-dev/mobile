package com.example.zero_degree.core.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.zero_degree.core.api.TokenManager

/**
 * Базовый класс для защищенных фрагментов.
 * Автоматически проверяет авторизацию и перенаправляет на экран входа, если пользователь не авторизован.
 */
abstract class ProtectedFragment : Fragment() {
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (!TokenManager.isLoggedIn(requireContext())) {
            // Используем рефлексию для создания LoginFragment, чтобы избежать зависимости от features
            try {
                val loginFragmentClass = Class.forName("com.example.zero_degree.features.auth.impl.presentation.LoginFragment")
                val loginFragment = loginFragmentClass.getDeclaredConstructor().newInstance() as Fragment
                NavigationHelper.replaceFragment(activity, loginFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        
        onViewCreatedProtected(view, savedInstanceState)
    }
    
    /**
     * Вызывается вместо onViewCreated, если пользователь авторизован.
     * Переопределите этот метод вместо onViewCreated для защищенных фрагментов.
     */
    abstract fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?)
}

