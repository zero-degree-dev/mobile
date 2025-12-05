package com.example.zero_degree.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.zero_degree.R
import com.example.zero_degree.ui.adapters.ReviewAdapter
import com.example.zero_degree.ui.viewmodel.DrinkDetailViewModel

class DrinkDetailFragment : Fragment() {
    
    private lateinit var viewModel: DrinkDetailViewModel
    private lateinit var rvReviews: RecyclerView
    private lateinit var progressBar: View
    
    private val reviewAdapter = ReviewAdapter()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drink_detail, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[DrinkDetailViewModel::class.java]
        
        val drinkId = arguments?.getInt("drinkId") ?: 0
        
        rvReviews = view.findViewById(R.id.rvReviews)
        progressBar = view.findViewById(R.id.progressBar)
        
        // Настройка RecyclerView
        rvReviews.layoutManager = LinearLayoutManager(requireContext())
        rvReviews.adapter = reviewAdapter
        
        // Кнопка избранного
        view.findViewById<View>(R.id.btnFavorite).setOnClickListener {
            viewModel.toggleFavorite()
        }
        
        // Наблюдаем за данными
        viewModel.drink.observe(viewLifecycleOwner) { drink ->
            drink?.let {
                view.findViewById<android.widget.TextView>(R.id.tvDrinkName).text = it.name
                view.findViewById<android.widget.TextView>(R.id.tvDrinkPrice).text = "${it.price} ₽"
                view.findViewById<android.widget.TextView>(R.id.tvDescription).text = it.description
                view.findViewById<android.widget.TextView>(R.id.tvType).text = "Тип: ${it.type}"
                view.findViewById<android.widget.TextView>(R.id.tvTaste).text = "Вкус: ${it.taste}"

//                view.findViewById<android.widget.ImageView>(R.id.ivDrink).load("https://minzchie.by/images/stories/klassifikatsiya-piva-po-tsvetu-4.jpg");
                view.findViewById<android.widget.ImageView>(R.id.ivDrink).load(it.imageUrl) {
                    placeholder(R.drawable.ic_launcher_background)
                }
            }
        }
        
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviewAdapter.submitList(reviews)
            view.findViewById<android.widget.TextView>(R.id.tvReviewsTitle).text = "Отзывы (${reviews.size})"
        }
        
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            val iconRes = if (isFavorite) {
                android.R.drawable.btn_star_big_on
            } else {
                android.R.drawable.btn_star_big_off
            }
            view.findViewById<android.widget.ImageButton>(R.id.btnFavorite)
                .setImageResource(iconRes)
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



