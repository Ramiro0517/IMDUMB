package com.imdumb.domain.usecases

import com.imdumb.data.cache.CategoryCache
import com.imdumb.domain.entities.Category
import io.reactivex.Completable
import javax.inject.Inject



class CacheCategoriesUseCase @Inject constructor(
    private val cache: CategoryCache
) {

    operator fun invoke(categories: List<Category>): Completable {
        return Completable.fromAction {
            cache.saveCategories(categories)
        }
    }

    fun getCachedCategories(): List<Category>? {
        return cache.getCategories()
    }
}