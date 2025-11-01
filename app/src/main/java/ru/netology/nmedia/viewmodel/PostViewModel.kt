package ru.netology.nmedia.viewmodel;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImp
import ru.netology.nmedia.dto.Post

private val empty = Post(
    id = 0L,
    author = "",
    content = "",
    published = "",
    likedByMe = false,
    countOfLikes = 0,
    countOfShare = 0
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImp()
    val edited = MutableLiveData(empty)

    fun get(): LiveData<List<Post>> = repository.get()

    fun likedById(id: Long) {
        repository.likedById(id)
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    fun likeById(id: Long) = repository.likedById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun save(currentText: String) {
        edited.value?.let {
            repository.save(it.copy(content = currentText))
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEdit() {
        edited.value = empty
    }
}
