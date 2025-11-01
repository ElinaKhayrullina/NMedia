package ru.netology.nmedia.adapter

import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: PostListener
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
                listener.likedById(post)
            }

            share.setOnClickListener {
                listener.onShare(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                listener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
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
