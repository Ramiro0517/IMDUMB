package com.imdumb.domain.usecases

import io.reactivex.Single
import javax.inject.Inject

class ValidateRecommendationUseCase @Inject constructor() {

    operator fun invoke(comment: String): Single<Boolean> {
        return Single.fromCallable {
            when {
                comment.isBlank() -> false
                comment.length < 5 -> false
                comment.length > 500 -> false
                else -> true
            }
        }
    }
}