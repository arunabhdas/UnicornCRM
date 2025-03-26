package app.unicornapp.unicorncrm.movielist.data.repository

import app.unicornapp.unicorncrm.movielist.data.local.MovieDatabase
import app.unicornapp.unicorncrm.movielist.data.mappers.toMovie
import app.unicornapp.unicorncrm.movielist.data.mappers.toMovieEntity
import app.unicornapp.unicorncrm.movielist.data.remote.MovieApiService
import app.unicornapp.unicorncrm.movielist.domain.model.Movie
import app.unicornapp.unicorncrm.movielist.domain.repository.MovieListRepository
import app.unicornapp.unicorncrm.movielist.util.Resource
import coil.network.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
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

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
               e.printStackTrace()
               emit(Resource.Error(message = "Error loading movies"))
               return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let { it ->
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)

            emit(Resource.Success(
                movieEntities.map { it.toMovie(category)

                }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))

            val movieEntity = movieDatabase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(
                    Resource.Success(
                        movieEntity.toMovie(movieEntity.category)
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error(
                message = "None found"
            ))

            emit(Resource.Loading(false))
        }
    }
}