package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.hamdan.forzenbook.post.core.domain.SendImagePostUseCase
import com.hamdan.forzenbook.post.core.domain.SendTextPostUseCase
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val textPostUseCase: SendTextPostUseCase,
    private val imagePostUseCase: SendImagePostUseCase,
) : BasePostViewModel(
    textPostUseCase,
    imagePostUseCase
) {
    private val _state: MutableState<PostState> =
        mutableStateOf(PostState.Content(PostContent.Text()))
    val state: MutableState<PostState>
        get() = _state

    override var postState: PostState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
