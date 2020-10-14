package com.example.gamelogger.helpers

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.models.Game
import com.example.gamelogger.ui.mygamelist.GamelistAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Game>?) {
    val adapter = recyclerView.adapter as GamelistAdapter
    adapter.submitList(data)
}