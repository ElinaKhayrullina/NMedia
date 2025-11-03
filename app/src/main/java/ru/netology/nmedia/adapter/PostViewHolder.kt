package ru.netology.nmedia.adapter

import android.view.View
import android.view.ViewGroup
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
        binding.apply {
            author.text = post.author
            published.text = post.published

            val rutubeLink = extractRutubeLink(post.content)
            val displayContent = if (rutubeLink != null) {
                post.content.replace(rutubeLink, "").trim()
            } else {
                post.content
            }
            content.text = displayContent

            if (rutubeLink == null) {
                videoContainer.visibility = ViewGroup.GONE
            } else {
                videoContainer.visibility = ViewGroup.VISIBLE
                videoThumbnail.setImageResource(R.drawable.video)
                videoThumbnail.setOnClickListener {
                    listener.onVideoClick(rutubeLink)
                }
                videoPlayButton.setOnClickListener {
                    listener.onVideoClick(rutubeLink)
                }
            }
            if (post.countOfLikes > 0) {
                favorite.text = formatNumbers(post.countOfLikes)
            } else {
                favorite.text = ""
            }
            favorite.isChecked = post.likedByMe
            if (post.countOfShare > 0) {
                share.text = formatNumbers(post.countOfShare)
            } else {
                share.text = ""
            }
            if (post.countOfVisibility > 0) {
                visibility.text = formatNumbers(post.countOfVisibility)
                visibility.visibility = View.VISIBLE
            } else {
                visibility.visibility = View.GONE
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
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
                    show()
                }
            }
            favorite.setOnClickListener {
                listener.likedById(post)
            }
            share.setOnClickListener {
                listener.onShare(post)
            }
        }
    }

    private fun extractRutubeLink(content: String): String? {
        val regex = "https://rutube\\.ru/video/[\\w\\-]+/?".toRegex()
        return regex.find(content)?.value
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
