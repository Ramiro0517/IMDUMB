package com.imdumb.domain.usecases

import io.reactivex.Single
import javax.inject.Inject

data class MovieImage(
    val url: String,
    val type: String
)

class GetMovieImagesUseCase @Inject constructor() {

    operator fun invoke(
        posterPath: String?,
        backdropPath: String?,
        additionalImages: List<String> = emptyList()
    ): Single<List<MovieImage>> {
        return Single.fromCallable {
            val images = mutableListOf<MovieImage>()

            posterPath?.let {
                images.add(MovieImage(
                    url = "https://image.tmdb.org/t/p/w500$it",
                    type = "poster"
                ))
            }

            backdropPath?.let {
                images.add(MovieImage(
                    url = "https://image.tmdb.org/t/p/w780$it",
                    type = "backdrop"
                ))
            }

            images.addAll(additionalImages.map { url ->
                MovieImage(url, "additional")
            })

            images
        }
    }
}