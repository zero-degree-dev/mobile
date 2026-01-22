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

class RegisterFragment : Fragment() {
    
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(requireActivity().application)
    }
    
    private lateinit var etEmail: TextInputEditText
    private lateinit var etName: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnRegister: MaterialButton
    private lateinit var tvError: TextView
    private lateinit var progressBar: ProgressBar
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        etEmail = view.findViewById(R.id.etEmail)
        etName = view.findViewById(R.id.etName)
        etPassword = view.findViewById(R.id.etPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        tvError = view.findViewById(R.id.tvError)
        progressBar = view.findViewById(R.id.progressBar)
        
        btnRegister.setOnClickListener {
            val email = etEmail.text?.toString() ?: ""
            val name = etName.text?.toString() ?: ""
            val password = etPassword.text?.toString() ?: ""
            
            if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                showError("Заполните все поля")
                return@setOnClickListener
            }
            
            if (password.length < 6) {
                showError("Пароль должен быть не менее 6 символов")
                return@setOnClickListener
            }
            
            viewModel.register(name, email, password)
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                btnRegister.isEnabled = !isLoading
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
            viewModel.registerSuccess.collect { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Регистрация выполнена успешно!", Toast.LENGTH_SHORT).show()
                    // Переход на главный экран
                    NavigationHelper.replaceFragment(activity, HomeFragment())
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

