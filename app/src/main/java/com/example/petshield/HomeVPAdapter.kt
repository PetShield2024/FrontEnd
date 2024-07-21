package com.example.petshield

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class HomeVPAdapter (private val imageResIds: IntArray) : RecyclerView.Adapter<HomeVPAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(imageResIds[position])
    }

    override fun getItemCount(): Int = imageResIds.size

    class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}