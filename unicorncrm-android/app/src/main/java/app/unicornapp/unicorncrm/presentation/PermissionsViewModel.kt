package app.unicornapp.unicorncrm.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

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

}