package app.unicornapp.unicorncrm.movielist.data.local

import androidx.room.Upsert

interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: )
}