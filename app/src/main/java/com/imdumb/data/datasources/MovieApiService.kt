package com.imdumb.data.datasources

import com.imdumb.data.models.CategoriesResponse
import com.imdumb.data.models.MovieDetailResponse
import com.imdumb.data.models.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("genre/movie/list")
    fun getCategories(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Single<CategoriesResponse>

    @GET("discover/movie")
    fun getMoviesByCategory(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("language") language: String = "es-ES"
    ): Single<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES",
        @Query("append_to_response") append: String = "credits"
    ): Single<MovieDetailResponse>
}