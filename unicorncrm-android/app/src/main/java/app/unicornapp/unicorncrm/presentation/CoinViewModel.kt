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

    init {
        fetchCoins()
    }

    private fun fetchCoins() {
        viewModelScope.launch {
            try {
                val coins = repository.getCoins()
                _coinList.value = coins
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}