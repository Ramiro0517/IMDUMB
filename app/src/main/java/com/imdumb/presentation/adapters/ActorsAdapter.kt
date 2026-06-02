package com.imdumb.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imdumb.databinding.ItemActorBinding

class ActorsAdapter(
    private val actors: List<String>
) : RecyclerView.Adapter<ActorsAdapter.ActorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val binding = ItemActorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    override fun getItemCount(): Int = actors.size

    class ActorViewHolder(
        private val binding: ItemActorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(actorName: String) {
            binding.tvActorName.text = actorName
        }
    }
}