package app.unicornapp.unicorncrm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.unicornapp.unicorncrm.data.model.settingsOptionsList
import app.unicornapp.unicorncrm.ui.screens.HomeScreen
import app.unicornapp.unicorncrm.ui.screens.NotificationScreen
import app.unicornapp.unicorncrm.ui.screens.SettingsScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun SetupDrawerNavGraph(
    navController: NavHostController,
    navigator: DestinationsNavigator
) {
    NavHost(
        navController = navController,
        startDestination = ScreenDrawer.HomeScreen.route
    ) {

        composable(
            route = ScreenDrawer.HomeScreen.route
        ) {
            HomeScreen(
                navController = navController,
                navigator = navigator
            )
        }

        composable(
            route = ScreenDrawer.SettingsScreen.route
        ) {
            // TODO-Pass settingsOptionsList to SetttingsScreen
            SettingsScreen(
                navController = navController,
                navigator = navigator,
                options = settingsOptionsList
            )
        }

        composable(
            route = ScreenDrawer.NotificationScreen.route
        ) {
            NotificationScreen(
                navController = navController,
                navigator = navigator
            )
        }

    }
}