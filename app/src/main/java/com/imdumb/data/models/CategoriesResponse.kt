package com.imdumb.data.models

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("genres")
    val genres: List<CategoryDto>
)

data class CategoryDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)


