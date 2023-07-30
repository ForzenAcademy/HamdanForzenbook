package com.hamdan.forzenbook.profile.compose

import android.graphics.BitmapFactory
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.CircularNetworkImage
import com.hamdan.forzenbook.compose.core.composewidgets.Divider
import com.hamdan.forzenbook.compose.core.composewidgets.FeedImagePost
import com.hamdan.forzenbook.compose.core.composewidgets.FeedTextPost
import com.hamdan.forzenbook.compose.core.composewidgets.FocusableTextField
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composewidgets.LoadingOverlay
import com.hamdan.forzenbook.compose.core.composewidgets.PostCard
import com.hamdan.forzenbook.compose.core.composewidgets.SubmitButton
import com.hamdan.forzenbook.compose.core.composewidgets.TitleText
import com.hamdan.forzenbook.compose.core.composewidgets.UserRow
import com.hamdan.forzenbook.compose.core.composewidgets.addIf
import com.hamdan.forzenbook.compose.core.theme.additionalColors
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.compose.core.theme.disabledAlpha
import com.hamdan.forzenbook.compose.core.theme.staticDimens
import com.hamdan.forzenbook.core.GlobalConstants.BASE_URL
import com.hamdan.forzenbook.core.GlobalConstants.ONE_LINE
import com.hamdan.forzenbook.core.GlobalConstants.QUICK_ANIMATION_DURATION_MS
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel
import com.hamdan.forzenbook.profile.core.viewmodel.ProfileContentSelector
import com.hamdan.forzenbook.ui.core.R

@Composable
fun Profile(
    state: BaseProfileViewModel.ProfileState,
    onBackPressed: () -> Unit,
    onErrorDismiss: () -> Unit,
    onEditAboutPressed: () -> Unit,
    onAboutTextChanged: (String) -> Unit,
    onAboutTextChangeSubmit: () -> Unit,
    onEditProfileImagePressed: () -> Unit,
    onChangeProfileImagePressed: () -> Unit,
    onChangeImageSubmit: () -> Unit,
    onEditSubmitPressed: () -> Unit,
    onSelectorPressed: (ProfileContentSelector) -> Unit,
    onEditPressed: () -> Unit,
    onSheetDismiss: () -> Unit,
    onBottomReached: () -> Unit,
    onTopReached: () -> Unit,
) {

    when (state) {
        is BaseProfileViewModel.ProfileState.Content -> {
            MainContent(
                state = state,
                onEditPressed = onEditPressed,
                onBackPressed = onBackPressed,
                onSelectorPressed = onSelectorPressed,
                onBottomReached = onBottomReached,
                onTopReached = onTopReached,
            )
        }

        is BaseProfileViewModel.ProfileState.Error -> {
            ErrorContent(state, onErrorDismiss)
        }

        is BaseProfileViewModel.ProfileState.Loading -> {
            LoadingContent(state = state)
        }

        is BaseProfileViewModel.ProfileState.Editing -> {
            EditingContent(
                state = state,
                onSelectorPressed = onSelectorPressed,
                onAboutTextChanged = onAboutTextChanged,
                onAboutTextChangeSubmit = onAboutTextChangeSubmit,
                onEditImagePressed = onEditProfileImagePressed,
                onChangeImagePressed = onChangeProfileImagePressed,
                onChangeImageSubmit = onChangeImageSubmit,
                onEditSubmitPressed = onEditSubmitPressed,
                onBackPressed = onBackPressed,
                onEditAboutPressed = onEditAboutPressed,
                onSheetDismiss = onSheetDismiss,
                onBottomReached = onBottomReached,
                onTopReached = onTopReached,
            )
        }

        is BaseProfileViewModel.ProfileState.InvalidLogin -> {
            // navigate to login
            // kick back to login function
        }
    }
}

@Composable
private fun MainContent(
    state: BaseProfileViewModel.ProfileState.Content,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onSelectorPressed: (ProfileContentSelector) -> Unit,
    onBottomReached: () -> Unit,
    onTopReached: () -> Unit,
) {
    state.profileData?.apply {
        BaseContent(
            imagePath = userIconPath,
            firstName = firstName,
            lastName = lastName,
            dateJoined = dateJoined,
            selectorState = bottomState,
            about = aboutUser,
            posts = postSet,
            isOwner = isOwner,
            firstPostId = firstPostId,
            lastPostId = lastPostId,
            onBackPressed = onBackPressed,
            onEditPressed = onEditPressed,
            onSelectorPressed = onSelectorPressed,
            requestScrollUpPosts = onTopReached,
            requestScrollDownPosts = onBottomReached,
        )
    }
}

@Composable
private fun ErrorContent(
    state: BaseProfileViewModel.ProfileState.Error,
    onErrorDismiss: () -> Unit,
) {
    val navigator = LocalNavController.current
    state.error.let { error ->
        state.profileData?.apply {
            when (error) {
                is BaseProfileViewModel.ProfileError.Generic -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_body),
                        buttonText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }

                BaseProfileViewModel.ProfileError.Loading -> {
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_loading_body),
                        buttonText = stringResource(id = R.string.generic_dialog_confirm)
                    ) {
                        onErrorDismiss()
                        navigator?.navigateUp()
                    }
                }

                is BaseProfileViewModel.ProfileError.NewAboutMe -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_about_body),
                        buttonText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }

                is BaseProfileViewModel.ProfileError.NewIcon -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_icon_body),
                        buttonText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }

                is BaseProfileViewModel.ProfileError.Posts -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_posts_body),
                        buttonText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(
    state: BaseProfileViewModel.ProfileState.Loading
) {
    state.profileData.apply {
        if (this == null) {
            LoadingOverlay()
        } else {
            BaseContent(
                imagePath = userIconPath,
                firstName = firstName,
                lastName = lastName,
                dateJoined = dateJoined,
                selectorState = bottomState,
                about = aboutUser,
                firstPostId = firstPostId,
                lastPostId = lastPostId,
            )
        }
    }
}

@Composable
private fun EditingContent(
    state: BaseProfileViewModel.ProfileState.Editing,
    onBackPressed: () -> Unit,
    onSelectorPressed: (ProfileContentSelector) -> Unit,
    onEditAboutPressed: () -> Unit,
    onAboutTextChanged: (String) -> Unit,
    onAboutTextChangeSubmit: () -> Unit,
    onEditImagePressed: () -> Unit,
    onChangeImagePressed: () -> Unit,
    onChangeImageSubmit: () -> Unit,
    onEditSubmitPressed: () -> Unit,
    onSheetDismiss: () -> Unit,
    onBottomReached: () -> Unit,
    onTopReached: () -> Unit,
) {
    state.profileData?.apply {
        BaseContent(
            imagePath = userIconPath,
            firstName = firstName,
            lastName = lastName,
            dateJoined = dateJoined,
            posts = postSet,
            selectorState = bottomState,
            about = aboutUser,
            firstPostId = firstPostId,
            lastPostId = lastPostId,
            editingContent = state.editContent,
            onBackPressed = onBackPressed,
            onSelectorPressed = onSelectorPressed,
            onNormalViewPressed = onEditSubmitPressed,
            onEditAboutPressed = onEditAboutPressed,
            onAboutTextChanged = onAboutTextChanged,
            onAboutTextChangeSubmit = onAboutTextChangeSubmit,
            onEditImagePressed = onEditImagePressed,
            onChangeImagePressed = onChangeImagePressed,
            onChangeImageSubmit = onChangeImageSubmit,
            onSheetDismiss = onSheetDismiss,
            requestScrollDownPosts = onBottomReached,
            requestScrollUpPosts = onTopReached,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun BaseContent(
    imagePath: String,
    firstName: String,
    lastName: String,
    dateJoined: String,
    selectorState: ProfileContentSelector,
    posts: List<PostData> = emptyList(),
    about: String,
    isOwner: Boolean = false,
    editingContent: BaseProfileViewModel.EditingContent? = null,
    firstPostId: Int?,
    lastPostId: Int?,
    onBackPressed: () -> Unit = {},
    onSelectorPressed: (ProfileContentSelector) -> Unit = {},
    onEditAboutPressed: () -> Unit = {},
    onAboutTextChanged: (String) -> Unit = {},
    onAboutTextChangeSubmit: () -> Unit = {},
    onEditImagePressed: () -> Unit = {},
    onChangeImagePressed: () -> Unit = {},
    onChangeImageSubmit: () -> Unit = {},
    onEditPressed: () -> Unit = {},
    onSheetDismiss: () -> Unit = {},
    onNormalViewPressed: () -> Unit = {},
    requestScrollUpPosts: () -> Unit = {},
    requestScrollDownPosts: () -> Unit = {},
) {
    val navigator = LocalNavController.current
    // region height and animation calculations
    val density = LocalDensity.current
    val topBarHeight = 64.dp // the height before showing top bar
    val topBarHeightFloat = with(density) { topBarHeight.toPx() }
    val dateAlpha = .8f
    val userImagePath = BASE_URL + imagePath

    val postSizes = remember { mutableMapOf<Int, Int>() }

    val lazyListState = rememberLazyListState()
    var lazyColumnHeight by remember { mutableStateOf(0f) }
    var rowHeight by remember { mutableStateOf(0f) }
    var aboutHeight by remember { mutableStateOf(0f) }
    val item = remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }
    val showSheet = remember { mutableStateOf(false) }
    showSheet.value = when (editingContent) {
        is BaseProfileViewModel.EditingContent.About -> true
        is BaseProfileViewModel.EditingContent.Image -> true
        else -> false
    }

    val animationBreakpointBool = item.value >= 1

    // temporary
    if (posts.isEmpty()) requestScrollDownPosts()

    var extraScrollHeight by remember { mutableStateOf(0f) }

    val containerColor = animateColorAsState(
        targetValue = if (animationBreakpointBool) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(QUICK_ANIMATION_DURATION_MS, 0, FastOutLinearInEasing),
    )
    val textColor = animateColorAsState(
        targetValue = if (animationBreakpointBool) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
        animationSpec = tween(QUICK_ANIMATION_DURATION_MS, 0, FastOutLinearInEasing),
    )
    val topBarColor = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = containerColor.value,
        titleContentColor = textColor.value,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
    val iconColor = animateColorAsState(
        targetValue = if (animationBreakpointBool) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onTertiary,
        animationSpec = tween(QUICK_ANIMATION_DURATION_MS, 0, FastOutLinearInEasing),
    )

    // endregion
    LazyColumn(
        modifier = Modifier
            .imePadding()
            .fillMaxSize()
            .onGloballyPositioned {
                if (lazyColumnHeight == 0f) {
                    lazyColumnHeight += it.size.height.toFloat()
                }
            }
            .background(MaterialTheme.colorScheme.tertiary),
        state = lazyListState,
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // region profile picture
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.dimens.grid.x6,
                            bottom = MaterialTheme.dimens.grid.x2,
                            start = MaterialTheme.dimens.grid.x5,
                            end = MaterialTheme.dimens.grid.x5,
                        )
                        .size(MaterialTheme.dimens.imageSizes.large)
                ) {
                    CircularNetworkImage(
                        imageUrl = userImagePath,
                        imageSize = MaterialTheme.dimens.imageSizes.large,
                        imageDescription = stringResource(R.string.profile_profile_picture),
                        borderColor = MaterialTheme.additionalColors.onBackgroundBorder,
                    )
                    if (editingContent != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .border(
                                        MaterialTheme.dimens.borderGrid.x2,
                                        MaterialTheme.additionalColors.onBackgroundBorder,
                                        CircleShape,
                                    )
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = stringResource(id = R.string.profile_edit_button),
                                    tint = iconColor.value,
                                    modifier = Modifier
                                        .padding(MaterialTheme.dimens.grid.x3)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(
                                                bounded = false,
                                                radius = MaterialTheme.dimens.grid.x6
                                            ),
                                            onClick = onEditImagePressed,
                                        )
                                )
                            }
                        }
                    }
                }
                // endregion

                // region profileText
                TitleText(
                    text = stringResource(id = R.string.user_name, firstName, lastName),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = stringResource(id = R.string.profile_joined, dateJoined),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = dateAlpha),
                    modifier = Modifier.padding(bottom = MaterialTheme.dimens.grid.x1),
                    style = MaterialTheme.typography.titleLarge,
                )
                // endregion
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .height(topBarHeight)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
        item {
            // region selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        if (rowHeight == 0f) {
                            rowHeight = it.size.height.toFloat()
                        }
                    }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = MaterialTheme.dimens.grid.x1)
            ) {
                ProfileContentSelector.values().forEach {
                    val selected = selectorState == it
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = MaterialTheme.dimens.borderGrid.x1,
                                end = if (it != ProfileContentSelector
                                    .values()
                                    .last()
                                ) MaterialTheme.dimens.borderGrid.x1 else 0.dp
                            )
                            .background(MaterialTheme.colorScheme.tertiary)
                            .clickable {
                                onSelectorPressed(it)
                            },
                    ) {
                        val spacerHeight =
                            animateDpAsState(targetValue = if (selected) MaterialTheme.dimens.grid.x1 else 0.dp)
                        Text(
                            text = when (it) {
                                ProfileContentSelector.ABOUT -> stringResource(id = R.string.profile_selector_about)
                                ProfileContentSelector.POSTS -> stringResource(id = R.string.profile_selector_posts)
                            },
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = MaterialTheme.dimens.grid.x4,
                                    bottom = if (selected) MaterialTheme.dimens.grid.x3 else MaterialTheme.dimens.grid.x4
                                ),
                            textAlign = TextAlign.Center,
                            maxLines = ONE_LINE,
                        )
                        if (selectorState == it) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .height(spacerHeight.value)
                            )
                        }
                    }
                }
            }
            // endregion
        }

        // region selected content
        when (selectorState) {
            ProfileContentSelector.ABOUT -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned {
                                aboutHeight = it.size.height.toFloat()
                            }
                            .background(MaterialTheme.colorScheme.tertiary),
                    ) {
                        Text(
                            text = if (about == BaseProfileViewModel.ABOUT_ME_DEFAULT) stringResource(
                                id = R.string.profile_about_default, firstName, lastName,
                            ) else about,
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(
                                horizontal = MaterialTheme.dimens.grid.x3,
                                vertical = MaterialTheme.dimens.grid.x4
                            ),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    extraScrollHeight =
                        (lazyColumnHeight - aboutHeight - rowHeight - topBarHeightFloat) / density.density
                    Spacer(
                        modifier = Modifier
                            .height(extraScrollHeight.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.tertiary),
                    )
                }
            }

            ProfileContentSelector.POSTS -> {
                itemsIndexed(posts) { index, item ->
                    if (item == posts.first() && item.postId != firstPostId) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.grid.x2),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = requestScrollUpPosts) {
                                Text(text = stringResource(R.string.profile_paging_button_up))
                            }
                        }
                    }
                    PostCard(
                        modifier = Modifier.addIf(
                            extraScrollHeight > 0 && !postSizes.containsKey(index)
                        ) {
                            Modifier.onGloballyPositioned {
                                postSizes[index] = it.size.height
                            }
                        }
                    ) {
                        UserRow(
                            BASE_URL + item.posterIcon,
                            item.posterFirstName,
                            item.posterLastName,
                            item.posterLocation,
                            item.date,
                        )
                        Divider()
                        if (item.type == PostData.IMAGE_TYPE) {
                            FeedImagePost(BASE_URL + item.body)
                        } else {
                            FeedTextPost(item.body)
                        }
                    }
                    if (item == posts.last() && item.postId != lastPostId) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.grid.x2),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(onClick = requestScrollDownPosts) {
                                Text(text = stringResource(id = R.string.profile_paging_button_down))
                            }
                        }
                    }
                }
                item {
                    extraScrollHeight =
                        ((lazyColumnHeight - rowHeight - topBarHeightFloat - postSizes.values.sum()) / density.density)
                            .coerceAtLeast(0f)
                    if (extraScrollHeight > 0) {
                        Spacer(
                            modifier = Modifier
                                .height(extraScrollHeight.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.tertiary)
                        )
                    }
                }
            }

            else -> throw StateException()
        }

        // endregion
    }
    // region FAB for about
    if (editingContent != null && selectorState == ProfileContentSelector.ABOUT) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .padding(MaterialTheme.dimens.grid.x3)
                    .clip(CircleShape)
                    .border(
                        MaterialTheme.dimens.borderGrid.x2,
                        MaterialTheme.additionalColors.onBackgroundBorder,
                        CircleShape,
                    )
                    .background(MaterialTheme.colorScheme.background)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            bounded = false,
                            radius = MaterialTheme.dimens.grid.x6
                        ),
                        onClick = onEditAboutPressed,
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(id = R.string.profile_edit_button),
                    tint = iconColor.value,
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.grid.x3)
                )
            }
        }
    }
    // endregion

    // region appBar
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TopAppBar(
            title = {
                TitleText(
                    text = stringResource(id = R.string.profile_title),
                    color = textColor.value
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_arrow),
                        tint = iconColor.value,
                    )
                }
            },
            actions = {
                if (isOwner && editingContent == null) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(id = R.string.profile_edit_button),
                        tint = iconColor.value,
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.grid.x3)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    bounded = false,
                                    radius = MaterialTheme.dimens.grid.x6
                                ),
                                onClick = onEditPressed,
                            )
                    )
                } else if (editingContent != null) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = stringResource(id = R.string.profile_edit_button),
                        tint = iconColor.value,
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.grid.x3)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    bounded = false,
                                    radius = MaterialTheme.dimens.grid.x6
                                ),
                                onClick = onNormalViewPressed,
                            )
                    )
                }
            },
            colors = topBarColor,
        )
        if (animationBreakpointBool) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = MaterialTheme.dimens.grid.x1)
            ) {
                ProfileContentSelector.values().forEach {
                    val selected = selectorState == it
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = MaterialTheme.dimens.borderGrid.x1,
                                end = if (it != ProfileContentSelector
                                    .values()
                                    .last()
                                ) MaterialTheme.dimens.borderGrid.x1 else 0.dp
                            )
                            .background(MaterialTheme.colorScheme.tertiary)
                            .clickable {
                                onSelectorPressed(it)
                            },
                    ) {
                        val spacerHeight =
                            animateDpAsState(targetValue = if (selected) MaterialTheme.dimens.grid.x1 else 0.dp)
                        Text(
                            text = when (it) {
                                ProfileContentSelector.ABOUT -> stringResource(id = R.string.profile_selector_about)
                                ProfileContentSelector.POSTS -> stringResource(id = R.string.profile_selector_posts)
                            },
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = MaterialTheme.dimens.grid.x4,
                                    bottom = if (selected) MaterialTheme.dimens.grid.x3 else MaterialTheme.dimens.grid.x4,
                                ),
                            textAlign = TextAlign.Center,
                            maxLines = ONE_LINE,
                        )
                        if (selectorState == it) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .height(spacerHeight.value)
                            )
                        }
                    }
                }
            }
        }
    }
    // endregion

    if (editingContent != null && editingContent is BaseProfileViewModel.EditingContent.Loading) {
        LoadingOverlay()
    }

    // region bottom sheet
    if (showSheet.value) {
        val bottomSheetState = rememberModalBottomSheetState(true)
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = {
                showSheet.value = false
                onSheetDismiss()
            },
            containerColor = MaterialTheme.additionalColors.sheetColor,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.grid.x8),
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(MaterialTheme.dimens.borderGrid.x4)
                            .width(MaterialTheme.dimens.grid.x8)
                            .clip(RoundedCornerShape(MaterialTheme.dimens.grid.x4))
                            .background(MaterialTheme.additionalColors.sheetHandle)

                    )
                }
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.navigationBarsPadding()
            ) {
                when (editingContent) {
                    is BaseProfileViewModel.EditingContent.About -> {
                        val focusRequester = remember { FocusRequester() }
                        val keyboard = LocalSoftwareKeyboardController.current
                        Column(
                            modifier = Modifier
                                .height(MaterialTheme.dimens.sheetSizes.normal)
                                .clickable {
                                    keyboard?.show()
                                    focusRequester.requestFocus()
                                },
                        ) {
                            FocusableTextField(
                                text = editingContent.newAbout,
                                label = stringResource(id = R.string.profile_edit_about_label),
                                focusRequester = focusRequester,
                                onValueChange = onAboutTextChanged,
                                textFieldColors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onTertiary,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onTertiary,
                                    cursorColor = MaterialTheme.colorScheme.onTertiary,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedLabelColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                ),
                            )
                        }
                        SubmitButton(
                            label = stringResource(R.string.profile_submit_change),
                            enabled = editingContent.newAbout != about,
                            onSubmission = {
                                showSheet.value = false
                                onAboutTextChangeSubmit()
                            }
                        )
                    }

                    is BaseProfileViewModel.EditingContent.Image -> {
                        Button(
                            onClick = onChangeImagePressed,
                            modifier = Modifier.padding(horizontal = MaterialTheme.dimens.grid.x2),
                        ) {
                            Image(
                                painterResource(id = R.drawable.image_icon),
                                contentDescription = stringResource(id = R.string.profile_change_image_description),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.dimens.grid.x1,
                                        horizontal = MaterialTheme.dimens.grid.x3,
                                    )
                                    .size(MaterialTheme.staticDimens.imageSizes.tiny),
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            editingContent.newImagePath?.let {
                                if (imagePath == it) {
                                    CircularNetworkImage(
                                        modifier = Modifier.padding(MaterialTheme.dimens.grid.x2),
                                        imageUrl = userImagePath,
                                        imageSize = MaterialTheme.dimens.imageSizes.large,
                                        imageDescription = stringResource(id = R.string.profile_profile_picture),
                                        borderColor = MaterialTheme.additionalColors.onSheetBorder,
                                    )
                                } else {
                                    Image(
                                        bitmap = BitmapFactory.decodeFile(it).asImageBitmap(),
                                        contentDescription = stringResource(id = R.string.profile_profile_picture),
                                        modifier = Modifier
                                            .padding(MaterialTheme.dimens.grid.x2)
                                            .size(MaterialTheme.dimens.imageSizes.large)
                                            .clip(CircleShape)
                                            .border(
                                                MaterialTheme.dimens.borderGrid.x2,
                                                MaterialTheme.additionalColors.onSheetBorder,
                                                CircleShape,
                                            ),
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = {
                                showSheet.value = false
                                onChangeImageSubmit()
                            },
                            enabled = editingContent.newImagePath != imagePath,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = MaterialTheme.disabledAlpha
                                ),
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(id = R.string.profile_edit_button),
                                tint = if (editingContent.newImagePath != imagePath) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.additionalColors.onDisabledButton,
                                modifier = Modifier
                                    .padding(
                                        vertical = MaterialTheme.dimens.grid.x1,
                                        horizontal = MaterialTheme.dimens.grid.x3,
                                    )
                                    .size(MaterialTheme.staticDimens.imageSizes.tiny),
                            )
                        }
                    }

                    null -> throw Exception("Null edit")
                    else -> throw StateException()
                }
            }
        }
    }
    // endregion
}
