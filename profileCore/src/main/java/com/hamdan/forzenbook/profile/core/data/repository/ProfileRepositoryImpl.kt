package com.hamdan.forzenbook.profile.core.data.repository

import android.content.Context
import com.hamdan.forzenbook.core.GlobalConstants.PAGED_POSTS_SIZE
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.core.NetworkRetrievalException
import com.hamdan.forzenbook.core.getToken
import com.hamdan.forzenbook.core.removeToken
import com.hamdan.forzenbook.data.PagingDirection
import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.entities.Postable
import com.hamdan.forzenbook.profile.core.data.network.ImageResponse
import com.hamdan.forzenbook.profile.core.data.network.ProfileResponse
import com.hamdan.forzenbook.profile.core.data.network.ProfileService
import com.hamdan.forzenbook.profile.core.data.network.toPostEntity
import com.hamdan.forzenbook.profile.core.data.network.toUserEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class ProfileRepositoryImpl(
    private val feedDao: FeedDao,
    private val userDao: UserDao,
    private val service: ProfileService,
    private val context: Context,
) : ProfileRepository {
    override suspend fun getProfile(userId: Int?): ProfileInfo {
        val token = getToken(context)

        if (token.isNullOrEmpty()) {
            removeToken(context)
            throw InvalidTokenException()
        }

        val currentTime = System.currentTimeMillis()
        feedDao.deleteOldPosts(currentTime)
        userDao.deleteOldUsers(currentTime)

        val response = if (userId == null) service.getUsersProfile(token)
        else service.getProfile(token, userId)

        if (!response.isSuccessful) {
            throw NetworkRetrievalException()
        }

        val body = response.body() ?: throw NetworkRetrievalException()

        return ProfileInfo(
            firstName = body.firstName,
            lastName = body.lastName,
            userId = body.userId,
            isOwner = userId == null,
            postSet = convertPosts(body),
            userIconPath = body.profileImage,
            dateJoined = body.created,
            aboutUser = body.aboutMe
        )
    }

    private suspend fun convertPosts(body: ProfileResponse): List<Postable> {
        val postables: MutableList<Postable> = mutableListOf()
        body.posts.forEach {
            var user = userDao.getUser(it.userId)
            if (user.isEmpty() || user.size > 1) {
                userDao.deleteUser(it.userId)
                try {
                    userDao.insert(body.toUserEntity())
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
    }

    override suspend fun getPagedPosts(
        postId: Int,
        userId: Int,
        pagingDirection: PagingDirection
    ): List<Postable> {
        val token = getToken(context)
        if (token.isNullOrEmpty()) {
            removeToken(context)
            throw InvalidTokenException()
        }

        return try {
            val user = userDao.getUser(userId).first()

            if (pagingDirection == PagingDirection.FORWARD) {
                feedDao.getForwardPagedPosts(postId, PAGED_POSTS_SIZE).map {
                    Postable(it, user)
                }
            } else {
                feedDao.getBackwardPagedPosts(postId, PAGED_POSTS_SIZE).map {
                    Postable(it, user)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun updateAboutMe(userId: Int, text: String) {
        val token = getToken(context)
        if (token.isNullOrEmpty()) {
            removeToken(context)
            throw InvalidTokenException()
        }

        val body = JSONObject(mapOf(ID to userId, ABOUT_ME to text)).toString()
            .toRequestBody(CONTENT_TYPE_JSON.toMediaTypeOrNull())
        val response = service.updateAboutMe(token, body)

        if (!response.isSuccessful) {
            throw NetworkRetrievalException()
        }
    }

    override suspend fun updateIcon(userId: Int, file: File): ImageResponse {
        val token = getToken(context)
        if (token.isNullOrEmpty()) {
            removeToken(context)
            throw InvalidTokenException()
        }

        val requestBodyType =
            userId.toString().toRequestBody(CONTENT_TYPE_MULTIPART_FORM.toMediaTypeOrNull())
        val requestBodyFile = file.asRequestBody(FILE_TYPE_IMAGE.toMediaTypeOrNull())

        val response = service.updateImage(
            token,
            requestBodyType,
            MultipartBody.Part.createFormData(IMAGE_FIELD_NAME, file.name, requestBodyFile)
        )

        if (!response.isSuccessful) {
            throw NetworkRetrievalException()
        }

        response.body()?.let {
            userDao.updateUserImage(it.image, userId)
            return it
        } ?: throw NetworkRetrievalException()
    }

    companion object {
        const val CONTENT_TYPE_JSON = "application/json; charset=utf-8"
        const val CONTENT_TYPE_MULTIPART_FORM = "multipart/form-data"
        const val FILE_TYPE_IMAGE = "image/*"
        const val ID = "id"
        const val ABOUT_ME = "aboutMe"
        const val IMAGE_FIELD_NAME = "profileImage"
    }
}