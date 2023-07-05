package com.hamdan.forzenbook.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.forzenbook.search.results.compose.SearchResultContent
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookBottomNavigationBar
import com.hamdan.forzenbook.compose.core.theme.ForzenBookTheme
import com.hamdan.forzenbook.core.GlobalConstants.NAVIGATION_ERROR
import com.hamdan.forzenbook.core.GlobalConstants.NAVIGATION_QUERY
import com.hamdan.forzenbook.core.GlobalConstants.NAVIGATION_USERID
import com.hamdan.forzenbook.core.NavBarItem
import com.hamdan.forzenbook.core.NavigationItem
import com.hamdan.forzenbook.core.launchGalleryImageGetter
import com.hamdan.forzenbook.core.saveBitmapFromUri
import com.hamdan.forzenbook.createaccount.compose.CreateAccountContent
import com.hamdan.forzenbook.login.compose.MainLoginContent
import com.hamdan.forzenbook.mainpage.compose.FeedPage
import com.hamdan.forzenbook.post.compose.Post
import com.hamdan.forzenbook.profile.compose.Profile
import com.hamdan.forzenbook.search.compose.SearchContent
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.view.NavigationDestinations.CREATE_ACCOUNT_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.FEED_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.LOGIN_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.POST_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.PROFILE_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.SEARCH_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.SEARCH_RESULTS_PAGE
import com.hamdan.forzenbook.viewmodels.CreateAccountViewModel
import com.hamdan.forzenbook.viewmodels.FeedViewModel
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import com.hamdan.forzenbook.viewmodels.PostViewModel
import com.hamdan.forzenbook.viewmodels.ProfileViewModel
import com.hamdan.forzenbook.viewmodels.SearchResultViewModel
import com.hamdan.forzenbook.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForzenbookActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val createAccountViewModel: CreateAccountViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchResultViewModel: SearchResultViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val postLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                postViewModel.viewModelScope.launch {
                    saveBitmapFromUri(
                        uri = it,
                        context = this@ForzenbookActivity,
                        onError = {
                            postViewModel.updateImage(null)
                        },
                        onImageSaved = { file ->
                            postViewModel.updateImage(file.path)
                        }
                    )
                }
            }
        }
        val profileLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                profileViewModel.viewModelScope.launch {
                    saveBitmapFromUri(
                        uri = it,
                        context = this@ForzenbookActivity,
                        onError = {
                            profileViewModel.onImageChanged(null)
                        },
                        onImageSaved = { file ->
                            profileViewModel.onImageChanged(file.path)
                        }
                    )
                }
            }
        }
        val navigationItems = listOf(NAVBAR_HOME, NAVBAR_SEARCH, NAVBAR_PROFILE)
        loginViewModel.checkLoggedIn()
        setContent {
            ForzenBookTheme {
                val bottomNav: @Composable () -> Unit =
                    { ForzenbookBottomNavigationBar(navIcons = navigationItems) }
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        startDestination = LOGIN_PAGE,
                    ) {
                        composable(LOGIN_PAGE) {
                            MainLoginContent(
                                state = loginViewModel.state.value,
                                onInfoDismiss = {
                                    loginViewModel.loginDismissInfoClicked()
                                },
                                onErrorDismiss = {
                                    loginViewModel.loginDismissErrorClicked()
                                },
                                onTextChange = { entry ->
                                    loginViewModel.updateText(entry)
                                },
                                onSubmission = {
                                    loginViewModel.loginClicked()
                                },
                                onCreateAccountPress = {
                                    navController.navigate(CREATE_ACCOUNT_PAGE)
                                }
                            ) {
                                navController.navigate(FEED_PAGE)
                            }
                        }
                        composable(CREATE_ACCOUNT_PAGE) {
                            CreateAccountContent(
                                state = createAccountViewModel.state.value,
                                onErrorDismiss = {
                                    createAccountViewModel.createAccountDismissErrorClicked()
                                },
                                onTextChange = { firstName, lastName, birthDate, email, location ->
                                    createAccountViewModel.updateCreateAccountTextAndErrors(
                                        firstName,
                                        lastName,
                                        birthDate,
                                        email,
                                        location,
                                    )
                                },
                                onDateFieldClick = { createAccountViewModel.createAccountDateDialogClicked() },
                                onDateSubmission = { createAccountViewModel.createAccountDateDialogSubmitClicked() },
                                onDateDismiss = { createAccountViewModel.createAccountDateDialogDismiss() },
                                onSubmission = { createAccountViewModel.createAccountClicked() },
                                onNavigateUp = { createAccountViewModel.navigateUpPressed() },
                            )
                        }
                        composable(FEED_PAGE) {
                            // Todo remove later based on implementation requirements
                            LaunchedEffect(Unit) {
                                if (feedViewModel.state.value.posts.isEmpty()) feedViewModel.loadMore()
                            }
                            FeedPage(
                                state = feedViewModel.state.value,
                                onRequestMorePosts = { feedViewModel.loadMore() },
                                onIconClick = { id ->
                                    navController.navigate("$PROFILE_PAGE/$id")
                                    profileViewModel.onProfilePress(id)
                                },
                                onNameClick = { id ->
                                    searchViewModel.onNameClicked(
                                        id,
                                        {
                                            navController.navigate(
                                                SEARCH_RESULTS_PAGE + searchViewModel.navigateUser(
                                                    id
                                                ) + "false"
                                            )
                                        },
                                    ) {
                                        navController.navigate(
                                            SEARCH_RESULTS_PAGE + searchViewModel.navigateUser(
                                                id
                                            ) + "true"
                                        )
                                    }
                                },
                                bottomBar = bottomNav,
                                onCreatePostClicked = {
                                    navController.navigate(POST_PAGE)
                                },
                                onErrorDismiss = {
                                    feedViewModel.onErrorDismiss()
                                }
                            ) {
                                loginViewModel.kickBackToLogin(this@ForzenbookActivity)
                                navController.navigate(LOGIN_PAGE) {
                                    popUpTo(LOGIN_PAGE)
                                    launchSingleTop = true
                                }
                                feedViewModel.kickBackToLogin()
                            }
                        }
                        composable(SEARCH_PAGE) {
                            SearchContent(
                                state = searchViewModel.state.value,
                                onSearchTextChange = {
                                    searchViewModel.onUpdateSearch(it)
                                },
                                onSubmitSearch = {
                                    searchViewModel.onSearchSubmit(
                                        onSuccess = {
                                            navController.navigate(SEARCH_RESULTS_PAGE + searchViewModel.navigateQuery() + "false")
                                        },
                                        onError = {
                                            navController.navigate(SEARCH_RESULTS_PAGE + searchViewModel.navigateQuery() + "true")
                                        },
                                    )
                                },
                                onErrorDismiss = {
                                    searchViewModel.onErrorDismiss()
                                }
                            ) {
                                loginViewModel.kickBackToLogin(this@ForzenbookActivity)
                                searchViewModel.kickBackToLogin()
                                navController.navigate(LOGIN_PAGE) {
                                    popUpTo(LOGIN_PAGE)
                                    launchSingleTop = true
                                }
                            }
                        }
                        composable(
                            "$SEARCH_RESULTS_PAGE/{$NAVIGATION_USERID}/{$NAVIGATION_QUERY}/{$NAVIGATION_ERROR}",
                            arguments = listOf(
                                navArgument(NAVIGATION_USERID) { type = NavType.IntType },
                                navArgument(NAVIGATION_QUERY) { type = NavType.StringType },
                                navArgument(NAVIGATION_ERROR) { type = NavType.BoolType },
                            ),
                        ) {
                            LaunchedEffect(Unit) {
                                it.arguments?.let { bundle ->
                                    val query = bundle.getString(NAVIGATION_QUERY)
                                    val userId = bundle.getInt(NAVIGATION_USERID)
                                    val pageError = bundle.getBoolean(NAVIGATION_ERROR)
                                    if (!pageError) {
                                        if (userId == -1 && !query.isNullOrEmpty()) {
                                            searchResultViewModel.getResultsByQuery(
                                                query,
                                            )
                                        } else {
                                            searchResultViewModel.getResultsById(
                                                userId,
                                            )
                                        }
                                    } else {
                                        searchResultViewModel.errorDuringSearch()
                                    }
                                }
                            }
                            SearchResultContent(
                                state = searchResultViewModel.state.value,
                                onIconClick = { id ->
                                    navController.navigate("$PROFILE_PAGE/$id")
                                    profileViewModel.onProfilePress(id)
                                },
                                onNameClick = { id ->
                                    searchViewModel.onNameClicked(
                                        id,
                                        {
                                            navController.navigate(
                                                SEARCH_RESULTS_PAGE + searchViewModel.navigateUser(
                                                    id
                                                ) + "false"
                                            )
                                        },
                                    ) {
                                        navController.navigate(
                                            SEARCH_RESULTS_PAGE + searchViewModel.navigateUser(
                                                id
                                            ) + "true"
                                        )
                                    }
                                },
                                bottomBar = bottomNav,
                                onErrorDismiss = { searchViewModel.onErrorDismiss() }
                            ) {
                                loginViewModel.kickBackToLogin(this@ForzenbookActivity)
                                searchResultViewModel.kickBackToLogin()
                                navController.navigate(LOGIN_PAGE) {
                                    popUpTo(LOGIN_PAGE)
                                    launchSingleTop = true
                                }
                            }
                        }
                        composable(POST_PAGE) {
                            Post(
                                state = postViewModel.state.value,
                                onTextChange = { postViewModel.updateText(it) },
                                onToggleClicked = { postViewModel.toggleClicked() },
                                onDialogDismiss = { postViewModel.dialogDismissClicked() },
                                onGalleryClicked = { launchGalleryImageGetter(postLauncher) },
                                onSendClicked = {
                                    postViewModel.sendPostClicked()
                                }
                            ) {
                                loginViewModel.kickBackToLogin(this@ForzenbookActivity)
                                postViewModel.kickBackToLogin()
                                navController.navigate(LOGIN_PAGE) {
                                    popUpTo(LOGIN_PAGE)
                                    launchSingleTop = true
                                }
                            }
                        }
                        composable(
                            "$PROFILE_PAGE/{$NAVIGATION_USERID}",
                            arguments = listOf(
                                navArgument(NAVIGATION_USERID) {
                                    type = NavType.StringType
                                    nullable = true
                                },
                            ),
                        ) {
                            LaunchedEffect(Unit) {
                                it.arguments?.let { bundle ->
                                    val userId = bundle.getString(NAVIGATION_USERID)
                                    if (userId.isNullOrEmpty()) {
                                        profileViewModel.onPersonalProfilePress()
                                    } else {
                                        profileViewModel.onProfilePress(userId.toInt())
                                    }
                                }
                            }
                            Profile(
                                state = profileViewModel.state.value,
                                onErrorDismiss = { profileViewModel.onErrorDismiss() },
                                onSelectorPressed = { profileViewModel.onSelectorPressed(it) },
                                onBackPressed = {
                                    profileViewModel.onBackPressed()
                                    navController.navigateUp()
                                },
                                onEditProfileImagePressed = {
                                    profileViewModel.onEditProfileImagePressed()
                                },
                                onChangeProfileImagePressed = {
                                    launchGalleryImageGetter(profileLauncher)
                                },
                                onChangeImageSubmit = {
                                    profileViewModel.onImageSubmit()
                                },
                                onAboutTextChanged = {
                                    profileViewModel.onEditAboutTextChange(it)
                                },
                                onAboutTextChangeSubmit = {
                                    profileViewModel.onEditAboutTextSubmit()
                                },
                                onEditSubmitPressed = {
                                    profileViewModel.onEditSubmitPressed()
                                },
                                onEditPressed = {
                                    profileViewModel.onEditPressed()
                                },
                                onEditAboutPressed = {
                                    profileViewModel.onEditAboutPressed()
                                },
                                onSheetDismiss = {
                                    profileViewModel.onSheetDismiss()
                                },
                                onTopReached = {
                                    profileViewModel.onPostsScrollUp()
                                },
                                onBottomReached = {
                                    profileViewModel.onPostsScrollDown()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        val NAVBAR_HOME =
            NavigationItem(
                NavBarItem.FEED,
                FEED_PAGE,
                R.drawable.home_icon,
                R.string.home_nav_text,
                R.string.home_nav_button,
            )
        val NAVBAR_SEARCH = NavigationItem(
            NavBarItem.SEARCH,
            SEARCH_PAGE,
            R.drawable.search_icon,
            R.string.search_nav_text,
            R.string.search_nav_button,
        )
        val NAVBAR_PROFILE = NavigationItem(
            NavBarItem.PROFILE,
            PROFILE_PAGE,
            R.drawable.profile_navigation_icon,
            R.string.profile_nav_text,
            R.string.profile_nav_button,
        )
    }
}
