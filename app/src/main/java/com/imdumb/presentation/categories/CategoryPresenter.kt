package com.imdumb.presentation.categories


import com.imdumb.domain.usecases.CacheCategoriesUseCase
import com.imdumb.domain.usecases.CategoryWithMovies
import com.imdumb.domain.usecases.GetCategoriesWithMoviesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryPresenter @Inject constructor(
    private val getCategoriesWithMoviesUseCase: GetCategoriesWithMoviesUseCase,
    private val cacheCategoriesUseCase: CacheCategoriesUseCase
) {

    private var view: CategoryContract.View? = null
    private val disposables = CompositeDisposable()

    fun attachView(view: CategoryContract.View) {
        this.view = view
    }

    fun detachView() {
        disposables.clear()
        this.view = null
    }

    fun loadCategories() {
        view?.showLoading(true)


        val cachedCategories = cacheCategoriesUseCase.getCachedCategories()
        if (!cachedCategories.isNullOrEmpty()) {
            view?.showCategories(emptyList())
        }

        disposables.add(
            getCategoriesWithMoviesUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { categoriesWithMovies ->
                        view?.showLoading(false)
                        view?.showCategories(categoriesWithMovies)

                        // Cachear categorías
                        val categories = categoriesWithMovies.map { it.category }
                        cacheCategoriesUseCase(categories)
                            .subscribeOn(Schedulers.io())
                            .subscribe()
                    },
                    { error ->
                        view?.showLoading(false)
                        view?.showError(error.message ?: "Error loading categories")
                    }
                )
        )
    }

    fun onMovieClick(movieId: Int) {
        view?.navigateToDetail(movieId)
    }
}

interface CategoryContract {
    interface View {
        fun showLoading(show: Boolean)
        fun showCategories(categories: List<CategoryWithMovies>)
        fun showError(message: String)
        fun navigateToDetail(movieId: Int)
    }
}