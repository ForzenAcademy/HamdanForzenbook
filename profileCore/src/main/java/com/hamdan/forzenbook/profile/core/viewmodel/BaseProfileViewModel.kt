package com.hamdan.forzenbook.profile.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.GlobalConstants.PAGED_POSTS_SIZE
import com.hamdan.forzenbook.core.GlobalConstants.POSTS_MAX_SIZE
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.profile.core.domain.GetBackwardPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.GetForwardPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.GetPersonalProfileUseCase
import com.hamdan.forzenbook.profile.core.domain.GetProfileByUserUseCase
import com.hamdan.forzenbook.profile.core.domain.SendAboutUpdateUseCase
import com.hamdan.forzenbook.profile.core.domain.SendIconUpdateUseCase
import kotlinx.coroutines.launch

abstract class BaseProfileViewModel(
    private val getBackwardPostsUseCase: GetBackwardPostsUseCase,
    private val getForwardPostsUseCase: GetForwardPostsUseCase,
    private val getProfileByUserUseCase: GetProfileByUserUseCase,
    private val getPersonalProfileUseCase: GetPersonalProfileUseCase,
    private val sendAboutUpdateUseCase: SendAboutUpdateUseCase,
    private val sendIconUpdateUseCase: SendIconUpdateUseCase,
) : ViewModel() {

    data class ProfileData(
        val firstName: String,
        val lastName: String,
        val id: Int,
        val isOwner: Boolean,
        val postSet: List<PostData> = emptyList(),
        val firstPostId: Int?,
        val userIconPath: String,
        val dateJoined: String,
        val aboutUser: String,
        val bottomState: ProfileContentSelector = ProfileContentSelector.ABOUT,
    )

    sealed interface ProfileState {
        val profileData: ProfileData?

        data class Loading(override val profileData: ProfileData? = null) : ProfileState

        data class Content(
            override val profileData: ProfileData? = null
        ) : ProfileState

        data class Error(
            override val profileData: ProfileData? = null,
            val error: ProfileError,
        ) : ProfileState

        data class Editing(
            override val profileData: ProfileData? = null,
            val editContent: EditingContent,
        ) : ProfileState

        data class InvalidLogin(override val profileData: ProfileData? = null) : ProfileState
    }

    sealed interface EditingContent {
        object None : EditingContent
        object Loading : EditingContent
        data class About(val newAbout: String) : EditingContent
        data class Image(val newImagePath: String?) : EditingContent
    }

    sealed interface ProfileError {
        object Posts : ProfileError
        object Generic : ProfileError
        object NewIcon : ProfileError
        object NewAboutMe : ProfileError
        object Loading : ProfileError
    }

    protected abstract var profileState: ProfileState

    fun onPersonalProfilePress() {
        profileState = ProfileState.Loading(null)
        try {
            viewModelScope.launch {
                profileState = ProfileState.Content(getPersonalProfileUseCase())
            }
        } catch (e: Exception) {
            profileState = ProfileState.Error(error = ProfileError.Loading)
        }
    }

    fun onProfilePress(userId: Int) {
        profileState = ProfileState.Loading(null)
        try {
            viewModelScope.launch {
                profileState = ProfileState.Content(getProfileByUserUseCase(userId))
            }
        } catch (e: Exception) {
            profileState = ProfileState.Error(error = ProfileError.Loading)
        }
    }

    fun onEditPressed() {
        val currentState = profileState
        currentState.profileData?.let {
            if (currentState is ProfileState.Content && it.isOwner) {
                profileState = ProfileState.Editing(
                    profileData = it,
                    editContent = EditingContent.None
                )
            } else throw StateException()
        } ?: throw StateException()
    }

    fun onEditAboutPressed() {
        val currentState = profileState
        currentState.profileData?.let {
            if (currentState is ProfileState.Editing) {
                profileState = currentState.copy(editContent = EditingContent.About(it.aboutUser))
            } else throw StateException()
        } ?: throw StateException()
    }

    fun onEditAboutTextChange(text: String) {
        val currentState = profileState
        currentState.profileData?.let {
            if (currentState is ProfileState.Editing && currentState.editContent is EditingContent.About) {
                profileState = currentState.copy(editContent = currentState.editContent.copy(text))
            } else throw StateException()
        } ?: throw StateException()
    }

    fun onEditAboutTextSubmit() {
        val currentState = profileState
        currentState.profileData?.let {
            if (currentState is ProfileState.Editing && currentState.editContent is EditingContent.About) {
                try {
                    profileState = currentState.copy(editContent = EditingContent.Loading)
                    viewModelScope.launch {
                        sendAboutUpdateUseCase(it.id, currentState.editContent.newAbout)
                        profileState = currentState.copy(
                            profileData = it.copy(aboutUser = currentState.editContent.newAbout),
                            editContent = EditingContent.None
                        )
                    }
                } catch (e: Exception) {
                    profileState =
                        ProfileState.Error(profileData = it, error = ProfileError.NewAboutMe)
                }
            } else throw StateException()
        } ?: throw StateException()
    }

    fun onImageChanged(iconPath: String?) {
        val currentState = profileState
        currentState.profileData?.let {
            if (currentState is ProfileState.Editing && currentState.editContent is EditingContent.Image) {
                iconPath?.let {
                    profileState =
                        currentState.copy(editContent = currentState.editContent.copy(iconPath))
                } ?: run {
                    profileState =
                        ProfileState.Error(profileData = it, error = ProfileError.NewIcon)
                }
            } else throw StateException()
        } ?: throw StateException()
    }

    fun onImageSubmit() {
        val currentState = profileState
        currentState.profileData?.let { data ->
            if (currentState is ProfileState.Editing && currentState.editContent is EditingContent.Image) {
                try {
                    profileState = currentState.copy(editContent = EditingContent.Loading)
                    viewModelScope.launch {
                        currentState.editContent.newImagePath?.let {
                            profileState = currentState.copy(
                                profileData = data.copy(
                                    userIconPath = sendIconUpdateUseCase(
                                        data.id,
                                        it
                                    )
                                ),
                                editContent = EditingContent.None
                            )
                        } ?: throw Exception("Null image path")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    profileState =
                        ProfileState.Error(profileData = data, error = ProfileError.NewIcon)
                }
            } else throw StateException()
        } ?: throw StateException()
    }

    fun onSheetDismiss() {
        val currentState = profileState
        if (currentState is ProfileState.Editing) {
            profileState = currentState.copy(editContent = EditingContent.None)
        } else throw StateException()
    }

    fun onEditProfileImagePressed() {
        val currentState = profileState
        if (currentState is ProfileState.Editing) {
            profileState =
                currentState.copy(editContent = EditingContent.Image(currentState.profileData?.userIconPath))
        } else throw StateException()
    }

    fun onSelectorPressed(selected: ProfileContentSelector) {
        val currentState = profileState
        profileState = when (currentState) {
            is ProfileState.Content -> currentState.copy(
                profileData = currentState.profileData?.copy(
                    bottomState = selected
                )
            )

            is ProfileState.Editing -> currentState.copy(
                profileData = currentState.profileData?.copy(
                    bottomState = selected
                )
            )

            else -> throw StateException()
        }
    }

    fun onPostsScrollUp() {
        val currentState = profileState
        currentState.profileData?.let { data ->
            viewModelScope.launch {
                when (currentState) {
                    is ProfileState.Content -> {
                        try {
                            currentState.apply {
                                var postsAvailable = true
                                data.postSet.forEach {
                                    if (it.postId == data.firstPostId) {
                                        postsAvailable = false
                                    }
                                }
                                if (postsAvailable && data.postSet.size == POSTS_MAX_SIZE) {
                                    profileState = copy(
                                        profileData = data.copy(
                                            postSet = getBackwardPostsUseCase(
                                                data.id,
                                                data.postSet[0].postId,
                                            ) + data.postSet.subList(
                                                0,
                                                data.postSet.size - PAGED_POSTS_SIZE,
                                            )
                                        )
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            profileState = ProfileState.Error(
                                profileData = data.copy(postSet = emptyList()),
                                error = ProfileError.Posts
                            )
                        }
                    }

                    is ProfileState.Editing -> {
                        try {
                            currentState.apply {
                                var postsAvailable = true
                                data.postSet.forEach {
                                    if (it.postId == data.firstPostId) {
                                        postsAvailable = false
                                    }
                                }
                                if (postsAvailable && data.postSet.size == POSTS_MAX_SIZE) {
                                    profileState = copy(
                                        profileData = data.copy(
                                            postSet = getBackwardPostsUseCase(
                                                data.id,
                                                data.postSet[0].postId,
                                            ) + data.postSet.subList(
                                                0,
                                                data.postSet.size - PAGED_POSTS_SIZE,
                                            )
                                        )
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            profileState = ProfileState.Error(
                                profileData = data.copy(postSet = emptyList()),
                                error = ProfileError.Posts,
                            )
                        }
                    }

                    else -> throw StateException()
                }
            }
        } ?: throw StateException()
    }

    fun onPostsScrollDown() {
        val currentState = profileState
        currentState.profileData?.let { data ->
            viewModelScope.launch {
                when (currentState) {
                    is ProfileState.Content -> {
                        try {
                            if (data.postSet.size % PAGED_POSTS_SIZE == 0 && data.postSet.size < POSTS_MAX_SIZE) {
                                profileState = currentState.copy(
                                    profileData = data.copy(
                                        postSet = data.postSet + getForwardPostsUseCase(
                                            data.id,
                                            data.postSet.last().postId,
                                        )
                                    )
                                )
                            } else if (data.postSet.size == POSTS_MAX_SIZE) {
                                profileState = currentState.copy(
                                    profileData = data.copy(
                                        postSet = data.postSet.subList(
                                            PAGED_POSTS_SIZE,
                                            data.postSet.size
                                        ) + getForwardPostsUseCase(
                                            data.id,
                                            data.postSet.last().postId,
                                        )
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            profileState = ProfileState.Error(
                                profileData = data.copy(postSet = emptyList()),
                                error = ProfileError.Posts,
                            )
                        }
                    }

                    is ProfileState.Editing -> {
                        try {
                            if (data.postSet.size % PAGED_POSTS_SIZE == 0 && data.postSet.size < POSTS_MAX_SIZE) {
                                profileState = currentState.copy(
                                    profileData = data.copy(
                                        postSet = data.postSet + getForwardPostsUseCase(
                                            data.id,
                                            data.postSet.last().postId,
                                        )
                                    )
                                )
                            } else if (data.postSet.size == POSTS_MAX_SIZE) {
                                profileState = currentState.copy(
                                    profileData = data.copy(
                                        postSet = data.postSet.subList(
                                            PAGED_POSTS_SIZE,
                                            data.postSet.size
                                        ) + getForwardPostsUseCase(
                                            data.id,
                                            data.postSet.last().postId,
                                        )
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            profileState = ProfileState.Error(
                                profileData = data.copy(postSet = emptyList()),
                                error = ProfileError.Posts,
                            )
                        }
                    }

                    else -> throw StateException()
                }
            }
        } ?: throw StateException()
    }

    fun onErrorDismiss() {
        val currentState = profileState
        if (currentState is ProfileState.Error) {
            when (currentState.error) {
                is ProfileError.Generic -> {
                    if (currentState.profileData != null) {
                        profileState =
                            ProfileState.Content(currentState.profileData)
                    } else throw StateException()
                }

                ProfileError.Loading -> {
                    profileState = ProfileState.Loading()
                }

                is ProfileError.NewAboutMe -> {
                    if (currentState.profileData != null) {
                        profileState = ProfileState.Content(currentState.profileData)
                    } else throw StateException()

                }

                is ProfileError.NewIcon -> {
                    if (currentState.profileData != null) {
                        profileState = ProfileState.Content(currentState.profileData)
                    } else throw StateException()
                }

                is ProfileError.Posts -> {
                    currentState.profileData?.let {
                        profileState = ProfileState.Content(it.copy(postSet = emptyList()))
                    } ?: throw StateException()
                }
            }
        } else throw StateException()
    }

    fun onBackPressed() {
        profileState = when (profileState) {
            is ProfileState.Content -> {
                ProfileState.Loading()
            }

            is ProfileState.Editing -> {
                ProfileState.Loading()
            }

            else -> throw StateException()
        }
    }

    fun onEditSubmitPressed() {
        val currentState = profileState
        if (currentState is ProfileState.Editing) {
            profileState = ProfileState.Content(currentState.profileData)
        } else throw StateException()
    }

    companion object {
        const val ABOUT_ME_DEFAULT = "ABOUT_ME_DEFAULT_VALUE"
    }
}

enum class ProfileContentSelector {
    ABOUT, POSTS,
}
