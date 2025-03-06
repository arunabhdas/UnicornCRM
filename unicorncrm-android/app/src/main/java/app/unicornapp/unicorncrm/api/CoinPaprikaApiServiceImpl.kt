package app.unicornapp.unicorncrm.api


import app.unicornapp.unicorncrm.data.model.CoinPaprikaCoin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.prefs.Preferences

class CoinPaprikaApiServiceImpl(
    private val preferences: Preferences // Your dependency injected preferences
) : CoinPaprikaApiService {

    // Lazy initialization of Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coinpaprika.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create an instance of the API interface using Retrofit
    private val service: CoinPaprikaApiService by lazy {
        retrofit.create(CoinPaprikaApiService::class.java)
    }

    override suspend fun getCoins(): List<CoinPaprikaCoin> {
        // Optionally use preferences here (e.g., for caching or logging)
        return service.getCoins()
    }
}