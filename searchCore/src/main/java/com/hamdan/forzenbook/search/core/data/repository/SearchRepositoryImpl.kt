package com.hamdan.forzenbook.search.core.data.repository

import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.entities.PostEntity
import com.hamdan.forzenbook.data.entities.Postable
import com.hamdan.forzenbook.data.entities.UserEntity
import com.hamdan.forzenbook.search.core.data.network.SearchResponse
import com.hamdan.forzenbook.search.core.data.network.SearchService
import com.hamdan.forzenbook.search.core.data.network.toFeedEntity
import com.hamdan.forzenbook.search.core.data.network.toUserEntity

class SearchRepositoryImpl(
    private val feedDao: FeedDao,
    private val userDao: UserDao,
    private val service: SearchService,
) : SearchRepository {
    override suspend fun getPostByUserId(id: Int): List<Postable> {
        deleteOldPosts()
        val postable = mutableListOf<Postable>()
        service.getPosts(id = id).body()?.forEach {
            val user = getUserEntity(it.userId)
            val post = getPostEntity(it)
            if (post.isNotEmpty() && user.isNotEmpty()) {
                postable.add(Postable(post[0], user[0]))
            }
        }
        return postable
    }

    override suspend fun getPostByQuery(query: String): List<Postable> {
        deleteOldPosts()
        val postable = mutableListOf<Postable>()
        service.getPosts(query = query).body()?.forEach {
            val user = getUserEntity(it.userId)
            val post = getPostEntity(it)
            if (post.isNotEmpty() && user.isNotEmpty()) {
                postable.add(Postable(post[0], user[0]))
            }
        }
        return postable
    }

    private suspend fun deleteOldPosts() {
        val currentTime = System.currentTimeMillis()
        feedDao.deleteOldPosts(currentTime)
        userDao.deleteOldUsers(currentTime)
    }

    private suspend fun getUserEntity(id: Int): List<UserEntity> {
        var user = userDao.getUser(id)
        if (user.isEmpty() || user.size > 1) {
            userDao.deleteUser(id)
            try {
                service.getUser(id).body()?.apply {
                    userDao.insert(toUserEntity())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            user = userDao.getUser(id)
        }
        return user
    }

    private suspend fun getPostEntity(response: SearchResponse): List<PostEntity> {
        var post = feedDao.getSpecificPostId(response.postId)
        if (post.isEmpty()) {
            feedDao.insert(response.toFeedEntity())
            post = feedDao.getSpecificPostId(response.postId)
        }
        return post
    }
}
