package ru.netology.coroutines

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import ru.netology.coroutines.dto.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/*
fun main() {
    runBlocking {
        println(Thread.currentThread().name)
    }
}
*/

/*
fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        println(Thread.currentThread().name)
    }

    Thread.sleep(1000L)
}
*/

/*
fun main() {
    val custom = Executors.newFixedThreadPool(64).asCoroutineDispatcher()
    with(CoroutineScope(EmptyCoroutineContext)) {
        launch(Dispatchers.Default) {
            println(Thread.currentThread().name)
        }
        launch(Dispatchers.IO) {
            println(Thread.currentThread().name)
        }
        // will throw exception without UI
        // launch(Dispatchers.Main) {
        //    println(Thread.currentThread().name)
        // }

        launch(custom) {
            println(Thread.currentThread().name)
        }
    }
    Thread.sleep(1000L)
    custom.close()
}
*/

/*
private val gson = Gson()
private val BASE_URL = "http://127.0.0.1:9999"
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor(::println).apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

fun main() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            try {
                val posts = getPosts(client)
                    .map { post ->
                        PostWithComments(post, getComments(client, post.id))
                    }
                println(posts)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    Thread.sleep(30_000L)
}

suspend fun OkHttpClient.apiCall(url: String): Response {
    return suspendCoroutine { continuation ->
        Request.Builder()
            .url(url)
            .build()
            .let(::newCall)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
    }
}

suspend fun <T> makeRequest(url: String, client: OkHttpClient, typeToken: TypeToken<T>): T =
    withContext(Dispatchers.IO) {
        client.apiCall(url)
            .let { response ->
                if (!response.isSuccessful) {
                    response.close()
                    throw RuntimeException(response.message)
                }
                val body = response.body ?: throw RuntimeException("response body is null")
                gson.fromJson(body.string(), typeToken.type)
            }
    }

suspend fun getPosts(client: OkHttpClient): List<Post> =
    makeRequest("$BASE_URL/api/slow/posts", client, object : TypeToken<List<Post>>() {})

suspend fun getComments(client: OkHttpClient, id: Long): List<Comment> =
    makeRequest("$BASE_URL/api/slow/posts/$id/comments", client, object : TypeToken<List<Comment>>() {})
*/

private val gson = Gson()
private val BASE_URL = "http://127.0.0.1:9999"
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor(::println).apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

fun main() = runBlocking {
    try {
        val posts = getPosts(client)
            .map { post ->
                async {
                    val authorDeferred = async { getAuthor(client, post.authorId) }
                    val commentsDeferred = async { getComments(client, post.id) }
                    
                    val author = authorDeferred.await()
                    val comments = commentsDeferred.await()
                    
                    val postWithAuthor = PostWithAuthor(
                        id = post.id,
                        author = author,
                        content = post.content,
                        published = post.published,
                        likedByMe = post.likedByMe,
                        likes = post.likes
                    )
                    
                    val authorsComment = comments.map { async { getAuthor(client, it.authorId) } }.awaitAll()
                    val commentsWithAuthors = comments.zip(authorsComment) { comment, commentAuthor ->
                        CommentWithAuthor(
                            id = comment.id,
                            postId = comment.postId,
                            author = commentAuthor,
                            content = comment.content,
                            published = comment.published,
                            likedByMe = comment.likedByMe,
                            likes = comment.likes
                        )
                    }
                    
                    PostWithComments(postWithAuthor, commentsWithAuthors)
                }
            }.awaitAll()
        
        println("Посты с авторами:")
        posts.forEach { postWithComments ->
            println("Пост от ${postWithComments.post.author.name}: ${postWithComments.post.content}")
            postWithComments.comments.forEach { comment ->
                println("  Комментарий от ${comment.author.name}: ${comment.content}")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

suspend fun OkHttpClient.apiCall(url: String): Response {
    return suspendCoroutine { continuation ->
        Request.Builder()
            .url(url)
            .build()
            .let(::newCall)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
    }
}

suspend fun <T> makeRequest(url: String, client: OkHttpClient, typeToken: TypeToken<T>): T =
    withContext(Dispatchers.IO) {
        client.apiCall(url)
            .let { response ->
                if (!response.isSuccessful) {
                    response.close()
                    throw RuntimeException(response.message)
                }
                val body = response.body ?: throw RuntimeException("response body is null")
                gson.fromJson(body.string(), typeToken.type)
            }
    }

suspend fun getPosts(client: OkHttpClient): List<Post> =
    makeRequest("$BASE_URL/api/slow/posts", client, object : TypeToken<List<Post>>() {})

suspend fun getComments(client: OkHttpClient, id: Long): List<Comment> =
    makeRequest("$BASE_URL/api/slow/posts/$id/comments", client, object : TypeToken<List<Comment>>() {})

suspend fun getAuthor(client: OkHttpClient, id: Long): Author =
    makeRequest("$BASE_URL/api/slow/authors/$id", client, object : TypeToken<Author>() {})
