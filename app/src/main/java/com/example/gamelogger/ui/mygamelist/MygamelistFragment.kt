package com.example.gamelogger.ui.mygamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameState
import com.example.gamelogger.databinding.FragmentGamelistBinding

class MygamelistFragment : Fragment() {

    /**
     * Initializes the [MygamelistViewModel]
     */
    private val viewModel: MygamelistViewModel by lazy {
        ViewModelProvider(this).get(MygamelistViewModel::class.java)
    }

    /**
     * Inflates the layout with databinding and sets its lifecycle owner to the Mygamelistfragment
     * to enable data binding to observe LiveData, and sets up the RecyclerView with an adapter
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // FragmentGamelistBinding corresponds to the fragment_gamelist.xml layout file.
        // it is automatically generated when a Data field is defined in the layout file,
        val binding = FragmentGamelistBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Gives binding access to the MygamelistViewModel
        binding.viewModel = viewModel



        /**
         * mygameList corresponds to the id of the RecyclerView from the layout file
         */
        binding.mygameList.adapter = GamelistAdapter(GamelistAdapter.OnClickListener {
            viewModel.changeGameState(it)
        })
        binding.mygameList.layoutManager = LinearLayoutManager(context)

        return binding.root
    }
}