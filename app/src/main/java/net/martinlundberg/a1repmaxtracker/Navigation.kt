package net.martinlundberg.a1repmaxtracker

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailRoute
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListRoute
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailRoute
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit

const val MOVEMENTS_LIST_ROUTE = "movements_list_route"

const val MOVEMENT_DETAIL_ROUTE = "movement_detail_route"
const val MOVEMENT_ID = "movementId"
const val MOVEMENT_NAME = "movementName"

const val ONE_REP_MAX_DETAIL_ROUTE = "one_rep_max_detail_route"
const val ONE_REP_MAX_ID = "oneRepMaxId"

const val animation_duration = 300

@Composable
fun Navigation(
    navViewModel: NavViewModel = hiltViewModel(),
) {
    val weightUnit by navViewModel.weightUnitFlow.collectAsState()

    DefaultScaffold(
        weightUnit = weightUnit,
        setWeightUnit = navViewModel::setWeightUnit,
    ) { innerPadding ->
        NavigationHost(navViewModel.controller, innerPadding)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultScaffold(
    weightUnit: WeightUnit = WeightUnit.KILOGRAMS,
    setWeightUnit: (Boolean) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(top = 24.dp),
                title = {
                    Text(
                        text = stringResource(R.string.top_bar_title),
                        style = MaterialTheme.typography.displayLarge,
                    )
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = weightUnit.toString(LocalContext.current),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Box(modifier = Modifier.size(4.dp))
                        Switch(
                            checked = weightUnit.isPounds(),
                            onCheckedChange = { isPounds ->
                                setWeightUnit(isPounds)
                            },
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            content(innerPadding)
        }
    )
}

@Composable
private fun NavigationHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = MOVEMENTS_LIST_ROUTE,
    ) {
        composable(
            route = MOVEMENTS_LIST_ROUTE,
            exitTransition = slideOutToLeft,
            enterTransition = slideInFromLeft,
        ) {
            MovementsListRoute(
                innerPadding = innerPadding,
                onMovementClick = { movement, lifeCycleState ->
                    if (lifeCycleState.isAtLeast(Lifecycle.State.RESUMED)) {
                        navController.navigate("$MOVEMENT_DETAIL_ROUTE/${movement.id}/${movement.name}")
                    }
                },
            )
        }
        composable(
            route = "$MOVEMENT_DETAIL_ROUTE/{$MOVEMENT_ID}/{$MOVEMENT_NAME}",
            arguments = listOf(
                navArgument(MOVEMENT_ID) { type = NavType.LongType },
                navArgument(MOVEMENT_NAME) { type = NavType.StringType }
            ),
            enterTransition = slideInFromRight,
            popExitTransition = slideOutToRight,
        ) { backStackEntry ->
            MovementDetailRoute(
                innerPadding = innerPadding,
                movementId = backStackEntry.arguments?.getLong(MOVEMENT_ID) ?: -1,
                movementName = backStackEntry.arguments?.getString(MOVEMENT_NAME) ?: "",
                //                onOneRepMaxClick = { oneRepMaxId, movementName ->
                //                    navController.navigate("$ONE_REP_MAX_DETAIL_ROUTE/${oneRepMaxId}/${movementName}")
                //                },
                navigateBack = { lifeCycleState ->
                    if (lifeCycleState.isAtLeast(Lifecycle.State.RESUMED)) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable(
            route = "$ONE_REP_MAX_DETAIL_ROUTE/{$ONE_REP_MAX_ID}/{$MOVEMENT_NAME}",
            arguments = listOf(
                navArgument(ONE_REP_MAX_ID) { type = NavType.LongType },
                navArgument(MOVEMENT_NAME) { type = NavType.StringType }
            ),
            enterTransition = slideInFromRight,
            popEnterTransition = null,
            popExitTransition = slideOutToRight,
        ) { backStackEntry ->
            OneRepMaxDetailRoute(
                oneRepMaxId = backStackEntry.arguments?.getLong(ONE_REP_MAX_ID) ?: -1,
                movementName = backStackEntry.arguments?.getString(MOVEMENT_NAME) ?: "",
            )
        }
    }
}

private val slideInFromRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    slideIntoContainer(
        SlideDirection.Left,
        animationSpec = tween(animation_duration)
    )
}

private val slideInFromLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    slideIntoContainer(
        SlideDirection.Right,
        animationSpec = tween(animation_duration)
    )
}

private val slideOutToRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    slideOutOfContainer(
        SlideDirection.Right,
        animationSpec = tween(animation_duration)
    )
}

private val slideOutToLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    slideOutOfContainer(
        SlideDirection.Left,
        animationSpec = tween(animation_duration)
    )
}
