package com.example.gamelogger.helpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.ui.gamesearch.SearchListAdapter
import com.example.gamelogger.ui.gamesearch.SearchStatus
import com.example.gamelogger.ui.mygamelist.GamelistAdapter
import com.example.gamelogger.ui.mygamelist.ListStatus

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

@BindingAdapter("searchStatus")
fun bindSearchListStatus(statusImageView: ImageView, status: SearchStatus?) {
    when (status) {
        SearchStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        SearchStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_baseline_error_24)
        }
        SearchStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        SearchStatus.EMPTY -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_baseline_search_24)
        }
    }
}

@BindingAdapter("searchStatus2")
fun bindSearchListStatus2(statusTextView: TextView, status: SearchStatus?) {
    if (status == SearchStatus.EMPTY) {
        statusTextView.visibility = View.VISIBLE
        statusTextView.text = "Use the search bar to search for games"
    } else statusTextView.visibility = View.GONE
}

@BindingAdapter("listStatusImage")
fun bindGameListStatusImage(statusImageView: ImageView, status: ListStatus?) {
    when (status) {
        ListStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ListStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_baseline_error_24)
        }
        ListStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        ListStatus.EMPTY -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_baseline_list_alt_24)
        }
    }
}

@BindingAdapter("listStatusText")
fun bindGameListStatusText(statusTextView: TextView, status: ListStatus?) {
    if (status == ListStatus.EMPTY) {
        statusTextView.visibility = View.VISIBLE
        statusTextView.text = "Your list is empty\nGo to Add Game to add games to your list!"
    } else statusTextView.visibility = View.GONE
}