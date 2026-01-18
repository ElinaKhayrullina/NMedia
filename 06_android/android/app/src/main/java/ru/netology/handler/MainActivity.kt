package ru.netology.handler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.netology.handler.databinding.ActivityMainBinding
import ru.netology.handler.databinding.CardPostBinding
import ru.netology.handler.dto.Post
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = PostAdapter()
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)

        val posts = listOf(
            Post(
                id = 1,
                author = "Нетология. Университет интернет-профессий будущего",
                authorAvatar = "netology.jpg",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. И вот, спустя время, мы создали университет с живыми, интерактивными курсами, где можно получать ответы на вопросы от преподавателей.",
                published = System.currentTimeMillis() - 3600000,
                likedByMe = false,
                likes = 999
            ),
            Post(
                id = 2,
                author = "Сбер",
                authorAvatar = "sber.jpg",
                content = "Экосистема цифровых продуктов и сервисов Сбера развивается стремительными темпами. Мы создаем удобные решения для жизни, работы и бизнеса наших клиентов.",
                published = System.currentTimeMillis() - 7200000,
                likedByMe = true,
                likes = 1500
            ),
            Post(
                id = 3,
                author = "Тинькофф",
                authorAvatar = "tcs.jpg",
                content = "Мобильный банк, который всегда под рукой. Переводы, платежи, инвестиции — все в одном приложении. Присоединяйтесь к миллионам довольных клиентов!",
                published = System.currentTimeMillis() - 10800000,
                likedByMe = false,
                likes = 2100
            )
        )

        adapter.submitList(posts)
    }
}

class PostAdapter : RecyclerView.Adapter<PostViewHolder>() {
    private var posts = mutableListOf<Post>()

    fun submitList(newPosts: List<Post>) {
        posts = newPosts.toMutableList()
        notifyDataSetChanged()
    }

    fun updatePost(position: Int, updatedPost: Post) {
        posts[position] = updatedPost
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding) { position ->
            val post = posts[position]
            val updatedPost = post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
            )
            updatePost(position, updatedPost)
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeClick: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            content.text = post.content
            published.text = formatDate(post.published)

            val avatarUrl = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
            Glide.with(avatar.context)
                .load(avatarUrl)
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_loading_100dp)
                .error(R.drawable.ic_error_100dp)
                .timeout(10_000)
                .into(avatar)

            like.setImageResource(
                if (post.likedByMe) R.drawable.baseline_favorite_24
                else R.drawable.baseline_favorite_border_24
            )

            likeCount.text = if (post.likes > 0) formatCount(post.likes) else ""

            like.setOnClickListener {
                onLikeClick(bindingAdapterPosition)
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun formatCount(count: Int): String {
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
