package com.imdumb.presentation.detail

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.imdumb.presentation.adapters.ActorsAdapter
import com.imdumb.presentation.adapters.ImagePagerAdapter
import com.imdumb.databinding.ActivityDetailBinding
import com.imdumb.domain.entities.MovieDetail
import com.imdumb.domain.usecases.MovieImage

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), DetailContract.View {

    @Inject lateinit var presenter: DetailPresenter

    private lateinit var binding: ActivityDetailBinding
    private var isImagesLoaded = false
    private var isDetailsLoaded = false

    companion object {
        private const val EXTRA_MOVIE_ID = "movie_id"

        fun newIntent(context: android.content.Context, movieId: Int) =
            android.content.Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)

        presenter.attachView(this)
        presenter.loadMovieDetails(movieId)

        binding.btnRecommend.setOnClickListener {
            showRecommendationDialog()
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.GONE
        }
    }

    override fun showMovieDetails(detail: MovieDetail) {

        binding.tvTitle.text = detail.title.ifEmpty { "Título no disponible" }
        binding.tvRating.text = if (detail.voteAverage > 0) "${detail.voteAverage}/10 ⭐" else "Sin calificación"

        binding.tvDescription.text = if (detail.overview.isNotEmpty()) {
            Html.fromHtml(detail.overview, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml("No hay descripción disponible")
        }

        setupActorsRecycler(detail.actors)

        binding.progressBar.visibility = android.view.View.GONE
        isDetailsLoaded = true
        checkAndHideLoading()
    }

    override fun showImages(images: List<MovieImage>) {

        if (images.isNotEmpty()) {
            val imageAdapter = ImagePagerAdapter(images)
            binding.viewPagerImages.adapter = imageAdapter
        }
        isImagesLoaded = true
        checkAndHideLoading()
    }

    private fun checkAndHideLoading() {
        if (isDetailsLoaded && isImagesLoaded) {
            binding.progressBar.visibility = android.view.View.GONE

        } else {

        }
    }

    private fun setupActorsRecycler(actors: List<String>) {

        if (actors.isNotEmpty()) {
            val actorsAdapter = ActorsAdapter(actors)
            binding.rvActors.adapter = actorsAdapter
        } else {
            val emptyList = listOf("No hay información de actores disponible")
            val actorsAdapter = ActorsAdapter(emptyList)
            binding.rvActors.adapter = actorsAdapter
        }
    }

    private fun showRecommendationDialog() {
        val dialog = RecommendationBottomSheet.newInstance(
            movieTitle = binding.tvTitle.text.toString()
        )
        dialog.setOnRecommendListener { comment ->

            presenter.sendRecommendation(comment)
        }
        dialog.show(supportFragmentManager, "recommendation_dialog")
    }

    override fun showRecommendationSuccess() {
        Toast.makeText(this, "Película recomendada", Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        binding.progressBar.visibility = android.view.View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}