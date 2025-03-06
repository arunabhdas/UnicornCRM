package app.unicornapp.unicorncrm.api

import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import retrofit2.http.GET


interface CoinPaprikaApiService {
    @GET("v1/coins")
    suspend fun getCoins(): List<CoinPaprikaCoin>
}