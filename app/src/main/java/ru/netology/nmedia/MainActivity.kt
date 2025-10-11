package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
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

        var post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            countOfLikes = 1,
            countOfShare = 100,
            countOfVisibility = 1_300_000
        )

        fun formatNumbers(count: Int): String {
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

        fun updateUi() {
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
                post = if (post.likedByMe) {
                    post.copy(
                        likedByMe = false,
                        countOfLikes = post.countOfLikes - 1
                    )
                } else {
                    post.copy(
                        likedByMe = true,
                        countOfLikes = post.countOfLikes + 1
                    )
                }
                updateUi()
            }
        }

        with(binding) {
            share.setOnClickListener {
                post = post.copy(
                    countOfShare = post.countOfShare + 1
                )
                updateUi()
            }
        }
        updateUi()
    }
}