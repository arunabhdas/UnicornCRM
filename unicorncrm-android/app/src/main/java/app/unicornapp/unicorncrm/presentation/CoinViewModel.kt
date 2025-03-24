package app.unicornapp.unicorncrm.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoinViewModel(private val repository: CoinRepository) : ViewModel() {

    private val _coinList = MutableStateFlow<List<CoinPaprikaCoin>>(emptyList())
    val coinList: StateFlow<List<CoinPaprikaCoin>> = _coinList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchCoins()
    }

    fun refreshCoins() {
        fetchCoins()
    }

    private fun fetchCoins() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val coins = repository.getCoins()
                _coinList.value = coins
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}