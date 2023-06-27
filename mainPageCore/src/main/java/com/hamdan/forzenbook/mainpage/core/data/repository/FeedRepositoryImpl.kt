package com.hamdan.forzenbook.mainpage.core.data.repository

import android.content.Context
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.core.getToken
import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.entities.Postable
import com.hamdan.forzenbook.mainpage.core.data.network.FeedService
import com.hamdan.forzenbook.mainpage.core.data.network.toPostEntity
import com.hamdan.forzenbook.mainpage.core.data.network.toUserEntity

class FeedRepositoryImpl(
    private val service: FeedService,
    private val feedDao: FeedDao,
    private val userDao: UserDao,
    private val context: Context,
) : FeedRepository {
    override suspend fun getFeed(): List<Postable> {
        val token = getToken(context)
        if (token.isNullOrEmpty()) throw InvalidTokenException()

        val currentTime = System.currentTimeMillis()
        feedDao.deleteOldPosts(currentTime)
        userDao.deleteOldUsers(currentTime)
        val postables: MutableList<Postable> = mutableListOf()
        service.getFeed(token).body()?.apply {
            this.forEach {
                var user = userDao.getUser(it.userId)
                if (user.isEmpty() || user.size > 1) {
                    userDao.deleteUser(it.userId)
                    try {
                        service.getUser(it.userId).body()?.apply {
                            userDao.insert(toUserEntity())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    user = userDao.getUser(it.userId)
                }
                var post = feedDao.getSpecificPostId(it.postId)
                if (post.isEmpty()) {
                    feedDao.insert(it.toPostEntity())
                    post = feedDao.getSpecificPostId(it.postId)
                }
                if (post.isNotEmpty() && user.isNotEmpty()) {
                    postables.add(Postable(post[0], user[0]))
                }
            }
            return postables
            // Todo will need to make this give specified values?
            // Only will need to do that if we have time to develop a service experience, otherwise this is on the backburner
        }
        throw (Exception("retrieval issue"))
    }

    /* Todo
    We will need to account for the fact we will be given 10 things to be shown by the service at a time
     */
}
