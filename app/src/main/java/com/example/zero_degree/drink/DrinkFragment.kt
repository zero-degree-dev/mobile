package com.example.zero_degree.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.zero_degree.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DrinkFragment : Fragment() {

    private val viewModel by viewModels<DrinkViewModel>()
    
    private lateinit var drinkImage: ImageView
    private lateinit var drinkName: TextView
    private lateinit var drinkDescription: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация views
        drinkImage = view.findViewById(R.id.drinkImage)
        drinkName = view.findViewById(R.id.drinkName)
        drinkDescription = view.findViewById(R.id.drinkDescription)

        // Подписка на данные напитка
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.drink.collectLatest { drink ->
                drink?.let {
                    drinkName.text = it.name
                    drinkDescription.text = it.description
                    
                    if (it.imageUrl.isNotEmpty()) {
                        drinkImage.load(it.imageUrl) {
                            placeholder(R.drawable.ic_launcher_background)
                        }
                    }
                }
            }
        }
        
        // Загружаем данные напитка
        viewModel.loadDrink()
    }
}
