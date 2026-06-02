package com.imdumb.domain.usecases

import com.imdumb.domain.entities.Movie
import io.reactivex.Single
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor() {

    operator fun invoke(movies: List<Movie>, query: String): Single<List<Movie>> {
        return Single.fromCallable {
            if (query.isEmpty()) {
                movies
            } else {
                movies.filter { movie ->
                    movie.title.contains(query, ignoreCase = true) ||
                            movie.overview.contains(query, ignoreCase = true)
                }
            }
        }
    }
}