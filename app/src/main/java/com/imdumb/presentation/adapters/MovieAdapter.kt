package com.imdumb.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imdumb.databinding.ItemMovieBinding
import com.imdumb.domain.entities.Movie

class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: List<Movie> = emptyList()

    fun submitList(list: List<Movie>) {
        movies = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvMovieTitle.text = movie.title
            binding.tvRating.text = "⭐ ${movie.voteAverage}/10"

            // Cargar imagen del poster si existe
            movie.posterPath?.let {
                Glide.with(binding.root.context)
                    .load("https://image.tmdb.org/t/p/w200$it")
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(binding.ivPoster)
            } ?: run {
                // Imagen por defecto si no hay poster
                binding.ivPoster.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            // Año de lanzamiento
            if (movie.releaseDate.isNotEmpty()) {
                val year = movie.releaseDate.take(4)
                binding.tvYear.text = year
            } else {
                binding.tvYear.text = "Próximamente"
            }

            // Click listener
            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }
}