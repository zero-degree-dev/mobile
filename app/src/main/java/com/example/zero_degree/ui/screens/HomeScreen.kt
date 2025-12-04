package com.example.zero_degree.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.zero_degree.data.model.Bar
import com.example.zero_degree.data.model.Event
import com.example.zero_degree.ui.theme.Primary
import com.example.zero_degree.ui.viewmodel.HomeViewModel

// Главный экран
@Composable
fun HomeScreen(
    onNavigateToBars: () -> Unit,
    onNavigateToDrinks: () -> Unit,
    onNavigateToEvents: () -> Unit,
    onNavigateToBar: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val recentBars by viewModel.recentBars.collectAsState()
    val activeEvents by viewModel.activeEvents.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Загружаем данные при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Приветствие
        Text(
            text = "Добро пожаловать в ZERO°!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Быстрые действия
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionButton(
                text = "Бары",
                onClick = onNavigateToBars,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                text = "Меню",
                onClick = onNavigateToDrinks,
                modifier = Modifier.weight(1f)
            )
            QuickActionButton(
                text = "События",
                onClick = onNavigateToEvents,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Последние посещенные бары
        Text(
            text = "Последние бары",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recentBars) { bar ->
                    BarCard(bar = bar, onClick = { onNavigateToBar(bar.id) })
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Активные события
        Text(
            text = "Активные события",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            activeEvents.forEach { event ->
                EventCard(event = event, modifier = Modifier.padding(bottom = 8.dp))
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Primary)
    ) {
        Text(text = text)
    }
}

@Composable
fun BarCard(
    bar: Bar,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .width(200.dp)
            .height(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = bar.imageUrl,
                contentDescription = bar.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = bar.name,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = event.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = event.date,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}


