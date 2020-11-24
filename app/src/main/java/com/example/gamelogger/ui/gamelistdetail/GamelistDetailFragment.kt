package com.example.gamelogger.ui.gamelistdetail

import android.os.Build
import android.os.Bundle
import android.text.Html
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

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        /** Setting up LiveData observation relationship **/
        viewModel.game.observe(viewLifecycleOwner, Observer { newAbout ->
            //Kode for å bruke HTML tekst i et textview, hentet fra: https://stackoverflow.com/questions/2116162/how-to-display-html-in-textview
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.about.text= Html.fromHtml(newAbout.about, Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.about.text= Html.fromHtml(newAbout.about);
            }
        })


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