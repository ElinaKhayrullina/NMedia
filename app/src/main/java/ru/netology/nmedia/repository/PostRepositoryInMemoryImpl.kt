package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImp : PostRepository {

    private val defaultPost = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        countOfLikes = 1,
        countOfShare = 100,
        countOfVisibility = 1_300_000
    )

    private val data = MutableLiveData(defaultPost)

    override fun get(): LiveData<Post> = data

    override fun like() {
        val post = requireNotNull(data.value) {
            "post not initialized"
        }
        data.value = post.copy(
            likedByMe = !post.likedByMe,
            countOfLikes = if (post.likedByMe) post.countOfLikes - 1 else post.countOfLikes + 1
        )
    }

    override fun share() {
        val post = requireNotNull(data.value) {
            "post not initialized"
        }
        data.value = post.copy(
            countOfShare = post.countOfShare + 1
        )
    }
}

