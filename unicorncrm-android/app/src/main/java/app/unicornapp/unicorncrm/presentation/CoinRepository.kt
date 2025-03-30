package app.unicornapp.unicorncrm.presentation

import app.unicornapp.unicorncrm.api.CoinPaprikaApiService
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import app.unicornapp.unicorncrm.data.model.CoinTicker
import timber.log.Timber


class CoinRepository(private val apiService: CoinPaprikaApiService) {

    suspend fun getCoins(): List<CoinPaprikaCoin> {
        Timber.d("CoinRepository: Calling getCoins API")
        val coins = apiService.getCoins()
        Timber.d("CoinRepository: getCoins returned ${coins.size} coins")
        return coins
    }

    suspend fun getTickers(): List<CoinTicker> {
        Timber.d("CoinRepository: Calling getTickers API")
        val tickers = apiService.getTickers()
        Timber.d("CoinRepository: getTickers returned ${tickers.size} tickers")
        return tickers
    }

    suspend fun getCoinsWithPrices(): List<CoinWithPrice> {
        Timber.d("CoinRepository: Getting coins and tickers for combined data")
        try {
            val coins = apiService.getCoins()
            Timber.d("CoinRepository: Got ${coins.size} coins")
            
            val tickers = apiService.getTickers()
            Timber.d("CoinRepository: Got ${tickers.size} tickers")

            val result = coins.mapNotNull { coin ->
                val ticker = tickers.find { it.id == coin.id }
                if (ticker != null) {
                    // Check if USD quote is available
                    val usdQuote = ticker.quotes["USD"]
                    if (usdQuote != null) {
                        // TODO-FIXME-CLEANUP Timber.d("CoinRepository: Found USD price for ${coin.name}: $${usdQuote.price}")
                        CoinWithPrice(
                            id = coin.id,
                            name = coin.name,
                            symbol = coin.symbol,
                            rank = coin.rank,
                            price = usdQuote.price,
                            priceChange24h = usdQuote.percent_change_24h
                        )
                    } else {
                        Timber.w("CoinRepository: No USD quote available for ${coin.name}")
                        null
                    }
                } else null
            }
            
            Timber.d("CoinRepository: Combined ${result.size} coins with prices")
            
            if (result.isEmpty()) {
                Timber.w("CoinRepository: No matches found between coins and tickers!")
            }
            
            return result
        } catch (e: Exception) {
            Timber.e(e, "CoinRepository: Error in getCoinsWithPrices: ${e.message}")
            throw e
        }
    }
}

data class CoinWithPrice(
    val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val price: Double,
    val priceChange24h: Double
)