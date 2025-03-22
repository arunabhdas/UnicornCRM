package app.unicornapp.unicorncrm.ui.screens

import android.bluetooth.le.BluetoothLeScanner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.unicornapp.unicorncrm.presentation.MockDestinationsNavigator
import app.unicornapp.unicorncrm.ui.screens.destinations.MainScreenDrawerNavigationDestination
import app.unicornapp.unicorncrm.ui.theme.TertiaryColor
import app.unicornapp.unicorncrm.ui.theme.ThemeUtils
import app.unicornapp.unicorncrm.ui.theme.TransparentColor
import app.unicornapp.unicorncrm.ui.theme.createGradientEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import timber.log.Timber

@Destination
@OptIn(ExperimentalPermissionsApi::class)
@RootNavGraph(start = true)
@Composable
fun PermissionsScreen(
    // TODO-DEPRECATE navController: NavController,
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    var bleScanner: BluetoothLeScanner? = null

    LaunchedEffect(key1 = "LandingScreenAppeared") {
        Timber.d( "route: LandingScreen")
    }
    var showExpandedText by remember {
        mutableStateOf(false)
    }
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
        ) {
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
        }
    }

}



@Composable
@Preview
fun PermissionsScreenPreview() {
    LandingScreen(
        navigator = MockDestinationsNavigator()
    )
}