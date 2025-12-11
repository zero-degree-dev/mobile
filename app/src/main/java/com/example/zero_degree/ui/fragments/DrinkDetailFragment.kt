package com.example.zero_degree.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.zero_degree.R
import com.example.zero_degree.ui.adapters.ReviewAdapter
import com.example.zero_degree.ui.viewmodel.DrinkDetailViewModel

class DrinkDetailFragment : Fragment() {
    
    private val viewModel by viewModels<DrinkDetailViewModel>()
    
    private lateinit var rvReviews: RecyclerView
    private lateinit var progressBar: View
    private lateinit var ivDrink: ImageView
    private lateinit var tvDrinkName: TextView
    private lateinit var tvDrinkPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvType: TextView
    private lateinit var tvTaste: TextView
    private lateinit var tvReviewsTitle: TextView
    private lateinit var btnFavorite: ImageButton
    
    private val reviewAdapter = ReviewAdapter()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_drink_detail, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val drinkId = arguments?.getInt("drinkId") ?: 0
        
        // Инициализация views
        rvReviews = view.findViewById(R.id.rvReviews)
        progressBar = view.findViewById(R.id.progressBar)
        ivDrink = view.findViewById(R.id.ivDrink)
        tvDrinkName = view.findViewById(R.id.tvDrinkName)
        tvDrinkPrice = view.findViewById(R.id.tvDrinkPrice)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvType = view.findViewById(R.id.tvType)
        tvTaste = view.findViewById(R.id.tvTaste)
        tvReviewsTitle = view.findViewById(R.id.tvReviewsTitle)
        btnFavorite = view.findViewById(R.id.btnFavorite)
        
        // Настройка RecyclerView
        rvReviews.layoutManager = LinearLayoutManager(requireContext())
        rvReviews.adapter = reviewAdapter
        
        // Кнопка избранного
        btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
        
        // Наблюдаем за данными
        viewModel.drink.observe(viewLifecycleOwner) { drink ->
            drink?.let {
                tvDrinkName.text = it.name
                tvDrinkPrice.text = "${it.price} ₽"
                tvDescription.text = it.description
                tvType.text = "Тип: ${it.type}"
                tvTaste.text = "Вкус: ${it.taste}"
                
                if (it.imageUrl.isNotEmpty()) {
                    ivDrink.load(it.imageUrl) {
                        placeholder(R.drawable.ic_launcher_background)
                    }
                }
            }
        }
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviewAdapter.submitList(reviews)
            tvReviewsTitle.text = "Отзывы (${reviews.size})"
        }
        
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            val iconRes = if (isFavorite) {
                android.R.drawable.btn_star_big_on
            } else {
                android.R.drawable.btn_star_big_off
            }
            btnFavorite.setImageResource(iconRes)
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Загружаем данные
        if (drinkId > 0) {
            viewModel.loadDrink(drinkId)
        }
    }
}



