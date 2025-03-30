package app.unicornapp.unicorncrm

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.ui.Modifier
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
import org.koin.androidx.viewmodel.ext.android.getViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navHostController: NavHostController
    private val viewModel: DefaultViewModel by viewModels()

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
                val snackbarHostState = remember {
                   SnackbarHostState()
                }
                val scope = rememberCoroutineScope()

                ObserveAsEvents(
                    flow = SnackbarManager.events,
                    key1 = snackbarHostState
                    ) { event ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action?.name,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            event.action?.action?.invoke()
                        }
                    }
                }
                // TODO-FIXME-DEPRECATE val viewModel = getViewModel<MainViewModel>()
                // TODO-FIXME-DEPRECATE viewModel.doNetworkCall()
                navHostController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState
                        )
                    }

                ) { innerPadding ->
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }

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
