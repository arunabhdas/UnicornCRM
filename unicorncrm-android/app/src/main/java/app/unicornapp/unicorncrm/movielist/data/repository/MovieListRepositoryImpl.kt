package app.unicornapp.unicorncrm.movielist.data.repository

import app.unicornapp.unicorncrm.movielist.data.local.MovieDatabase
import app.unicornapp.unicorncrm.movielist.data.remote.MovieApiService
import app.unicornapp.unicorncrm.movielist.domain.model.Movie
import app.unicornapp.unicorncrm.movielist.domain.repository.MovieListRepository
import app.unicornapp.unicorncrm.movielist.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieListRepositoryImply @Inject constructor(
    private val movieApi: MovieApiService,
    private val movieDatabase: MovieDatabase
): MovieListRepository{
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isEmpty() && !forceFetchFromRemote


            // TODO-FIXME-COMPLETE-IMPLEMENTATION

        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        TODO("Not yet implemented")
    }
}