package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFiles(private val context: Context) : PostRepository {

    private var defaultPosts = readPosts()
        set(value) {
            field = value
            sync()
        }

    private val data = MutableLiveData(defaultPosts)
    private var nextId = (defaultPosts.maxByOrNull { it.id }?.id ?: 0L) + 1

    override fun get(): LiveData<List<Post>> = data

    override fun likedById(id: Long) {
        val posts = data.value.orEmpty()
        data.value = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    countOfLikes = if (post.likedByMe) {
                        post.countOfLikes - 1
                    } else post.countOfLikes + 1
                )
            } else {
                post
            }
        }
    }

    override fun shareById(id: Long) {
        val posts = data.value.orEmpty()
        data.value = posts.map { post ->
            if (post.id == id) {
                post.copy(countOfShare = post.countOfShare + 1)
            } else {
                post
            }
        }
    }

    override fun removeById(id: Long) {
        val currentPosts = data.value.orEmpty()
        val newList = currentPosts.filter { it.id != id }
        data.value = newList
        defaultPosts = newList
    }


    override fun save(post: Post) {
        if (post.id == 0L) {
            val newPost = post.copy(
                id = nextId++,
                author = "Me",
                likedByMe = false,
                published = "now"
            )
            val newList = listOf(newPost) + data.value.orEmpty()
            data.value = newList
            defaultPosts = newList
            return
        }
        val newList = data.value.orEmpty().map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = newList
        defaultPosts = newList
    }

    private fun sync() {
        context.openFileOutput(KEY_FILE, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(defaultPosts))
        }
    }

    private fun readPosts(): List<Post> {
        val file = context.filesDir.resolve(KEY_FILE)
        return if (file.exists()) {
            context.openFileInput(KEY_FILE).bufferedReader().use {
                gson.fromJson(it, type)
            }
        } else {
            emptyList()
        }
    }

    private fun writePosts(posts: List<Post>) {
        val file = context.filesDir.resolve(KEY_FILE)
        file.writeText(gson.toJson(posts))
    }

    companion object {
        private const val KEY_FILE = "posts.json"

        private val gson = Gson()
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}

