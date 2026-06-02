package com.imdumb.presentation.categories

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.imdumb.R
import com.imdumb.databinding.ActivityMainBinding
import com.imdumb.domain.usecases.CategoryWithMovies
import com.imdumb.presentation.adapters.CategoryAdapter
import com.imdumb.presentation.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CategoryContract.View {

    @Inject
    lateinit var presenter: CategoryPresenter

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CategoryAdapter
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        presenter.attachView(this)
        presenter.loadCategories()
    }

    private fun setupRecyclerView() {
        adapter = CategoryAdapter { movieId ->
            presenter.onMovieClick(movieId)
        }

        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.GONE
        }
    }

    override fun showCategories(categories: List<CategoryWithMovies>) {
        adapter.submitList(categories)
    }

    override fun showError(message: String) {

    }

    override fun navigateToDetail(movieId: Int) {
        startActivity(DetailActivity.newIntent(this, movieId))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}