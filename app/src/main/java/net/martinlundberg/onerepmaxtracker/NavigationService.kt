package net.martinlundberg.onerepmaxtracker

import android.content.Context
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface NavigationService {
    val navController: NavHostController
}

@Singleton
class NavigationServiceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : NavigationService {
    override val navController = NavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }
}