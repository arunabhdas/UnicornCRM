package app.unicornapp.unicorncrm.movielist.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.unicornapp.unicorncrm.movielist.domain.repository.MovieListRepository
import app.unicornapp.unicorncrm.movielist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private var savedStateHandle: SavedStateHandle
): ViewModel() {
    private val movieId = savedStateHandle.get<Int>("movieId")

    // Add state for movie details
    private val _movieDetailState = MutableStateFlow(MovieDetailState())
    val movieDetailState = _movieDetailState.asStateFlow()

    init {
        getMovieById(movieId ?: -1)
    }

    fun getMovieById(id: Int) {
        viewModelScope.launch {
            _movieDetailState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovie(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieDetailState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is Resource.Loading -> {
                        _movieDetailState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _movieDetailState.update {
                                it.copy(
                                    movie = movie
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}