package app.unicornapp.unicorncrm.ui.screens

import android.Manifest
import android.bluetooth.le.BluetoothLeScanner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import app.unicornapp.unicorncrm.SnackbarEvent
import app.unicornapp.unicorncrm.SnackbarManager
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.presentation.PermissionsViewModel
import app.unicornapp.unicorncrm.ui.screens.destinations.MainScreenDrawerNavigationDestination
import app.unicornapp.unicorncrm.ui.theme.TertiaryColor
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.TransparentColor
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import timber.log.Timber

@Destination
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsScreen(
    // TODO-DEPRECATE navController: NavController,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    var bleScanner: BluetoothLeScanner? = null

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
        )
    )

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val viewModel = viewModel<PermissionsViewModel>()
    val composeColor = viewModel.composeColor
    val stateFlowColor = viewModel.stateFlowColor.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = "LandingScreenAppeared") {
        Timber.d( "route: LandingScreen")
    }
    var showExpandedText by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = createGradientEffect(
                    colors = ThemeUtils.GradientColors,
                    isVertical = true
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp)
                .padding(vertical = 8.dp)
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = TertiaryColor
                )
            ) {
                Text(text = "Check Permissions")
            }
            Button(
                onClick = {
                    navigator.navigate(
                        MainScreenDrawerNavigationDestination()
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(TransparentColor),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = TertiaryColor
                )
            ) {
                Text(text = "Get Started")
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (cameraPermissionState.status.isGranted) {
                Text(
                    text = "Camera permission Granted",
                    color = Color.White
                )
            } else {
                Column {
                    val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                        "The camera is needed for this app to work. We respsect your privacy and only use the camera to capture visual input. No data will be stored on our end. Please grant camera permission."
                    } else {
                        "Camera not available"
                    }

                    Text(
                        text = textToShow,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { cameraPermissionState.launchPermissionRequest() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = TertiaryColor
                        )
                    ) {
                        Text(
                            text = "Request permission",
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (multiplePermissionsState.allPermissionsGranted) {
                // If all permissions are granted, then show screen with the feature enabled
                Text(
                    text = "Camera and Read storage permissions Granted! Thank you!",
                    color = Color.White
                )
            } else {
                Column {
                    Text(text =
                        getTextToShowGivenPermissions(
                            multiplePermissionsState.revokedPermissions,
                            multiplePermissionsState.shouldShowRationale
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        multiplePermissionsState.launchMultiplePermissionRequest()
                        viewModel.showSnackbar(
                            messageInitial = "Requesting record audio and camera permissions",
                            messageAfterAction = "Dismiss"
                        )
                    }
                    ) {
                        Text(
                            text = "Request record audio and camera permissions",
                            color = Color.White
                        )
                    }
                }
            }
            if (locationPermissionsState.allPermissionsGranted) {
                Text(
                    text = "Thanks! I can access your exact location :D",
                    color = Color.White
                )
            } else {
                Column {
                    val allPermissionsRevoked =
                        locationPermissionsState.permissions.size ==
                                locationPermissionsState.revokedPermissions.size

                    val textToShow = if (!allPermissionsRevoked) {
                        // If not all the permissions are revoked, it's because the user accepted the COARSE
                        // location permission, but not the FINE one.
                        "Yay! Thanks for letting me access your approximate location. " +
                                "But you know what would be great? If you allow me to know where you " +
                                "exactly are. Thank you!"
                    } else if (locationPermissionsState.shouldShowRationale) {
                        // Both location permissions have been denied
                        "Getting your exact location is important for this app. " +
                                "Please grant us fine location. Thank you :D"
                    } else {
                        // First time the user sees this feature or the user doesn't want to be asked again
                        "This feature requires location permission"
                    }

                    val buttonText = if (!allPermissionsRevoked) {
                        "Allow precise location"
                    } else {
                        "Request location permissions"
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick =
                        {
                            locationPermissionsState.launchMultiplePermissionRequest()
                            scope.launch {
                                SnackbarManager.sendEvent(
                                    event = SnackbarEvent(
                                        message = "Launching Multiple Permission Request"
                                    )
                                )
                            }
                        }
                    ) {
                        Text(
                            text = buttonText,
                            color = Color.White
                        )

                    }
                }
            }
        }
    }

}


@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }
            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }
            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}


@Composable
@Preview
fun PermissionsScreenPreview() {
    LandingScreen(
        navigator = MockDestinationsNavigator()
    )
}