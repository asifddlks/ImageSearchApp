package com.asifddlks.imagesearchapp.viewadapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asifddlks.imagesearchapp.R
import com.asifddlks.imagesearchapp.databinding.ItemImageCellBinding
import com.asifddlks.imagesearchapp.model.ImageModel
import com.asifddlks.imagesearchapp.views.FullscreenImageActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.gson.Gson

class ImageAdapter(
    private val activity: Activity,
    private val context: Context,
    private val listener: OnItemClickListener
) :
    PagingDataAdapter<ImageModel, ImageAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemImageCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class PhotoViewHolder(private val binding: ItemImageCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)

                        //snapshot().items

                        binding.imageView.transitionName = "thumbnailTransition"

                        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity,
                            binding.imageView,
                            "transition_name"
                        )

                        val intent = Intent(context, FullscreenImageActivity::class.java)

                        val bundle = Bundle()
                        val listAsString = Gson().toJson(snapshot().items)
                        bundle.putInt("position", position)
                        bundle.putString("listAsString", listAsString)
                        intent.putExtras(bundle)
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }

        fun bind(photo: ImageModel) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                //textViewUserName.text = photo.description
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: ImageModel)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<ImageModel>() {
            override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel) =
                oldItem == newItem
        }
    }
}