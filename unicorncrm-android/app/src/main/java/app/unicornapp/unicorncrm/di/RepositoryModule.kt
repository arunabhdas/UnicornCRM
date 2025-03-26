package app.unicornapp.unicorncrm.di

import app.unicornapp.unicorncrm.movielist.data.repository.MovieListRepositoryImpl
import app.unicornapp.unicorncrm.movielist.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieListRepositoryImply: MovieListRepositoryImpl
    ): MovieListRepository


}