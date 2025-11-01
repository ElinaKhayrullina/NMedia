package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import ru.netology.nmedia.utils.AndroidUtils

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

        val adapter = PostAdapter(object : PostListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun likedById(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })

        binding.container.layoutManager = LinearLayoutManager(this)
        binding.container.adapter = adapter

        viewModel.get().observe(this) { posts ->
            adapter.submitList(posts)
        }
        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                binding.editControls.visibility = View.GONE
                binding.cancel.visibility = View.GONE
                binding.editContent.setText("")
            } else {
                binding.editControls.visibility = View.VISIBLE
                binding.cancel.visibility = View.VISIBLE
                with(binding.editContent) {
                    setText(post.content)
                    AndroidUtils.showKeyboard(this)
                }
            }
        }

        binding.save.setOnClickListener {
            val currentText = binding.editContent.text?.trim().toString()
            if (currentText.isBlank()) {
                Toast.makeText(this, "Post content is empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.save(currentText)

            binding.editContent.setText("")
            binding.editContent.clearFocus()
            AndroidUtils.hideKeyBoard(binding.editContent)
        }
        binding.cancel.setOnClickListener {
            viewModel.cancelEdit()
            binding.editContent.setText("")
            binding.editContent.clearFocus()
            AndroidUtils.hideKeyBoard(binding.editContent)
        }
    }
}
