package com.imdumb.domain.usecases

import com.imdumb.domain.entities.MovieDetail
import com.imdumb.domain.repositories.MovieRepository
import io.reactivex.Single
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Single<MovieDetail> {
        return repository.getMovieDetails(movieId)
    }
}