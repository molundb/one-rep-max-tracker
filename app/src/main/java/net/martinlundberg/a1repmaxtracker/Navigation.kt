package net.martinlundberg.a1repmaxtracker

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.martinlundberg.NavViewModel
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailRoute
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListRoute
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailRoute

const val MOVEMENTS_LIST_ROUTE = "movements_list_route"

const val MOVEMENT_DETAIL_ROUTE = "movement_detail_route"
const val MOVEMENT_ID = "movementId"
const val MOVEMENT_NAME = "movementName"

const val ONE_REP_MAX_DETAIL_ROUTE = "one_rep_max_detail_route"
const val ONE_REP_MAX_ID = "oneRepMaxId"

@Composable
fun Navigation(
    navController: NavHostController = hiltViewModel<NavViewModel>().controller,
) {
    NavHost(navController = navController, startDestination = MOVEMENTS_LIST_ROUTE) {
        composable(
            route = MOVEMENTS_LIST_ROUTE,
        ) {
            MovementsListRoute(
                onMovementClick = { movement ->
                    navController.navigate("$MOVEMENT_DETAIL_ROUTE/${movement.id}/${movement.name}")
                },
            )
        }
        composable(
            route = "$MOVEMENT_DETAIL_ROUTE/{$MOVEMENT_ID}/{$MOVEMENT_NAME}",
            arguments = listOf(
                navArgument(MOVEMENT_ID) { type = NavType.LongType },
                navArgument(MOVEMENT_NAME) { type = NavType.StringType }
            ),
            enterTransition = slideInRight,
            popEnterTransition = null,
            popExitTransition = slideOutRight,
        ) { backStackEntry ->
            MovementDetailRoute(
                movementId = backStackEntry.arguments?.getLong(MOVEMENT_ID) ?: -1,
                movementName = backStackEntry.arguments?.getString(MOVEMENT_NAME) ?: "",
                onOneRepMaxClick = { oneRepMaxId, movementName ->
                    navController.navigate("$ONE_REP_MAX_DETAIL_ROUTE/${oneRepMaxId}/${movementName}")
                },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "$ONE_REP_MAX_DETAIL_ROUTE/{$ONE_REP_MAX_ID}/{$MOVEMENT_NAME}",
            arguments = listOf(
                navArgument(ONE_REP_MAX_ID) { type = NavType.LongType },
                navArgument(MOVEMENT_NAME) { type = NavType.StringType }
            ),
            enterTransition = slideInRight,
            popEnterTransition = null,
            popExitTransition = slideOutRight,
        ) { backStackEntry ->
            OneRepMaxDetailRoute(
                oneRepMaxId = backStackEntry.arguments?.getLong(ONE_REP_MAX_ID) ?: -1,
                movementName = backStackEntry.arguments?.getString(MOVEMENT_NAME) ?: "",
            )
        }
    }
}

private val slideInRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    slideIntoContainer(
        SlideDirection.Left,
        animationSpec = tween(700)
    )
}

private val slideOutRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    slideOutOfContainer(
        SlideDirection.Right,
        animationSpec = tween(700)
    )
}