package com.imdumb.presentation.adapters

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imdumb.databinding.ItemImagePagerBinding
import com.imdumb.domain.usecases.MovieImage

class ImagePagerAdapter(
    private val images: List<MovieImage>
) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImagePagerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class ImageViewHolder(
        private val binding: ItemImagePagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: MovieImage) {
            Glide.with(binding.root.context)
                .load(image.url)
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.drawable.ic_menu_report_image)
                .into(binding.ivImage)
        }
    }
}