package app.unicornapp.unicorncrm.movielist.presentation

sealed interface MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent
    object Navigate : MovieListUiEvent
}