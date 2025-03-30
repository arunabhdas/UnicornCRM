package app.unicornapp.unicorncrm.api

import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import app.unicornapp.unicorncrm.data.model.CoinTicker
import retrofit2.http.GET


interface CoinPaprikaApiService {
    @GET("v1/coins")
    suspend fun getCoins(): List<CoinPaprikaCoin>

    @GET("v1/tickers")
    suspend fun getTickers(): List<CoinTicker>

}