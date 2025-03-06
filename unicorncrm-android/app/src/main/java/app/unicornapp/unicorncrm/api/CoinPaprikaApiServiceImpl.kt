package app.unicornapp.unicorncrm.api


import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import retrofit2.Retrofit

class CoinPaprikaApiServiceImpl(
    private val retrofit: Retrofit
) : CoinPaprikaApiService {

    // Create an instance of the API interface using Retrofit
    private val service: CoinPaprikaApiService by lazy {
        retrofit.create(CoinPaprikaApiService::class.java)
    }

    override suspend fun getCoins(): List<CoinPaprikaCoin> {
        return service.getCoins()
    }
}