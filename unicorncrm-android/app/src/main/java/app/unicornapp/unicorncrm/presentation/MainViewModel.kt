package app.unicornapp.unicorncrm.presentation
import app.unicornapp.unicorncrm.presentation.MainRepository

import androidx.lifecycle.ViewModel

class MainViewModel(
    val repository: MainRepository
): ViewModel() {
    fun doNetworkCall() {
        repository.doNetworkCall()
    }
}