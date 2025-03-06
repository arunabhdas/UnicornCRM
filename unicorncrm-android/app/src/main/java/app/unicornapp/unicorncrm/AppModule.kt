package app.unicornapp.unicorncrm

import app.unicornapp.unicorncrm.api.CoinPaprikaApiService
import app.unicornapp.unicorncrm.api.UnicornApi
import app.unicornapp.unicorncrm.presentation.MainRepository
import app.unicornapp.unicorncrm.presentation.MainRepositoryImpl
import app.unicornapp.unicorncrm.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.prefs.Preferences

val appModule = module {

    // Provide Gson
    single { Gson() }

    single<Preferences> { PreferencesImpl() }

    /* TODO-FIXME-CLEANUP
    single {
        Retrofit.Builder()
            .baseUrl("http://localhost:8000/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UnicornApi::class.java)
    }
    */

    // Provide the API service using our custom implementation
    single<CoinPaprikaApiService> { CoinPaprikaApiServiceImpl(preferences = get()) }


    // Provide the CoinPaprikaApiService
    single { get<Retrofit>().create(CoinPaprikaApiService::class.java) }

    // Provide the Repository
    single { CoinRepository(get()) }

    // Provide the ViewModel
    viewModel { CoinViewModel(get()) }

    single<MainRepository> {
        MainRepositoryImpl(get())
    }

    viewModel {
        MainViewModel(get())
    }

}