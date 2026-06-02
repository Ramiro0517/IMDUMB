package com.imdumb.data.repositories

import com.imdumb.data.cache.CategoryCache
import com.imdumb.data.datasources.MovieApiService
import com.imdumb.data.models.*
import com.imdumb.domain.entities.*
import com.imdumb.domain.repositories.MovieRepository
import io.reactivex.Single
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val cache: CategoryCache,
    private val apiKey: String
) : MovieRepository {

    override fun getCategories(): Single<List<Category>> {
        return apiService.getCategories(apiKey)
            .map { response ->
                response.genres.map { it.toEntity() }
            }
            .doOnSuccess { categories ->
                cache.saveCategories(categories)
            }
    }

    override fun getMoviesByCategory(categoryId: Int): Single<List<Movie>> {
        return apiService.getMoviesByCategory(apiKey, categoryId)
            .map { response ->
                response.results.map { it.toEntity() }
            }
    }

    override fun getMovieDetails(movieId: Int): Single<MovieDetail> {
        return apiService.getMovieDetails(movieId, apiKey)
            .map { it.toEntity() }
    }
}

fun CategoryDto.toEntity() = Category(id, name)
fun MovieDto.toEntity() = Movie(id, title, overview, posterPath, voteAverage, releaseDate)
fun MovieDetailResponse.toEntity(): MovieDetail {
    val actorsList = this.credits?.cast?.map { it.name }?.take(10) ?: emptyList()
    return MovieDetail(
        id = this.id,
        title = this.title ?: "Sin título",
        overview = this.overview ?: "No hay descripción disponible",
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        voteAverage = this.voteAverage,
        genres = this.genres?.map { it.name } ?: emptyList(),
        actors = actorsList
    )
}