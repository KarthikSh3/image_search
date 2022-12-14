package com.karthik.imagesearch.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.karthik.imagesearch.R
import com.karthik.imagesearch.data.model.Photo
import com.karthik.imagesearch.databinding.ItemBinding


class ImagePagingAdapter :
    PagingDataAdapter<Photo, ImagePagingAdapter.ImageViewHolder>(ImageComparator) {

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val photo = getItem(position)
        photo?.let {
            holder.view.ivImage.load(it.getImageUrl()) {
                placeholder(R.drawable.place_holder)
            }
            holder.view.tvTitle.text = it.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBinding.inflate(inflater, parent, false)

        return ImageViewHolder(binding)
    }

    class ImageViewHolder(val view: ItemBinding) : RecyclerView.ViewHolder(view.root)

    object ImageComparator : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }
}