package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
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

        val adapter = PostAdapter(
            likeClickListener = { post ->
                viewModel.likedById(post.id)
            },
            shareClickListener = { post ->
                viewModel.shareById(post.id)
            }
        )

        binding.container.layoutManager = LinearLayoutManager(this)
        binding.container.adapter = adapter

        viewModel.get().observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}
