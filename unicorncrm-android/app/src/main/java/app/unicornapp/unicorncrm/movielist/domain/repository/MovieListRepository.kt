package app.unicornapp.unicorncrm.movielist.domain.repository

import app.unicornapp.unicorncrm.movielist.util.Resource
import kotlinx.coroutines.flow.Flow
import app.unicornapp.unicorncrm.movielist.domain.model.Movie

interface MovieListRepository {
    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>
}