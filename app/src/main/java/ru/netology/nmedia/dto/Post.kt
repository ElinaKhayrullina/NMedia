package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val countOfLikes: Int,
    val countOfShare: Int,
    val countOfVisibility: Int = 100,
    val likedByMe: Boolean,
    val videoUrl: String = ""
)