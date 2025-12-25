package com.example.zero_degree.core.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

object NavigationHelper {
    
    fun replaceFragment(activity: FragmentActivity?, fragment: Fragment) {
        if (activity == null) return
        
        try {
            // Пытаемся получить fragment_container через рефлексию
            val rClass = Class.forName("com.example.zero_degree.R\$id")
            val fragmentContainerField = rClass.getField("fragment_container")
            val containerId = fragmentContainerField.getInt(null)
            
            activity.supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit()
        } catch (e: Exception) {
            // Fallback - ищем container view по имени ресурса
            try {
                val resources = activity.resources
                val containerId = resources.getIdentifier("fragment_container", "id", activity.packageName)
                if (containerId != 0) {
                    activity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    throw Exception("Container not found")
                }
            } catch (e2: Exception) {
                // Последний fallback - используем android.R.id.content
                activity.supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    
    /**
     * Заменяет текущий фрагмент и очищает back stack.
     * Используется при выходе из аккаунта для предотвращения возврата к защищенным экранам.
     */
    fun replaceFragmentAndClearBackStack(activity: FragmentActivity?, fragment: Fragment) {
        if (activity == null) return
        
        try {
            // Очищаем back stack
            activity.supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
            
            // Пытаемся получить fragment_container через рефлексию
            val rClass = Class.forName("com.example.zero_degree.R\$id")
            val fragmentContainerField = rClass.getField("fragment_container")
            val containerId = fragmentContainerField.getInt(null)
            
            activity.supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit()
        } catch (e: Exception) {
            // Fallback - ищем container view по имени ресурса
            try {
                val resources = activity.resources
                val containerId = resources.getIdentifier("fragment_container", "id", activity.packageName)
                if (containerId != 0) {
                    activity.supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    activity.supportFragmentManager.beginTransaction()
                        .replace(containerId, fragment)
                        .commit()
                } else {
                    throw Exception("Container not found")
                }
            } catch (e2: Exception) {
                // Последний fallback - используем android.R.id.content
                activity.supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                activity.supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit()
            }
        }
    }
}

