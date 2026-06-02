package com.imdumb.domain.usecases

import com.imdumb.domain.entities.Category
import com.imdumb.domain.entities.Movie
import com.imdumb.domain.repositories.MovieRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

data class CategoryWithMovies(
    val category: Category,
    val movies: List<Movie>
)

class GetCategoriesWithMoviesUseCase @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase
) {

    operator fun invoke(): Single<List<CategoryWithMovies>> {
        return getCategoriesUseCase()
            .flatMap { categories ->
                val observables = categories.map { category ->
                    getMoviesByCategoryUseCase(category.id)
                        .map { movies -> CategoryWithMovies(category, movies) }
                }
                Single.zip(observables) { results ->
                    results.map { it as CategoryWithMovies }
                }
            }
    }
}