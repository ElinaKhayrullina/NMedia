package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)

    fun getAllAsync(callback: GetAllCallback)
    fun likeByIdAsync(id: Long, callback: PostCallback)
    fun saveAsync(post: Post, callback: PostCallback)
    fun removeByIdAsync(id: Long, callback: PostCallback)
    fun shareByIdAsync(id: Long, callback: PostCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    interface PostCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }
}
