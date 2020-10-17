package com.example.gamelogger.helpers

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.ui.gamesearch.SearchListAdapter
import com.example.gamelogger.ui.mygamelist.GamelistAdapter

@BindingAdapter("gameListData")
fun bindGameListRecyclerView(recyclerView: RecyclerView, data: List<Game>?) {
    val adapter = recyclerView.adapter as GamelistAdapter
    adapter.submitList(data)
}

@BindingAdapter("searchListData")
fun bindSearchListRecyclerViews(recyclerView: RecyclerView, data: List<Game>?) {
    val adapter = recyclerView.adapter as SearchListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_baseline_broken_image_24))
            .into(imgView)
    }
}

