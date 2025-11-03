package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.utils.AndroidUtils

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!text.isNullOrBlank()) {
            binding.edit.setText(text)
            binding.edit.setSelection(text.length)
        }

        AndroidUtils.showKeyboard(binding.edit)
        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val intent = Intent().apply { putExtra(Intent.EXTRA_TEXT, text) }
                setResult(RESULT_OK, intent)
            }
            finish()
        }
    }
}

object NewPostContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit) =
        Intent(context, NewPostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)
}

object EditPostContract : ActivityResultContract<Post, String?>() {
    override fun createIntent(context: Context, input: Post) =
        Intent(context, NewPostActivity::class.java).apply {
            putExtra(Intent.EXTRA_TEXT, input.content)
        }

    override fun parseResult(resultCode: Int, intent: Intent?) =
        intent?.getStringExtra(Intent.EXTRA_TEXT)
}


