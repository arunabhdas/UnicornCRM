package app.unicornapp.unicorncrm.movielist.presentation

import androidx.lifecycle.ViewModel
import app.unicornapp.unicorncrm.movielist.domain.repository.MovieListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
): ViewModel() {

}