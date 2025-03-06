package app.unicornapp.unicorncrm.presentation

import app.unicornapp.unicorncrm.api.CoinPaprikaApiService
import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin


class CoinRepository(private val apiService: CoinPaprikaApiService) {
    suspend fun getCoins(): List<CoinPaprikaCoin> = apiService.getCoins()
}