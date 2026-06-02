package com.imdumb.domain.usecases

import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import java.util.UUID
import javax.inject.Inject

data class Recommendation(
    val movieId: Int,
    val movieTitle: String,
    val comment: String,
    val timestamp: Long
)

class SaveRecommendationUseCase @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) {

    operator fun invoke(recommendation: Recommendation): Completable {
        return Completable.create { emitter ->
            val recommendationsRef = firebaseDatabase.getReference("recommendations")
            val id = UUID.randomUUID().toString()

            recommendationsRef.child(id).setValue(recommendation)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener { error ->
                    emitter.onError(error)
                }
        }
    }
}