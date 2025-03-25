package app.unicornapp.unicorncrm.movielist.data.model

data class MovieApiResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)