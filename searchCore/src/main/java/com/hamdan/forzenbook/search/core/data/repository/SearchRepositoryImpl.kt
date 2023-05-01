package com.hamdan.forzenbook.search.core.data.repository

import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.entities.Postable
import com.hamdan.forzenbook.search.core.data.network.SearchResponse
import com.hamdan.forzenbook.search.core.data.network.SearchService
import com.hamdan.forzenbook.search.core.data.network.toFeedEntity
import com.hamdan.forzenbook.search.core.data.network.toUserEntity

class SearchRepositoryImpl(
    private val feedDao: FeedDao,
    private val userDao: UserDao,
    private val service: SearchService,
) : SearchRepository {
    override suspend fun getPostByUserId(id: Int, token: String): List<Postable> {
        val postable = mutableListOf<Postable>()
        service.getPosts(id = id, token = token).body()?.forEach {
            val user = userDao.getUser(it.userId)
            val post = feedDao.getSpecificPostId(it.postId)
            if (post.isNotEmpty() && user.isNotEmpty()) {
                postable.add(Postable(post[0], user[0]))
            }
        }
        return postable
    }

    override suspend fun getPostByQuery(query: String, token: String): List<Postable> {
        val postable = mutableListOf<Postable>()
        service.getPosts(query = query, token = token).body()?.forEach {
            val user = userDao.getUser(it.userId)
            val post = feedDao.getSpecificPostId(it.postId)
            if (post.isNotEmpty() && user.isNotEmpty()) {
                postable.add(Postable(post[0], user[0]))
            }
        }
        return postable
    }

    override suspend fun searchPostByUserId(id: Int, token: String) {
        deletePostsAndUsers()
        service.getPosts(id = id, token = token).body()?.forEach {
            updateUserEntity(it.userId)
            updatePostEntity(it)
        }
    }

    override suspend fun searchPostByQuery(query: String, token: String) {
        deletePostsAndUsers()
        service.getPosts(query = query, token = token).body()?.forEach {
            updateUserEntity(it.userId)
            updatePostEntity(it)
        }
    }

    private suspend fun deleteOldPosts(time: Long) {
        feedDao.deleteOldPosts(time)
    }

    private suspend fun deleteOldUsers(time: Long) {
        userDao.deleteOldUsers(time)
    }

    private suspend fun deletePostsAndUsers() {
        val currentTime = System.currentTimeMillis()
        deleteOldPosts(currentTime)
        deleteOldUsers(currentTime)
    }

    private suspend fun updatePostEntity(response: SearchResponse) {
        feedDao.getSpecificPostId(response.postId).apply {
            if (isEmpty() || size > 1) {
                feedDao.deleteSpecificPost(response.postId)
                feedDao.insert(response.toFeedEntity())
            }
        }
    }

    private suspend fun updateUserEntity(id: Int) {
        userDao.getUser(id).apply {
            if (isEmpty() || size > 1) {
                service.getUser(id).body()?.apply {
                    userDao.deleteUser(id)
                    userDao.insert(toUserEntity())
                }
            }
        }
    }
}