package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import kotlin.Unit

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<PostViewModel>()
    private lateinit var newPostLauncher: ActivityResultLauncher<Unit>
    private lateinit var editPostLauncher: ActivityResultLauncher<Post>

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
        val adapter = PostAdapter(object : PostListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post)
            }

            override fun likedById(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onVideoClick(videoUrl: String) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Не удалось открыть видео",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        binding.container.layoutManager = LinearLayoutManager(this)
        binding.container.adapter = adapter

        viewModel.get().observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {

            } else {

            }
        }
        newPostLauncher = registerForActivityResult(NewPostContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }
        editPostLauncher = registerForActivityResult(EditPostContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }

        binding.add.setOnClickListener {
            newPostLauncher.launch(Unit)
        }
    }
}
