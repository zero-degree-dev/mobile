package com.example.zero_degree.features.auth.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.zero_degree.core.ui.NavigationHelper
import com.example.zero_degree.features.auth.impl.R
import com.example.zero_degree.features.home.impl.presentation.HomeFragment
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(requireActivity().application)
    }
    
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: MaterialButton
    private lateinit var tvError: TextView
    private lateinit var progressBar: ProgressBar
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)
        tvError = view.findViewById(R.id.tvError)
        progressBar = view.findViewById(R.id.progressBar)
        
        btnLogin.setOnClickListener {
            val email = etEmail.text?.toString() ?: ""
            val password = etPassword.text?.toString() ?: ""
            
            if (email.isEmpty() || password.isEmpty()) {
                showError("Заполните все поля")
                return@setOnClickListener
            }
            
            viewModel.login(email, password)
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                btnLogin.isEnabled = !isLoading
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                if (error != null) {
                    showError(error)
                } else {
                    hideError()
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginSuccess.collect { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Вход выполнен успешно!", Toast.LENGTH_SHORT).show()
                    // Небольшая задержка перед переходом, чтобы токен успел сохраниться
                    kotlinx.coroutines.delay(300)
                    // Переход на главный экран
                    try {
                        NavigationHelper.replaceFragment(activity, HomeFragment())
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Если ошибка, просто показываем Toast
                        Toast.makeText(requireContext(), "Ошибка перехода на главный экран", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    
    private fun showError(message: String) {
        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
    
    private fun hideError() {
        tvError.visibility = View.GONE
    }
}

