package com.hamdan.forzenbook.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.forzenbook.search.results.compose.SearchResultContent
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composables.ForzenbookBottomNavigationBar
import com.hamdan.forzenbook.compose.core.theme.ForzenBookTheme
import com.hamdan.forzenbook.core.NavigationItem
import com.hamdan.forzenbook.core.getBitmapFromUri
import com.hamdan.forzenbook.core.getImageFromNetwork
import com.hamdan.forzenbook.core.launchGalleryImageGetter
import com.hamdan.forzenbook.createaccount.compose.CreateAccountContent
import com.hamdan.forzenbook.login.compose.MainLoginContent
import com.hamdan.forzenbook.mainpage.compose.FeedPage
import com.hamdan.forzenbook.post.compose.Post
import com.hamdan.forzenbook.search.compose.SearchContent
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.view.NavigationDestinations.CREATE_ACCOUNT_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.FEED_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.LOGIN_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.POST_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.SEARCH_PAGE
import com.hamdan.forzenbook.view.NavigationDestinations.SEARCH_RESULTS_PAGE
import com.hamdan.forzenbook.viewmodels.CreateAccountViewModel
import com.hamdan.forzenbook.viewmodels.FeedViewModel
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import com.hamdan.forzenbook.viewmodels.PostViewModel
import com.hamdan.forzenbook.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForzenbookActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val createAccountViewModel: CreateAccountViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                getBitmapFromUri(
                    it,
                    this,
                    {
                        postViewModel.updateImage(null)
                    },
                ) {
                    postViewModel.updateImage(it.path)
                }
            }
        }
        val navigationItems = listOf(NAVBAR_HOME, NAVBAR_SEARCH)
        loginViewModel.checkLoggedIn(this)
        feedViewModel.loadMore(this@ForzenbookActivity)
        setContent {
            ForzenBookTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        startDestination = FEED_PAGE
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
                                    loginViewModel.loginClicked(this@ForzenbookActivity)
                                }
                            ) {
                                navController.navigate(CREATE_ACCOUNT_PAGE)
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
                            FeedPage(
                                state = feedViewModel.state.value,
                                onRequestMorePosts = { feedViewModel.loadMore(this@ForzenbookActivity) },
                                onImageRequestLoad = { url ->
                                    getImageFromNetwork(url, this@ForzenbookActivity)
                                },
                                bottomBar = {
                                    ForzenbookBottomNavigationBar(navIcons = navigationItems)
                                }
                            ) {
                                navController.navigate(POST_PAGE)
                            }
                        }
                        composable(SEARCH_PAGE) {
                            SearchContent()
                        }
                        composable(SEARCH_RESULTS_PAGE) {
                            SearchResultContent(
                                bottomBar = { ForzenbookBottomNavigationBar(navIcons = navigationItems) }
                            )
                        }
                        composable(POST_PAGE) {
                            Post(
                                state = postViewModel.state.value,
                                onTextChange = { postViewModel.updateText(it) },
                                onToggleClicked = { postViewModel.toggleClicked() },
                                onDialogDismiss = { postViewModel.dialogDismissClicked() },
                                onGalleryClicked = { launchGalleryImageGetter(launcher) },
                            ) {
                                postViewModel.sendPostClicked()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        val NAVBAR_HOME =
            NavigationItem(
                FEED_PAGE,
                R.drawable.baseline_home_24,
                R.string.home_nav_text,
                R.string.home_nav_button
            )
        val NAVBAR_SEARCH = NavigationItem(
            SEARCH_PAGE,
            R.drawable.search_icon,
            R.string.search_nav_text,
            R.string.search_nav_button
        )
    }
}
