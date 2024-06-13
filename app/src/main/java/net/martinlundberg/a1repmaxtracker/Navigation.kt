package net.martinlundberg.a1repmaxtracker

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.martinlundberg.a1repmaxtracker.features.movementslist.MovementsListRoute

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "movements_list") {
        composable(route = "movements_list") {
            MovementsListRoute()
        }
        composable(route = "movement_detail") {
        }
    }
}