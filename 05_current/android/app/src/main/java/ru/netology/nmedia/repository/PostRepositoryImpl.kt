package ru.netology.nmedia.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {
    override fun getAllAsync(callback: PostRepository.GetAllCallback) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                    return
                }

                callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
            }
        })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.PostCallback) {
        PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                    return
                }

                val post = response.body()
                if (post != null) {
                    callback.onSuccess(post)
                } else {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
            }
        })
    }

    override fun dislikeByIdAsync(id: Long, callback: PostRepository.PostCallback) {
        PostsApi.retrofitService.dislikeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                    return
                }

                val post = response.body()
                if (post != null) {
                    callback.onSuccess(post)
                } else {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
            }
        })
    }

    override fun saveAsync(post: Post, callback: PostRepository.PostCallback) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                    return
                }

                val savedPost = response.body()
                if (savedPost != null) {
                    callback.onSuccess(savedPost)
                } else {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
            }
        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.PostCallback) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                    return
                }

                callback.onSuccess(
                    Post(
                        id = id,
                        author = "",
                        authorAvatar = "",
                        content = "",
                        published = "",
                        likedByMe = false,
                        likes = 0
                    )
                )
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
            }
        })
    }

    override fun shareByIdAsync(id: Long, callback: PostRepository.PostCallback) {
        PostsApi.retrofitService.shareById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                    return
                }

                val post = response.body()
                if (post != null) {
                    callback.onSuccess(post)
                } else {
                    callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(Exception("Что-то пошло не так, повторите запрос позже"))
            }
        })
    }

    override fun getAll(): List<Post> {
        return emptyList()
    }

    override fun likeById(id: Long) {
    }

    override fun save(post: Post) {
    }

    override fun removeById(id: Long) {
    }
}
