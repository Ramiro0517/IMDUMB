package com.imdumb.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.imdumb.databinding.FragmentRecommendationBottomSheetBinding

class RecommendationBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRecommendationBottomSheetBinding
    private var onRecommendListener: ((String) -> Unit)? = null


    companion object {
        private const val ARG_MOVIE_TITLE = "movie_title"

        fun newInstance(movieTitle: String): RecommendationBottomSheet {
            return RecommendationBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_MOVIE_TITLE, movieTitle)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieTitle = arguments?.getString(ARG_MOVIE_TITLE) ?: ""
        binding.tvMovieTitle.text = movieTitle

        binding.btnSendRecommendation.setOnClickListener {
            val comment = binding.etComment.text.toString()
            if (comment.isNotBlank()) {
                onRecommendListener?.invoke(comment)
                dismiss()
            } else {
                Toast.makeText(context, "Por favor escribe un comentario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setOnRecommendListener(listener: (String) -> Unit) {
        onRecommendListener = listener
    }
}