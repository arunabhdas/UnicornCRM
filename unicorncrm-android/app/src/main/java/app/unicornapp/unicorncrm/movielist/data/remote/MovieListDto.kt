package app.unicornapp.unicorncrm.movielist.data.remote

data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)