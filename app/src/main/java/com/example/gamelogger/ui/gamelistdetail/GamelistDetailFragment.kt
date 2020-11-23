package com.example.gamelogger.ui.gamelistdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.gamelogger.R
import com.example.gamelogger.databinding.FragmentGamelistDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class GamelistDetail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGamelistDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_gamelist_detail, container, false)

        val args = GamelistDetailArgs.fromBundle(requireArguments())
        Toast.makeText(context, "Game Id: ${args.gameId}", Toast.LENGTH_LONG).show()
        Log.i("GamelistDetail","Game Id: ${args.gameId}")

        return binding.root
    }
}