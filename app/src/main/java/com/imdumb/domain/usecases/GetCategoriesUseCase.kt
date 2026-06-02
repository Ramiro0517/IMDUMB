package com.imdumb.domain.usecases

import com.imdumb.domain.entities.Category
import com.imdumb.domain.repositories.MovieRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Single<List<Category>> = repository.getCategories()
}