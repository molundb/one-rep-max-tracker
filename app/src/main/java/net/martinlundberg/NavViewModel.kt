package net.martinlundberg

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.martinlundberg.a1repmaxtracker.NavigationService
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    navigationService: NavigationService,
) : ViewModel() {
    val controller = navigationService.navController
}