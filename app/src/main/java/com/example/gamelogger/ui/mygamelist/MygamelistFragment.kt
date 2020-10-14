package com.example.gamelogger.ui.mygamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamelogger.databinding.FragmentGamelistBinding

class MygamelistFragment : Fragment() {
/*
    private lateinit var gameList: ArrayList<Game>
    private lateinit var linLayoutManager: RecyclerView.LayoutManager
    private lateinit var gamelistAdapter: RecyclerView.Adapter<*>
    private lateinit var gamelistRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_gamelist, container, false)
        val rvGamelist = rootView.findViewById(R.id.mygamelist) as RecyclerView
        linLayoutManager = LinearLayoutManager(context)
        rvGamelist.adapter = GamelistAdapter(GameApi.retrofitService.getGameList())
        rvGamelist.layoutManager = linLayoutManager

        return rootView
    }*/

    private val viewModel: MygamelistViewModel by lazy {
        ViewModelProvider(this).get(MygamelistViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGamelistBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Gives binding access to the MygamelistViewModel
        binding.viewModel = viewModel

        /**
         * [mygameList] corresponds to the id of the RecyclerView from the layout file
         */
        binding.mygameList.adapter = GamelistAdapter()

        return binding.root
    }
}