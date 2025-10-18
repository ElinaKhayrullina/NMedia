package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val commonSpacing = resources.getDimensionPixelSize(R.dimen.common_spacing)
            v.setPadding(
                systemBars.left + commonSpacing,
                systemBars.top + commonSpacing,
                systemBars.right + commonSpacing,
                systemBars.bottom + commonSpacing
            )
            insets
        }

        viewModel.get().observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                countOfLikes.text = formatNumbers(post.countOfLikes)
                countOfShare.text = formatNumbers(post.countOfShare)
                countOfVisibility.text = formatNumbers(post.countOfVisibility)
                if (post.likedByMe) {
                    favorite.setImageResource(R.drawable.baseline_favourite_24)
                } else {
                    favorite.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }

        with(binding) {
            favorite.setOnClickListener {
                viewModel.like()
            }
            share.setOnClickListener {
                viewModel.share()
            }
        }
    }

    private fun formatNumbers(count: Int): String {
        return when {
            count < 10000 -> {
                if (count < 1000) {
                    count.toString()
                } else {
                    if (((count % 1000) / 100) > 0) {
                        "${count / 1000}.${(count % 1000) / 100}"
                    } else {
                        "${count / 1000}K"
                    }
                }
            }
            count < 1000000 -> {
                "${count / 1000}K"
            }
            else -> {
                if (((count % 1000000) / 100000) > 0) {
                    "${count / 1000000}.${(count % 1000000) / 100000}M"
                } else {
                    "${count / 1000000}M"
                }
            }
        }
    }
}