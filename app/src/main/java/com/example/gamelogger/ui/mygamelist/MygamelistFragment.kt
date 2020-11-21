package com.example.gamelogger.ui.mygamelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamelogger.R
import com.example.gamelogger.classes.Game
import com.example.gamelogger.classes.GameState
import com.example.gamelogger.databinding.FragmentGamelistBinding
import kotlinx.coroutines.withTimeout


class MygamelistFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var spinner: Spinner

    /**
     * Initializes the [MygamelistViewModel]
     */
    val viewModel: MygamelistViewModel by lazy {
        ViewModelProvider(this).get(MygamelistViewModel::class.java)
    }

    // Inflates the layout with databinding and sets its lifecycle owner to the Mygamelistfragment
    // to enable data binding to observe LiveData, and sets up the RecyclerView with an adapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // FragmentGamelistBinding corresponds to the fragment_gamelist.xml layout file.
        // it is automatically generated when a Data field is defined in the layout file,
        //val binding = FragmentGamelistBinding.inflate(inflater)
        val binding: FragmentGamelistBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_gamelist, container, false
        )

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Gives binding access to the MygamelistViewModel
        binding.viewModel = viewModel

        val adapter = GamelistAdapter(GameButtonListener {
                game, state ->
            viewModel.changeGameState(game, state)
            binding.mygameList.adapter?.notifyDataSetChanged()
        }, GameImageListener{
            game ->
            Toast.makeText(context, "${game}", Toast.LENGTH_LONG).show()
        })

        // mygameList corresponds to the id of the RecyclerView from the layout file
        binding.mygameList.adapter = adapter

        // Sets the layoutmanager for the fragment
        binding.mygameList.layoutManager = LinearLayoutManager(context)

        // Make the whole searchview clickable, instead of just the icon
        searchView = binding.root.findViewById(R.id.searchBar)
        searchView.setOnClickListener { searchView.isIconified = false }

/*
        // Creates an OnItemSelectedListener, which will later be used to change the color of spinner
        val listener: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // assigns listener to sortSpinner in fragment_gamelist.xml
        spinner = binding.root.findViewById(R.id.sortSpinner)
        spinner.onItemSelectedListener = listener;
*/

        return binding.root
    }
}