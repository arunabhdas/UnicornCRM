package app.unicornapp.unicorncrm.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.unicornapp.unicorncrm.SnackbarAction
import app.unicornapp.unicorncrm.SnackbarEvent
import app.unicornapp.unicorncrm.SnackbarManager
import kotlinx.coroutines.launch

class PermissionsViewModel: ViewModel() {
    private val _stateFlowColor = MutableStateFlow(0xFFFFFFFF)
    val stateFlowColor = _stateFlowColor.asStateFlow()

    var composeColor by mutableStateOf(0xFFFFFFFF)
        private set

    fun generateNewColor() {
        val randomColor = Random.nextLong(0xFFFFFFFF)
        _stateFlowColor.value = randomColor
        composeColor = randomColor
    }

    fun showSnackbar() {
        viewModelScope.launch {
            SnackbarManager.sendEvent(
                event = SnackbarEvent(
                    message = "Please check permissions",
                    action = SnackbarAction(
                        name = "Check",
                        action = {
                            viewModelScope.launch {
                                SnackbarManager.sendEvent(
                                    event = SnackbarEvent(
                                        message = "Checking permissions"
                                    )
                                )
                            }
                        }
                    )
                )
            )
        }
    }
}