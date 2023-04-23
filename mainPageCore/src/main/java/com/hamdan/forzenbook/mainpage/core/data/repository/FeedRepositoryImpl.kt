package com.hamdan.forzenbook.mainpage.core.data.repository

import com.hamdan.forzenbook.mainpage.core.data.database.FeedDao
import com.hamdan.forzenbook.mainpage.core.data.database.UserDao
import com.hamdan.forzenbook.mainpage.core.data.network.FeedService
import com.hamdan.forzenbook.mainpage.core.data.network.toFeedEntity
import com.hamdan.forzenbook.mainpage.core.data.network.toUserEntity

class FeedRepositoryImpl(
    private val service: FeedService,
    private val feedDao: FeedDao,
    private val userDao: UserDao,
) : FeedRepository {
    override suspend fun getFeed(nameFormat: String): List<Postable> {
        val currentTime = System.currentTimeMillis()
        feedDao.deleteOldPosts(currentTime)
        userDao.deleteOldUsers(currentTime)
        val postables: MutableList<Postable> = mutableListOf()
        service.getFeed().body()?.apply {
            this.forEach {
                var user = userDao.getUser(it.userId)
                if (user.isEmpty() || user.size > 1) {
                    userDao.deleteUser(it.userId)
                    try {
                        service.getUser(it.userId).body()?.apply {
                            userDao.insert(toUserEntity(nameFormat))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    user = userDao.getUser(it.userId)
                }
                var post = feedDao.getSpecificPostId(it.postId)
                if (post.isEmpty()) {
                    feedDao.insert(it.toFeedEntity())
                    post = feedDao.getSpecificPostId(it.postId)
                }
                if (post.isNotEmpty() && user.isNotEmpty()) {
                    postables.add(Postable(post[0], user[0]))
                }
            }
            return postables
            // Todo will need to make this give specified values?
        }
        throw (Exception("retrieval issue"))
    }

    /* Todo
    We will need to account for the fact we will be given 10 things to be shown by the service at a time
     */
    companion object {
        const val DAY_IN_MILLIS: Long = 86400000
    }
}
