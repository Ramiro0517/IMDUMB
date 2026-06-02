package com.imdumb.domain.usecases

import com.imdumb.domain.entities.Movie
import com.imdumb.domain.repositories.MovieRepository
import io.reactivex.Single
import javax.inject.Inject

class GetMoviesByCategoryUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(categoryId: Int): Single<List<Movie>> {
        return repository.getMoviesByCategory(categoryId)
    }
}