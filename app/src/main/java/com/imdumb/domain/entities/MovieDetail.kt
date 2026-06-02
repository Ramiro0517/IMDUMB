package com.imdumb.domain.entities

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val genres: List<String>,
    val actors: List<String>
)