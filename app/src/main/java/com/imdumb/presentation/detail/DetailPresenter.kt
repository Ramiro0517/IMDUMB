package com.imdumb.presentation.detail

import android.util.Log
import com.imdumb.data.cache.ConfigCache
import com.imdumb.domain.entities.MovieDetail
import com.imdumb.domain.usecases.GetMovieDetailsUseCase
import com.imdumb.domain.usecases.GetMovieImagesUseCase
import com.imdumb.domain.usecases.MovieImage
import com.imdumb.domain.usecases.Recommendation
import com.imdumb.domain.usecases.SaveRecommendationUseCase
import com.imdumb.domain.usecases.ValidateRecommendationUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailPresenter @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val saveRecommendationUseCase: SaveRecommendationUseCase,
    private val validateRecommendationUseCase: ValidateRecommendationUseCase,
    private val configCache: ConfigCache
) {

    private var view: DetailContract.View? = null
    private val disposables = CompositeDisposable()
    private var currentMovieId: Int = -1
    private var currentMovieTitle: String = ""

    fun attachView(view: DetailContract.View) {
        this.view = view
    }

    fun detachView() {
        disposables.clear()
        this.view = null
    }

    fun loadMovieDetails(movieId: Int) {
        currentMovieId = movieId
        view?.showLoading(true)

        disposables.add(
            getMovieDetailsUseCase(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movieDetail ->
                        currentMovieTitle = movieDetail.title
                        view?.showLoading(false)
                        view?.showMovieDetails(movieDetail)

                        // Cargar imágenes para el carrusel
                        loadMovieImages(movieDetail.posterPath, movieDetail.backdropPath)
                    },
                    { error ->

                        view?.showLoading(false)
                        view?.showError(error.message ?: "Error loading movie details")
                    }
                )
        )
    }

    private fun loadMovieImages(posterPath: String?, backdropPath: String?) {

        disposables.add(
            getMovieImagesUseCase(posterPath, backdropPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { images ->
                        view?.showImages(images)
                    },
                    { error ->
                        view?.showImages(emptyList())
                    }
                )
        )
    }

    fun sendRecommendation(comment: String) {
        view?.showLoading(true)

        disposables.add(
            validateRecommendationUseCase(comment)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { isValid ->
                        if (isValid) {
                            saveRecommendation(comment)
                        } else {
                            view?.showLoading(false)
                            view?.showError("El comentario debe tener entre 5 y 500 caracteres")
                        }
                    },
                    { error ->
                        view?.showLoading(false)
                        view?.showError(error.message ?: "Error al validar el comentario")
                    }
                )
        )
    }

    private fun saveRecommendation(comment: String) {
        val recommendation = Recommendation(
            movieId = currentMovieId,
            movieTitle = currentMovieTitle,
            comment = comment,
            timestamp = System.currentTimeMillis()
        )

        disposables.add(
            saveRecommendationUseCase(recommendation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        view?.showLoading(false)
                        view?.showRecommendationSuccess()
                    },
                    { error ->
                        view?.showLoading(false)
                        view?.showError(error.message ?: "Error al guardar la recomendación")
                    }
                )
        )
    }
}

interface DetailContract {
    interface View {
        fun showLoading(show: Boolean)
        fun showMovieDetails(detail: MovieDetail)
        fun showImages(images: List<MovieImage>)
        fun showRecommendationSuccess()
        fun showError(message: String)
    }
}