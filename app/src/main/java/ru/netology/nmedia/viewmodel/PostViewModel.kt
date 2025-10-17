package ru.netology.nmedia.viewmodel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImp
import ru.netology.nmedia.dto.Post

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImp()

    fun get(): LiveData<Post> = repository.get()

    fun like() {
        repository.like()
    }

    fun share() {
        repository.share()
    }
}
