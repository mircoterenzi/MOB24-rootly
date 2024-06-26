package com.unibo.rootly.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.unibo.rootly.ui.screens.AddPlantScreen
import com.unibo.rootly.ui.screens.HomeScreen
import com.unibo.rootly.ui.screens.PlantDetailsScreen
import com.unibo.rootly.ui.screens.SettingsScreen
import com.unibo.rootly.ui.screens.UserProfileScreen
import com.unibo.rootly.utils.LocationService
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.SettingsViewModel
import com.unibo.rootly.viewmodel.UserViewModel
import org.koin.androidx.compose.koinViewModel

sealed class RootlyRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
){
    data object Home : RootlyRoute("plants" , "To-do")
    data object Settings : RootlyRoute("settings", "Settings")
    data object AddPlant : RootlyRoute("add","Add a plant")
    data object PlantDetails: RootlyRoute("plant","Plant details")
    data object UserProfile : RootlyRoute(
        "user/{userId}",
        "Profile",
        listOf(navArgument("userId") { type = NavType.StringType } )
    )
    companion object {
        val routes = setOf(Home, Settings, PlantDetails, UserProfile, AddPlant)
    }
}

@Composable
fun RootlyNavGraph(
    navController: NavHostController,
    settingsVM: SettingsViewModel,
    locationService: LocationService,
    userId: Int,
    modifier: Modifier = Modifier
) {
    val  plantViewModel = koinViewModel<PlantViewModel>()
    val  userViewModel = koinViewModel<UserViewModel>()
    userViewModel.setUser(userId)

    NavHost(
        navController = navController,
        startDestination = RootlyRoute.Home.route,
        modifier = modifier
    ) {
        with(RootlyRoute.Home) {
            composable(route) {
                HomeScreen(
                    navController,
                    plantViewModel,
                    userViewModel
                )
            }
        }
        with(RootlyRoute.UserProfile) {
            composable(route, arguments) {
                UserProfileScreen(
                    navController,
                    userViewModel,
                    locationService
                )
            }
        }
        with(RootlyRoute.PlantDetails) {
            composable(route, arguments) {
                val plant = plantViewModel.plantSelected
                if(plant != null) {
                    PlantDetailsScreen(
                        navController,
                        plantViewModel
                    )
                }
            }
        }
        with(RootlyRoute.AddPlant) {
            composable(route) {
                AddPlantScreen(navController, plantViewModel, userViewModel)
            }
        }
        with(RootlyRoute.Settings) {
            composable(route) {
                SettingsScreen(
                    navController,
                    settingsVM,
                    settingsVM.state
                )
            }
        }
    }
}