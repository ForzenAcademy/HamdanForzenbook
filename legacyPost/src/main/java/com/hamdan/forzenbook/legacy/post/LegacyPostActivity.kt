package com.hamdan.forzenbook.legacy.post

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import coil.load
import com.hamdan.forzenbook.core.launchGalleryImageGetter
import com.hamdan.forzenbook.core.saveBitmapFromUri
import com.hamdan.forzenbook.legacy.core.view.utils.DialogUtils
import com.hamdan.forzenbook.legacy.core.viewmodels.LegacyPostViewModel
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel
import com.hamdan.forzenbook.post.core.viewmodel.asContentOrNull
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ActivityLegacyPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LegacyPostActivity : ComponentActivity() {
    private lateinit var binding: ActivityLegacyPostBinding
    private val postViewModel: LegacyPostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                postViewModel.viewModelScope.launch {
                    saveBitmapFromUri(
                        it,
                        this@LegacyPostActivity,
                        {
                            postViewModel.updateImage(null)
                        },
                    ) { file ->
                        postViewModel.updateImage(file.path)
                    }
                }
            }
        }

        binding = ActivityLegacyPostBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            val setPillImage: () -> Unit = {
                inputPostText.setText("")
                pillLeftFrame.isEnabled = false
                textToggle.isChecked = false
                pillRightFrame.isEnabled = true
                imageToggle.isChecked = true
            }
            val setPillText: () -> Unit = {
                pillLeftFrame.isEnabled = true
                textToggle.isChecked = true
                pillRightFrame.isEnabled = false
                imageToggle.isChecked = false
            }
            val showImageContent: (Boolean) -> Unit = { showBottomButton ->
                postLoadIndicator.isVisible = false
                setPillImage()
                if (showBottomButton) {
                    postChooseImage.isVisible = true
                    scrollPost.isVisible = false
                    postChosenImage.isVisible = false
                    postChangeImage.isVisible = false
                } else {
                    postChooseImage.isVisible = false
                    scrollPost.isVisible = false
                    postChosenImage.isVisible = true
                    postChangeImage.isVisible = true
                }
            }
            val showTextContent = {
                setPillText()
                postLoadIndicator.isVisible = false
                postChooseImage.isVisible = false
                scrollPost.isVisible = true
                postChosenImage.isVisible = false
                postChangeImage.isVisible = false
            }
            val loadingContent = {
                setPillText()
                postLoadIndicator.isVisible = true
                postChooseImage.isVisible = false
                scrollPost.isVisible = false
                postChosenImage.isVisible = false
                postChangeImage.isVisible = false
            }
            val errorContent = {
                setPillText()
                postLoadIndicator.isVisible = false
                postChooseImage.isVisible = false
                scrollPost.isVisible = false
                postChosenImage.isVisible = false
                postChangeImage.isVisible = false
            }
            postLayoutToolBar.setNavigationOnClickListener {
                postViewModel.backButtonPressed(this@LegacyPostActivity)
            }
            postLayoutToolBar.inflateMenu(R.menu.post_menu)
            postLayoutToolBar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.postSendIcon) {
                    postViewModel.sendPostClicked()
                    true
                } else {
                    false
                }
            }
            postPill.setOnClickListener {
                postViewModel.toggleClicked()
                val currentFocus = this@LegacyPostActivity.currentFocus
                if (currentFocus != null) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
            }

            postChangeImage.setOnClickListener {
                launchGalleryImageGetter(launcher)
            }
            postChooseImage.setOnClickListener {
                launchGalleryImageGetter(launcher)
            }

            postViewModel.viewModelScope.launch(Dispatchers.IO) {
                postViewModel.state.collect { state ->
                    withContext(Dispatchers.Main) {
                        when (state) {
                            is BasePostViewModel.PostState.Content -> {
                                if (state.content is BasePostViewModel.PostContent.Text) {
                                    showTextContent()
                                } else {
                                    (state.content as BasePostViewModel.PostContent.Image).let {
                                        showImageContent(it.filePath.isNullOrEmpty())
                                        it.filePath?.let { path ->
                                            postChosenImage.isVisible = true
                                            postChosenImage.load(path)
                                        } ?: {
                                            postChosenImage.isVisible = false
                                        }
                                    }
                                }
                            }

                            BasePostViewModel.PostState.Error -> {
                                errorContent()
                                DialogUtils.standardAlertDialog(
                                    this@LegacyPostActivity,
                                    getString(R.string.generic_error_title),
                                    getString(R.string.post_error),
                                    getString(R.string.generic_dialog_confirm)
                                ) {
                                    postViewModel.dialogDismissClicked()
                                }
                            }

                            BasePostViewModel.PostState.InvalidLogin -> {
                                postViewModel.kickToLogin(this@LegacyPostActivity)
                            }

                            BasePostViewModel.PostState.Loading -> {
                                loadingContent()
                            }
                        }
                    }
                }
            }

            inputPostText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    postTextBody.hint = ""
                } else {
                    if (inputPostText.text.isNullOrEmpty()) {
                        postTextBody.hint = getString(R.string.post_text_label)
                    }
                }
            }
            inputPostText.addTextChangedListener {
                if (postViewModel.state.value.asContentOrNull() is BasePostViewModel.PostContent.Text) {
                    postViewModel.updateText(it.toString())
                }
            }
        }
    }
}
