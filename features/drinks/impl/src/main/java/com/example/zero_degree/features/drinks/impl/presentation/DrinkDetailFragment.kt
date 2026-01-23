package com.example.zero_degree.features.drinks.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.zero_degree.core.ui.ProtectedFragment
import com.example.zero_degree.core.ui.R as CoreUiR
import com.example.zero_degree.core.ui.adapters.ReviewAdapter
import com.example.zero_degree.features.drinks.impl.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class DrinkDetailFragment : ProtectedFragment() {
    
    private val viewModel by viewModels<DrinkDetailViewModel> {
        DrinkDetailViewModelFactory(requireActivity().application)
    }
    
    private lateinit var progressBar: View
    private lateinit var ivDrink: ImageView
    private lateinit var tvDrinkName: TextView
    private lateinit var tvDrinkPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvType: TextView
    private lateinit var tvTaste: TextView
    private lateinit var btnFavorite: MaterialButton
    private lateinit var rvReviews: RecyclerView
    
    private val reviewAdapter = ReviewAdapter()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_drink_detail, container, false)
    }
    
    override fun onViewCreatedProtected(view: View, savedInstanceState: Bundle?) {
        val drinkId = arguments?.getString("drinkId") ?: ""
        
        progressBar = view.findViewById(R.id.progressBar)
        ivDrink = view.findViewById(R.id.ivDrink)
        tvDrinkName = view.findViewById(R.id.tvDrinkName)
        tvDrinkPrice = view.findViewById(R.id.tvDrinkPrice)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvType = view.findViewById(R.id.tvType)
        tvTaste = view.findViewById(R.id.tvTaste)
        btnFavorite = view.findViewById(R.id.btnFavorite)
        rvReviews = view.findViewById(R.id.rvReviews)
        val btnAddReview = view.findViewById<MaterialButton>(R.id.btnAddReview)
        
        rvReviews.layoutManager = LinearLayoutManager(requireContext())
        rvReviews.adapter = reviewAdapter
        
        btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
        
        btnAddReview?.setOnClickListener {
            showAddReviewDialog(drinkId)
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.drink.collect { drink ->
                drink?.let {
                    tvDrinkName.text = it.name
                    tvDrinkPrice.text = "${it.price} ₽"
                    tvDescription.text = it.description ?: ""
                    tvType.text = "Тип: ${it.type ?: "Не указан"}"
                    tvTaste.text = "Вкус: ${it.taste ?: "Не указан"}"
                    
                    it.imageUrl?.let { url ->
                        ivDrink.load(url)
                    } ?: run {
                        ivDrink.setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavorite.collect { isFavorite ->
                val iconRes = if (isFavorite) {
                    android.R.drawable.btn_star_big_on
                } else {
                    android.R.drawable.btn_star_big_off
                }
                btnFavorite.setIconResource(iconRes)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviews.collect { reviews ->
                reviewAdapter.submitList(reviews)
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviewCreated.collect { created ->
                if (created) {
                    Toast.makeText(requireContext(), "Отзыв добавлен", Toast.LENGTH_SHORT).show()
                    viewModel.resetReviewCreated()
                }
            }
        }
        
        if (drinkId.isNotEmpty()) {
            viewModel.loadDrink(drinkId)
            viewModel.loadReviews(drinkId)
        }
    }
    
    private fun showAddReviewDialog(drinkId: String) {
        val dialogView = layoutInflater.inflate(CoreUiR.layout.dialog_add_review, null)
        val ratingBar = dialogView.findViewById<RatingBar>(CoreUiR.id.ratingBar)
        val commentEditText = dialogView.findViewById<TextInputEditText>(CoreUiR.id.etComment)
        
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Добавить отзыв")
            .setView(dialogView)
            .setPositiveButton("Отправить", null)
            .setNegativeButton("Отмена", null)
            .create()
        
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val rating = ratingBar.rating.toInt()
                val comment = commentEditText.text?.toString()?.trim() ?: ""
                
                if (comment.isEmpty()) {
                    Toast.makeText(requireContext(), "Введите комментарий", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                if (rating < 1 || rating > 5) {
                    Toast.makeText(requireContext(), "Выберите рейтинг", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                
                viewModel.createReview(drinkId, rating, comment)
                dialog.dismiss()
            }
        }
        
        dialog.show()
    }
}

