package app.unicornapp.unicorncrm

import app.unicornapp.unicorncrm.api.UnicornApi
import app.unicornapp.unicorncrm.presentation.MainRepository
import app.unicornapp.unicorncrm.presentation.MainRepositoryImpl
import app.unicornapp.unicorncrm.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("localhost:8000/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(UnicornApi::class.java)
    }

    single<MainRepository> {
        MainRepositoryImpl(get())
    }

    viewModel {
        MainViewModel(get())
    }
}