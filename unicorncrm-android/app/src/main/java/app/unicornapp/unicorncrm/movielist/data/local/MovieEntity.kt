package app.unicornapp.unicorncrm.movielist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class MovieEntity(
    val adult: Boolean,
    val backdrop_path: String,
    @TypeConverters(IntListConverter::class)
    val genre_ids: List<Int>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,

    @PrimaryKey
    val id: Int,
    val category: String
)