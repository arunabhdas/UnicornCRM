package app.unicornapp.unicorncrm.presentation
import app.unicornapp.unicorncrm.presentation.MainRepository

import androidx.lifecycle.ViewModel
import timber.log.Timber

class MainViewModel(
    val repository: MainRepository
): ViewModel() {
    fun doNetworkCall() {
        Timber.d("MainViewModel -------------------------- doNetworkCall()")
    }
}