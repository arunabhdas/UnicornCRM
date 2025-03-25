package app.unicornapp.unicorncrm.movielist.data.local

import androidx.room.Query
import androidx.room.Upsert

interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity


}