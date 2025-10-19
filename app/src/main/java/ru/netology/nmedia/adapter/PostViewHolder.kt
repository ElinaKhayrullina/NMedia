package ru.netology.nmedia.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val likeClickListener: LikeClickListener,
    private val shareClickListener: (Post) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            if (post.countOfLikes > 0) {
                countOfLikes.text = formatNumbers(post.countOfLikes)
                countOfLikes.visibility = View.VISIBLE
            } else {
                countOfLikes.visibility = View.GONE
            }

            countOfShare.text = formatNumbers(post.countOfShare)
            countOfVisibility.text = formatNumbers(post.countOfVisibility)

            if (post.likedByMe) {
                favorite.setImageResource(R.drawable.baseline_favourite_24)
            } else {
                favorite.setImageResource(R.drawable.baseline_favorite_border_24)
            }

            favorite.setOnClickListener {
                likeClickListener(post)
            }

            share.setOnClickListener {
                shareClickListener(post)
            }
        }
    }

    private fun formatNumbers(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10000 -> {
                if ((count % 1000) / 100 > 0) {
                    "${count / 1000}.${(count % 1000) / 100}K"
                } else {
                    "${count / 1000}K"
                }
            }

            count < 1000000 -> "${count / 1000}K"
            else -> {
                if ((count % 1000000) / 100000 > 0) {
                    "${count / 1000000}.${(count % 1000000) / 100000}M"
                } else {
                    "${count / 1000000}M"
                }
            }
        }
    }
}
