package ru.netology.nmedia.adapter

import ru.netology.nmedia.dto.Post

interface PostListener {
    fun onEdit(post: Post)
    fun likedById(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onVideoClick(videoUrl: String)
}