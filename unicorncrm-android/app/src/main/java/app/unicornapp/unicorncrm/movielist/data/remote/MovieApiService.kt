package app.unicornapp.unicorncrm.movielist.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import app.unicornapp.unicorncrm.BuildConfig

interface MovieApiService {

    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = TMDB_ORG_API_KEY
    )

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
        val TMDB_ORG_API_KEY: String = BuildConfig.TMDB_ORG_API_KEY
    }
}