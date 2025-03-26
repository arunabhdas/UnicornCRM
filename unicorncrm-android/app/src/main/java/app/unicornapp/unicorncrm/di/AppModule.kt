package app.unicornapp.unicorncrm.di

import android.app.Application
import androidx.room.Room
import app.unicornapp.unicorncrm.movielist.data.local.MovieDatabase
import app.unicornapp.unicorncrm.movielist.data.remote.MovieApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesMovieApiService(): MovieApiService {
       return Retrofit.Builder()
           .addConverterFactory(GsonConverterFactory.create())
           .baseUrl(MovieApiService.BASE_URL)
           .client(client)
           .build()
           .create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(
            application,
            MovieDatabase::class.java,
            "movies.db"
        ).build()
    }


}