package com.example.zero_degree.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.zero_degree.ui.screens.BarsMapScreen
import com.example.zero_degree.ui.screens.BookingScreen
import com.example.zero_degree.ui.screens.DrinkCatalogScreen
import com.example.zero_degree.ui.screens.DrinkDetailScreen
import com.example.zero_degree.ui.screens.HomeScreen

// Определение маршрутов навигации
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DrinkCatalog : Screen("drink_catalog")
    object BarsMap : Screen("bars_map")
    object Booking : Screen("booking")
    object DrinkDetail : Screen("drink_detail/{drinkId}") {
        fun createRoute(drinkId: Int) = "drink_detail/$drinkId"
    }
    object BarDetail : Screen("bar_detail/{barId}") {
        fun createRoute(barId: Int) = "bar_detail/$barId"
    }
}

// Навигационный граф
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToBars = { navController.navigate(Screen.BarsMap.route) },
                onNavigateToDrinks = { navController.navigate(Screen.DrinkCatalog.route) },
                onNavigateToEvents = { /* TODO: создать экран событий */ },
                onNavigateToBar = { barId ->
                    navController.navigate(Screen.BarDetail.createRoute(barId))
                }
            )
        }
        
        composable(Screen.DrinkCatalog.route) {
            DrinkCatalogScreen(
                onNavigateToDrink = { drinkId ->
                    navController.navigate(Screen.DrinkDetail.createRoute(drinkId))
                }
            )
        }
        
        composable(Screen.BarsMap.route) {
            BarsMapScreen(
                onNavigateToBar = { barId ->
                    navController.navigate(Screen.BarDetail.createRoute(barId))
                }
            )
        }
        
        composable(Screen.Booking.route) {
            BookingScreen()
        }
        
        composable(
            route = Screen.DrinkDetail.route,
            arguments = listOf(navArgument("drinkId") { type = NavType.IntType })
        ) { backStackEntry ->
            val drinkId = backStackEntry.arguments?.getInt("drinkId") ?: 0
            DrinkDetailScreen(drinkId = drinkId)
        }
        
        composable(
            route = Screen.BarDetail.route,
            arguments = listOf(navArgument("barId") { type = NavType.IntType })
        ) { backStackEntry ->
            val barId = backStackEntry.arguments?.getInt("barId") ?: 0
            // TODO: создать экран детальной страницы бара
            // BarDetailScreen(barId = barId, onNavigateToBooking = {
            //     navController.navigate(Screen.Booking.route)
            // })
        }
    }
}

