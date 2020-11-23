package com.example.gamelogger.ui.gamelistdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gamelogger.R
import com.example.gamelogger.databinding.FragmentGamelistDetailBinding
import com.example.gamelogger.ui.mygamelist.MygamelistViewModel

/**
 * A simple [Fragment] subclass.
 */
class GamelistDetail : Fragment() {


    private lateinit var viewModel: GamelistDetailViewModel
    private lateinit var viewModelFactory: GamelistDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGamelistDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_gamelist_detail, container, false)
        val args = GamelistDetailArgs.fromBundle(requireArguments())
        val gameId = args.gameId

        viewModelFactory = GamelistDetailViewModelFactory(GamelistDetailArgs.fromBundle(requireArguments()).gameId)
        viewModel = ViewModelProvider(this, viewModelFactory)
        .get(GamelistDetailViewModel::class.java)

        binding.gamelistDetailViewModel = viewModel


        binding.lifecycleOwner = viewLifecycleOwner


        //Shows a toast of the gameId belonging to the game the user clicked
        Toast.makeText(context, "Game Id: ${gameId}", Toast.LENGTH_LONG).show()
        //Logs the gamId to console
        Log.i("GamelistDetail","Game Id: ${gameId}")

        //Sets the text of aboutTitle to the gameId retrieved from GamelistDetailArgs
        //binding.aboutTitle.text = args.gameId.toString()

        Log.i("GamelistDetailsFragment", "Called ViewModelProvider.get")

        return binding.root
    }
}