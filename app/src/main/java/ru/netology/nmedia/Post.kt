package ru.netology.nmedia

data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val countOfLikes: Int = 0,
    val countOfShare: Int = 0,
    val countOfVisibility: Int = 0,
    var likedByMe: Boolean = false
)