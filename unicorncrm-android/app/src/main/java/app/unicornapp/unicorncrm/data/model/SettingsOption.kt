package app.unicornapp.unicorncrm.data.model


data class SettingsOption(
    val title: String,
    val description: String,
    val settingsValue: String
)


val settingsOptionsList = listOf(
    SettingsOption("Email notifications", "Receive email notifications", "ON"),
    SettingsOption("Push notifications", "Receive push notifications", "ON"),
    SettingsOption("SMS notifications", "Receive SMS notifications", "OFF"),
)