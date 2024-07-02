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
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailRoute
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailViewModel
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListRoute
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListViewModel

const val MOVEMENTS_LIST_ROUTE = "movements_list_route"

const val MOVEMENT_DETAIL_ROUTE = "movement_detail_route"
const val MOVEMENT_ID = "movementId"
const val MOVEMENT_NAME = "movementName"

@Composable
fun Navigation(
    movementsListViewModel: MovementsListViewModel,
    movementDetailViewModel: MovementDetailViewModel,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = MOVEMENTS_LIST_ROUTE) {
        composable(
            route = MOVEMENTS_LIST_ROUTE,
        ) {
            MovementsListRoute(
                onMovementClick = { movement ->
                    navController.navigate("$MOVEMENT_DETAIL_ROUTE/${movement.id}/${movement.name}")
                },
                movementsListViewModel = movementsListViewModel
            )
        }
        composable(
            route = "$MOVEMENT_DETAIL_ROUTE/{$MOVEMENT_ID}/{$MOVEMENT_NAME}",
            arguments = listOf(
                navArgument(MOVEMENT_ID) { type = NavType.LongType },
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
            MovementDetailRoute(
                movementId = backStackEntry.arguments?.getLong(MOVEMENT_ID) ?: -1,
                movementName = backStackEntry.arguments?.getString(MOVEMENT_NAME) ?: "",
                movementDetailViewModel = movementDetailViewModel
            )
        }
    }
}