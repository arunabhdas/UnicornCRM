package app.unicornapp.unicorncrm.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class CoinViewModel(private val repository: CoinRepository) : ViewModel() {

    private val _coinList = MutableStateFlow<List<CoinPaprikaCoin>>(emptyList())
    val coinList: StateFlow<List<CoinPaprikaCoin>> = _coinList

    private val _coinsWithPricesList = MutableStateFlow<List<CoinWithPrice>>(emptyList())
    val coinsWithPricesList: StateFlow<List<CoinWithPrice>> = _coinsWithPricesList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        Timber.d("CoinViewModel initialized")
        fetchCoinsWithPrices()
    }

    fun refreshCoins() {
        Timber.d("Refreshing coins data")
        fetchCoinsWithPrices()
    }

    private fun fetchCoins() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Timber.d("Fetching coins from repository")
                val coins = repository.getCoins()
                Timber.d("Fetched ${coins.size} coins from API")
                _coinList.value = coins
            } catch (e: Exception) {
                Timber.e(e, "Error fetching coins: ${e.message}")
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun fetchCoinsWithPrices() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                Timber.d("Fetching coins with prices from repository")
                val coinsWithPrices = repository.getCoinsWithPrices()
                Timber.d("Fetched ${coinsWithPrices.size} coins with prices from API")
                
                // Always update the coins with prices list
                _coinsWithPricesList.value = coinsWithPrices
                Timber.d("Updated coinsWithPricesList with ${coinsWithPrices.size} items")
                
                // Log some of the prices to verify data
                coinsWithPrices.take(5).forEach { coin ->
                    Timber.d("Price data: ${coin.name} (${coin.symbol}) - $${String.format("%,.2f", coin.price)} / ${coin.priceChange24h}%")
                }
                
                // Also update the plain coin list if we got results
                if (coinsWithPrices.isNotEmpty()) {
                    // Convert CoinWithPrice objects to CoinPaprikaCoin objects
                    _coinList.value = coinsWithPrices.map { coinWithPrice -> 
                        CoinPaprikaCoin(
                            id = coinWithPrice.id,
                            name = coinWithPrice.name,
                            symbol = coinWithPrice.symbol,
                            rank = coinWithPrice.rank,
                            is_active = true,
                            is_new = false,
                            type = "cryptocurrency"
                        )
                    }
                    Timber.d("Updated coinList with ${_coinList.value.size} items")
                } else {
                    Timber.w("No coins with prices returned from repository")
                    // Fallback to just fetching basic coin info if no prices available
                    fetchCoins()
                }
            } catch (e: Exception) {
                Timber.e(e, "Error fetching coins with prices: ${e.message}")
                e.printStackTrace()
                // Fallback to basic coins if there's an error
                fetchCoins()
            } finally {
                _isLoading.value = false
            }
        }
    }
}