package app.unicornapp.unicorncrm

import android.Manifest
import androidx.activity.result.ActivityResult
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import app.unicornapp.unicorncrm.presentation.DefaultViewModel
import app.unicornapp.unicorncrm.presentation.MainViewModel
import app.unicornapp.unicorncrm.ui.screens.NavGraphs
import app.unicornapp.unicorncrm.ui.theme.UnicornTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    private val viewModel: DefaultViewModel by viewModels()
    private val mainViewModel by viewModel<MainViewModel>()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContent {

            UnicornTheme {
                navHostController = rememberNavController()
                DestinationsNavHost(navGraph = NavGraphs.root)

            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
