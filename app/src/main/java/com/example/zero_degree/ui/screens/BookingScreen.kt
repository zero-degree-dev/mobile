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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zero_degree.data.model.Bar
import com.example.zero_degree.ui.theme.Primary
import com.example.zero_degree.ui.viewmodel.BookingViewModel

// Экран бронирования
@Composable
fun BookingScreen(
    userId: Int = 1, // В реальном приложении получать из авторизации
    viewModel: BookingViewModel = viewModel()
) {
    val bars by viewModel.bars.collectAsState()
    val selectedBar by viewModel.selectedBar.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()
    val guestsCount by viewModel.guestsCount.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val bookingSuccess by viewModel.bookingSuccess.collectAsState()
    
    // Загружаем бары при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadBars()
    }
    
    // Показываем диалог успешного бронирования
    if (bookingSuccess) {
        AlertDialog(
            onDismissRequest = { viewModel.resetBookingSuccess() },
            title = { Text("Успешно!") },
            text = { Text("Бронирование создано") },
            confirmButton = {
                TextButton(onClick = { viewModel.resetBookingSuccess() }) {
                    Text("OK")
                }
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Бронирование столика",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Выбор бара
                item {
                    Text(
                        text = "Выберите бар",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                items(bars) { bar ->
                    BarSelectionCard(
                        bar = bar,
                        isSelected = selectedBar?.id == bar.id,
                        onClick = { viewModel.setSelectedBar(bar) }
                    )
                }
                
                // Выбор даты
                item {
                    Text(
                        text = "Дата",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { viewModel.setDate(it) },
                        label = { Text("Дата (например, 2024-12-25)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Выбор времени
                item {
                    Text(
                        text = "Время",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    OutlinedTextField(
                        value = selectedTime,
                        onValueChange = { viewModel.setTime(it) },
                        label = { Text("Время (например, 19:00)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Количество гостей
                item {
                    Text(
                        text = "Количество гостей",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { 
                            if (guestsCount > 1) viewModel.setGuestsCount(guestsCount - 1)
                        }) {
                            Text("-", fontSize = 24.sp)
                        }
                        Text(
                            text = guestsCount.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { viewModel.setGuestsCount(guestsCount + 1) }) {
                            Text("+", fontSize = 24.sp)
                        }
                    }
                }
                
                // Кнопка бронирования
                item {
                    Button(
                        onClick = { viewModel.createBooking(userId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .height(56.dp),
                        enabled = selectedBar != null && selectedDate.isNotEmpty() && selectedTime.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Забронировать", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BarSelectionCard(
    bar: Bar,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bar.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = bar.address,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            if (isSelected) {
                Text("✓", fontSize = 24.sp, color = Primary)
            }
        }
    }
}


