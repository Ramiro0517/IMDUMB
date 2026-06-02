package com.imdumb.data.models

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("genres")
    val genres: List<CategoryDto>? = emptyList(),
    @SerializedName("credits")
    val credits: CreditsDto? = null
)

data class CreditsDto(
    @SerializedName("cast")
    val cast: List<CastDto>
)

data class CastDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String?
)