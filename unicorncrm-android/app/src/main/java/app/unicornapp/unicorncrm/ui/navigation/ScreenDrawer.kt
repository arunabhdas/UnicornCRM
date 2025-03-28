package app.unicornapp.unicorncrm.ui.navigation

sealed class ScreenDrawer {
    object MoviesScreen: Screen(route = "movies_screen")
    object RatesScreen: Screen(route = "rates_screen")
    object SettingsScreen: Screen(route = "settings_screen")
    object NotificationScreen: Screen(route = "notification_screen")
    object HomeDetailScreen: Screen(route = "home_detail_screen")
    object MoviesDetailScreen: Screen(route = "movies_detail_screen/{movieId}")
}

