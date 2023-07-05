package com.hamdan.forzenbook.legacy.core.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.ui.core.R
import com.hamdan.forzenbook.ui.core.databinding.ImagePostBinding
import com.hamdan.forzenbook.ui.core.databinding.LoadPostBinding
import com.hamdan.forzenbook.ui.core.databinding.TextPostBinding

class FeedAdapter(
    private val getDataAt: (Int) -> FeedItemViewModel,
    private val getDataSize: () -> Int,
    private val getDataType: (Int) -> Int,
    private val onLoadMore: () -> Unit,
    private val onNameClick: (Context, Int) -> Unit,
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            FeedItemViewModel.IMAGE -> {
                val binding = ImagePostBinding.inflate(inflater, parent, false)
                ImageViewHolder(binding, onNameClick)
            }

            FeedItemViewModel.TEXT -> {
                val binding = TextPostBinding.inflate(inflater, parent, false)
                TextViewHolder(binding, onNameClick)
            }

            FeedItemViewModel.LOADING_MORE -> {
                val binding = LoadPostBinding.inflate(inflater, parent, false)
                LoadViewHolder(binding, onLoadMore)
            }

            else -> throw RuntimeException("Type of view not supported, view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getDataAt(position))
    }

    override fun getItemCount(): Int = getDataSize()

    override fun getItemViewType(position: Int): Int =
        getDataType(position)
}

abstract class FeedItemViewModel(val type: Int) {
    companion object {
        const val IMAGE = 0
        const val TEXT = 1
        const val LOADING_MORE = 2
        // don't believe theres a need for a loading item for the TextItem and ImageItem as coil can handle the loading situation
    }
}

class ImageFeedItemViewModel(
    val profilePictureUri: String?,
    val firstName: String,
    val lastName: String,
    val date: String,
    val location: String,
    val posterId: Int,
    val imageUri: String,
) : FeedItemViewModel(IMAGE)

class TextFeedItemViewModel(
    val profilePictureUri: String?,
    val firstName: String,
    val lastName: String,
    val date: String,
    val location: String,
    val posterId: Int,
    val postText: String,
) : FeedItemViewModel(TEXT)

class LoadFeedItemViewModel() : FeedItemViewModel(LOADING_MORE)

abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(model: FeedItemViewModel)
}

class ImageViewHolder(
    private val binding: ImagePostBinding,
    private val onNameClick: (Context, Int) -> Unit
) :
    ViewHolder(binding.root) {
    override fun bind(model: FeedItemViewModel) {
        val imageModel = model as ImageFeedItemViewModel
        val context = binding.root.context
        binding.apply {
            feedUserName.text = this.root.context.getString(
                R.string.user_name,
                imageModel.firstName,
                imageModel.lastName
            )
            feedUserName.setOnClickListener {
                onNameClick(context,imageModel.posterId)
            }
            feedUserDate.text = imageModel.date
            feedUserLocation.text = imageModel.location
            val imageLoader = ImageLoader(itemView.context)
            val profilePictureRequest = ImageRequest.Builder(itemView.context)
                .data(imageModel.profilePictureUri)
                .target(
                    onStart = {
                        postProfileIndicator.isVisible = true
                    },
                    onSuccess = { image ->
                        postProfileIndicator.isVisible = false
                        feedProfilePicture.setImageDrawable(image)
                        postProfilePictureHolder.isVisible = true
                    },
                    onError = {
                        postProfileIndicator.isVisible = false
                        feedProfilePicture.setImageResource((R.drawable.logo_render_full_notext))
                        postProfilePictureHolder.isVisible = true
                    }
                )
                .build()
            val imageRequest = ImageRequest.Builder(itemView.context)
                .data(imageModel.imageUri)
                .target(
                    onStart = { postImageIndicator.isVisible = true },
                    onSuccess = { image ->
                        postImageIndicator.isVisible = false
                        feedPostImage.setImageDrawable(image)
                        feedPostImage.isVisible = true
                    },
                    onError = {
                        postImageIndicator.isVisible = false
                        feedPostImage.setImageResource((R.drawable.logo_render_full_notext))
                        feedPostImage.isVisible = true
                    }
                )
                .build()
            imageLoader.enqueue(profilePictureRequest)
            imageLoader.enqueue(imageRequest)
        }
    }
}

class TextViewHolder(
    private val binding: TextPostBinding,
    private val onNameClick: (Context, Int) -> Unit
) :
    ViewHolder(binding.root) {
    override fun bind(model: FeedItemViewModel) {
        val textModel = model as TextFeedItemViewModel
        val context = binding.root.context
        binding.apply {
            feedUserName.text = this.root.context.getString(
                R.string.user_name,
                textModel.firstName,
                textModel.lastName
            )
            feedUserName.setOnClickListener {
                onNameClick(context,textModel.posterId)
            }
            feedUserDate.text = textModel.date
            feedUserLocation.text = textModel.location
            feedPostText.text = textModel.postText
            val imageLoader = ImageLoader(itemView.context)
            val profilePictureRequest = ImageRequest.Builder(itemView.context)
                .data(textModel.profilePictureUri)
                .target(
                    onStart = { postProfileIndicator.isVisible = true },
                    onSuccess = { image ->
                        postProfileIndicator.isVisible = false
                        feedProfilePicture.setImageDrawable(image)
                        postProfilePictureHolder.isVisible = true
                    },
                    onError = {
                        postProfileIndicator.isVisible = false
                        feedProfilePicture.setImageResource((R.drawable.logo_render_full_notext))
                        postProfilePictureHolder.isVisible = true
                    }
                )
                .build()
            imageLoader.enqueue(profilePictureRequest)
        }
    }
}

class LoadViewHolder(private val binding: LoadPostBinding, val onLoadMore: () -> Unit) :
    ViewHolder(binding.root) {
    override fun bind(model: FeedItemViewModel) {
        onLoadMore()
    }
}

fun PostData.toFeedItemViewModel(): FeedItemViewModel {
    return when (this.type) {
        PostData.TEXT_TYPE -> {
            TextFeedItemViewModel(
                profilePictureUri = this.posterIcon,
                firstName = this.posterFirstName,
                lastName = this.posterLastName,
                date = this.date,
                location = this.posterLocation,
                posterId = this.posterId,
                postText = this.body,
            )
        }

        PostData.IMAGE_TYPE -> {
            ImageFeedItemViewModel(
                profilePictureUri = this.posterIcon,
                firstName = this.posterFirstName,
                lastName = this.posterLastName,
                date = this.date,
                location = this.posterLocation,
                posterId = this.posterId,
                imageUri = GlobalConstants.BASE_URL + this.body,
            )
        }

        else -> {
            LoadFeedItemViewModel()
        }
    }
}
