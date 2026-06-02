package com.imdumb.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imdumb.databinding.ItemCategoryBinding
import com.imdumb.domain.usecases.CategoryWithMovies


class CategoryAdapter(
    private val onMovieClick: (Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var items: List<CategoryWithMovies> = emptyList()

    fun submitList(list: List<CategoryWithMovies>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onMovieClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var moviesAdapter: MovieAdapter

        fun bind(categoryWithMovies: CategoryWithMovies) {
            binding.tvCategoryTitle.text = categoryWithMovies.category.name

            moviesAdapter = MovieAdapter { movie ->
                onMovieClick(movie.id)
            }

            binding.rvMovies.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                    context,
                    androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = moviesAdapter
                setHasFixedSize(true)
            }

            moviesAdapter.submitList(categoryWithMovies.movies)
        }
    }
}