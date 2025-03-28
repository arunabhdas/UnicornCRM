package app.unicornapp.unicorncrm.movielist.presentation

import app.unicornapp.unicorncrm.movielist.domain.model.Movie


data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String = ""
)