package net.martinlundberg.a1repmaxtracker

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.martinlundberg.a1repmaxtracker.features.movementdetail.MovementDetailRoute
import net.martinlundberg.a1repmaxtracker.features.movementslist.MovementsListRoute

const val MOVEMENTS_LIST_ROUTE = "movements_list_route"

const val MOVEMENT_DETAIL_ROUTE = "movement_detail_route"
const val MOVEMENT_NAME = "movementName"

@Composable
fun Navigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = MOVEMENTS_LIST_ROUTE) {
        composable(
            route = MOVEMENTS_LIST_ROUTE,
        ) {
            MovementsListRoute(
                onMovementClick = { name ->
                    navController.navigate("$MOVEMENT_DETAIL_ROUTE/$name")
                },
            )
        }
        composable(
            route = "$MOVEMENT_DETAIL_ROUTE/{$MOVEMENT_NAME}",
            arguments = listOf(
                navArgument(MOVEMENT_NAME) { type = NavType.StringType }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
        ) { backStackEntry ->
            MovementDetailRoute(movementName = backStackEntry.arguments?.getString(MOVEMENT_NAME) ?: "")
        }
    }
}