package app.unicornapp.unicorncrm

import app.unicornapp.unicorncrm.api.CoinPaprikaApiService
import app.unicornapp.unicorncrm.api.CoinPaprikaApiServiceImpl
import app.unicornapp.unicorncrm.api.UnicornApi
import app.unicornapp.unicorncrm.presentation.CoinRepository
import app.unicornapp.unicorncrm.presentation.MainRepository
import app.unicornapp.unicorncrm.presentation.CoinViewModel
import app.unicornapp.unicorncrm.presentation.MainViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {

    // Provide Gson
    single { Gson() }

    // Provide Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://api.coinpaprika.com/")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }

    // Provide UnicornApi
    single {
        get<Retrofit>().create(UnicornApi::class.java)
    }

    // Provide the API service using our custom implementation
    single<CoinPaprikaApiService> { CoinPaprikaApiServiceImpl(get()) }

    // Provide the Repository
    single { CoinRepository(get()) }

    // Provide the ViewModel
    viewModel {
        MainViewModel()
    }

    viewModel { CoinViewModel(get()) }

}