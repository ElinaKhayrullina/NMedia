package ru.netology.nmedia.viewmodel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImp
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImp()

    fun get(): LiveData<List<Post>> = repository.get()

    fun likedById(id: Long) {
        repository.likedById(id)
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    fun likeById(id: Long) = repository.likedById(id)

}
