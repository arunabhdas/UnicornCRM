package app.unicornapp.unicorncrm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import app.unicornapp.unicorncrm.data.model.settingsOptionsList
import app.unicornapp.unicorncrm.ui.screens.MoviesDetailScreen
import app.unicornapp.unicorncrm.ui.screens.MoviesPopularScreen
import app.unicornapp.unicorncrm.ui.screens.MoviesUpcomingScreen
import app.unicornapp.unicorncrm.ui.screens.NotificationScreen
import app.unicornapp.unicorncrm.ui.screens.SettingsScreen
import app.unicornapp.unicorncrm.ui.screens.RatesScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun SetupDrawerNavGraph(
    navController: NavHostController,
    navigator: DestinationsNavigator
) {
    NavHost(
        navController = navController,
        startDestination = ScreenDrawer.MoviesPopularScreen.route
    ) {
        // TODO-FIXME - MoviesScreen
        composable(
            route = ScreenDrawer.MoviesPopularScreen.route
        ) {
            MoviesPopularScreen(
                navController = navController,
                navigator = navigator
            )
        }

        // TODO-FIXME - Movies Detail Screen
        composable(
            route = ScreenDrawer.MoviesDetailScreen.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MoviesDetailScreen(
                movieId = movieId,
                navController = navController,
                navigator = navigator
            )
        }

        // TODO-FIXME - MoviesUpcomingScreen
        composable(
            route = ScreenDrawer.MoviesUpcomingScreen.route
        ) {
            MoviesUpcomingScreen(
                navController = navController,
                navigator = navigator
            )
        }

        // TODO-FIXME - RatesScreen
        composable(
            route = ScreenDrawer.RatesScreen.route
        ) {
            RatesScreen(
                navController = navController,
                navigator = navigator
            )
        }

        composable(
            route = ScreenDrawer.SettingsScreen.route
        ) {
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