package app.unicornapp.unicorncrm.ui.navigation

sealed class ScreenDrawer {
    object HomeScreen: Screen(route = "home_screen")
    object SettingsScreen: Screen(route = "settings_screen")
    object NotificationScreen: Screen(route = "notification_screen")
    object HomeDetailScreen: Screen(route = "home_detail_screen")
}

