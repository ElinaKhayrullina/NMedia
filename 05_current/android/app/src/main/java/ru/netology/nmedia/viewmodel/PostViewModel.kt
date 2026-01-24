package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    author = "",
    authorAvatar = "",
    content = "",
    published = "",
    likedByMe = false,
    likes = 0
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    private val _errorEvent = SingleLiveEvent<String>()
    val errorEvent: LiveData<String>
        get() = _errorEvent

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
                _errorEvent.postValue(e.message ?: "Что-то пошло не так, повторите запрос позже")
            }
        })
    }

    fun save() {
        edited.value?.let { post ->
            _data.value = _data.value?.copy(loading = true)
            repository.saveAsync(post, object : PostRepository.PostCallback {
                override fun onSuccess(savedPost: Post) {
                    _postCreated.postValue(Unit)
                    val currentPosts = _data.value?.posts.orEmpty().toMutableList()
                    val existingIndex = currentPosts.indexOfFirst { it.id == savedPost.id }
                    if (existingIndex != -1) {
                        currentPosts[existingIndex] = savedPost
                    } else {
                        currentPosts.add(0, savedPost)
                    }
                    _data.postValue(FeedModel(posts = currentPosts, empty = currentPosts.isEmpty()))
                }

                override fun onError(e: Exception) {
                    _data.postValue(_data.value?.copy(loading = false, error = true))
                    _errorEvent.postValue(
                        e.message ?: "Что-то пошло не так, повторите запрос позже"
                    )
                }
            })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        val post = old.find { it.id == id } ?: return
        val wasLiked = post.likedByMe

        val updatedPosts = old.map { currentPost ->
            if (currentPost.id == id) {
                currentPost.copy(
                    likedByMe = !currentPost.likedByMe,
                    likes = if (currentPost.likedByMe) currentPost.likes - 1 else currentPost.likes + 1
                )
            } else {
                currentPost
            }
        }
        _data.postValue(_data.value?.copy(posts = updatedPosts))

        val callback = object : PostRepository.PostCallback {
            override fun onSuccess(post: Post) {
                val currentPosts = _data.value?.posts.orEmpty().map { currentPost ->
                    if (currentPost.id == post.id) post else currentPost
                }
                _data.postValue(_data.value?.copy(posts = currentPosts))
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old, error = true))
                _errorEvent.postValue(e.message ?: "Что-то пошло не так, повторите запрос позже")
            }
        }

        if (wasLiked) {
            repository.dislikeByIdAsync(id, callback)
        } else {
            repository.likeByIdAsync(id, callback)
        }
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        val updatedPosts = old.filter { it.id != id }
        _data.postValue(_data.value?.copy(posts = updatedPosts))

        repository.removeByIdAsync(id, object : PostRepository.PostCallback {
            override fun onSuccess(post: Post) {
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old, error = true))
                _errorEvent.postValue(e.message ?: "Что-то пошло не так, повторите запрос позже")
            }
        })
    }

    fun shareById(id: Long) {
        repository.shareByIdAsync(id, object : PostRepository.PostCallback {
            override fun onSuccess(post: Post) {
                val currentPosts = _data.value?.posts.orEmpty().map { currentPost ->
                    if (currentPost.id == post.id) post else currentPost
                }
                _data.postValue(_data.value?.copy(posts = currentPosts))
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(error = true))
                _errorEvent.postValue(e.message ?: "Что-то пошло не так, повторите запрос позже")
            }
        })
    }
}
