package app.unicornapp.unicorncrm.movielist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.unicornapp.unicorncrm.movielist.domain.repository.MovieListRepository
import app.unicornapp.unicorncrm.movielist.util.Category
import app.unicornapp.unicorncrm.movielist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    // Add state for movie details
    private val _movieDetailState = MutableStateFlow(MovieDetailState())
    val movieDetailState = _movieDetailState.asStateFlow()

    init {
        getPopularMovieList(true)
        getUpcomingMovieList(true)
    }

    fun refreshMovies() {
        getPopularMovieList(true)
    }

    fun onEvent(event: MovieListUiEvent) {
       when(event) {
           MovieListUiEvent.Navigate -> {
               _movieListState.update {
                   it.copy(
                       isCurrentScreenPopular = !movieListState.value.isCurrentScreenPopular
                   )
               }
           }
           is MovieListUiEvent.Paginate -> {
               if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)
               } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovieList(true)
               }
           }
       }
    }

    fun getMovieById(id: Int) {
        viewModelScope.launch {
            movieListRepository.getMovie(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _movieDetailState.value = MovieDetailState(isLoading = result.isLoading)
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _movieDetailState.value = MovieDetailState(movie = movie)
                        }
                    }
                    is Resource.Error -> {
                        _movieDetailState.value = MovieDetailState(
                            error = result.message ?: "Unknown error occurred"
                        )
                    }
                }
            }
        }
    }
    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList + popularList,
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                movieListState.value.upcomingMovieListPage
            ).collectLatest { result ->
                when(result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList + upcomingList,
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
            }
        }
    }


}