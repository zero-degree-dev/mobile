package com.example.zero_degree.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.zero_degree.data.model.Drink
import com.example.zero_degree.ui.theme.Primary
import com.example.zero_degree.ui.viewmodel.DrinkCatalogViewModel

// Экран каталога напитков
@Composable
fun DrinkCatalogScreen(
    onNavigateToDrink: (Int) -> Unit,
    viewModel: DrinkCatalogViewModel = viewModel()
) {
    val drinks by viewModel.drinks.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val selectedTaste by viewModel.selectedTaste.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Загружаем напитки при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadDrinks()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Каталог напитков",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Фильтры
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedType == "пиво",
                onClick = { viewModel.setTypeFilter(if (selectedType == "пиво") null else "пиво") },
                label = { Text("Пиво") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = selectedType == "лимонад",
                onClick = { viewModel.setTypeFilter(if (selectedType == "лимонад") null else "лимонад") },
                label = { Text("Лимонад") },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTaste == "сладкий",
                onClick = { viewModel.setTasteFilter(if (selectedTaste == "сладкий") null else "сладкий") },
                label = { Text("Сладкий") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = selectedTaste == "горький",
                onClick = { viewModel.setTasteFilter(if (selectedTaste == "горький") null else "горький") },
                label = { Text("Горький") },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Список напитков
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(drinks) { drink ->
                    DrinkCard(drink = drink, onClick = { onNavigateToDrink(drink.id) })
                }
            }
        }
    }
}

@Composable
fun DrinkCard(
    drink: Drink,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = drink.imageUrl,
                contentDescription = drink.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = drink.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = drink.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "${drink.price} ₽",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}


