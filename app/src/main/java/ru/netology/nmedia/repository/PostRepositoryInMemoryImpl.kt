package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImp : PostRepository {
    private val defaultPosts = List(100) { counter ->
        Post(
            id = counter + 1L,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Post #${counter} Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "22 мая в 18:36",
            countOfLikes = 0,  // Изменено с 1 на 0
            countOfShare = 100,
            countOfVisibility = 1_300_000
        )
    }

    private val data = MutableLiveData<List<Post>>(defaultPosts)

    override fun get(): LiveData<List<Post>> = data

    override fun likedById(id: Long) {
        val posts = data.value.orEmpty()

        data.value = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    countOfLikes = if (post.likedByMe) post.countOfLikes - 1 else post.countOfLikes + 1
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
}
