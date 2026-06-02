package com.imdumb.domain.repositories

import com.imdumb.domain.entities.Category
import com.imdumb.domain.entities.Movie
import com.imdumb.domain.entities.MovieDetail
import io.reactivex.Single

interface MovieRepository {
    fun getCategories(): Single<List<Category>>
    fun getMoviesByCategory(categoryId: Int): Single<List<Movie>>
    fun getMovieDetails(movieId: Int): Single<MovieDetail>
}