package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.hamdan.forzenbook.profile.core.domain.GetBackwardPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.GetForwardPostsUseCase
import com.hamdan.forzenbook.profile.core.domain.GetPersonalProfileUseCase
import com.hamdan.forzenbook.profile.core.domain.GetProfileByUserUseCase
import com.hamdan.forzenbook.profile.core.domain.SendAboutUpdateUseCase
import com.hamdan.forzenbook.profile.core.domain.SendIconUpdateUseCase
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val backwardPostsUseCase: GetBackwardPostsUseCase,
    private val forwardPostsUseCase: GetForwardPostsUseCase,
    private val profileByUserUseCase: GetProfileByUserUseCase,
    private val personalProfileUseCase: GetPersonalProfileUseCase,
    private val aboutUpdateUseCase: SendAboutUpdateUseCase,
    private val iconUpdateUseCase: SendIconUpdateUseCase,
) : BaseProfileViewModel(
    backwardPostsUseCase,
    forwardPostsUseCase,
    profileByUserUseCase,
    personalProfileUseCase,
    aboutUpdateUseCase,
    iconUpdateUseCase,
) {
    private val _state: MutableState<ProfileState> =
        mutableStateOf(ProfileState.Loading())
    val state: MutableState<ProfileState>
        get() = _state

    override var profileState: ProfileState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
