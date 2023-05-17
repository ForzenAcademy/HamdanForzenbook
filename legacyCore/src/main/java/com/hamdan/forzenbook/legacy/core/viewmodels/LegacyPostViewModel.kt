package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.post.core.domain.SendImagePostUseCase
import com.hamdan.forzenbook.post.core.domain.SendTextPostUseCase
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LegacyPostViewModel @Inject constructor(
    private val textPostUseCase: SendTextPostUseCase,
    private val imagePostUseCase: SendImagePostUseCase,
    private val navigator: Navigator,
) : BasePostViewModel(
    textPostUseCase,
    imagePostUseCase
) {

    private val _state: MutableStateFlow<PostState> =
        MutableStateFlow(PostState.Content(PostContent.Text()))
    val state: StateFlow<PostState>
        get() = _state

    override var postState: PostState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun backButtonPressed(context: Context) {
        navigator.navigateToFeed(context)
    }

    fun kickToLogin(context: Context) {
        kickBackToLogin()
        navigator.kickToLogin(context)
    }
}
