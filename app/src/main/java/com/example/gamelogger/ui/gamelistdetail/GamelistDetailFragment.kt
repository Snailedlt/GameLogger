package com.example.gamelogger.ui.gamelistdetail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gamelogger.R
import com.example.gamelogger.databinding.FragmentGamelistDetailBinding
import com.example.gamelogger.helpers.listToPresentableString

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

        /** Setting up LiveData observation relationship between the clicked game object, and the views in fragment_gamelist_detail.xml**/
        viewModel.game.observe(viewLifecycleOwner, { newGame ->
            //Kode for å bruke HTML tekst i et textview, hentet fra følgende stackoverflow svar: https://stackoverflow.com/a/2116191/12206312
            when {
                newGame.about == null -> binding.gameAbout.text = "N/A"
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    binding.gameAbout.text= Html.fromHtml(newGame.about, Html.FROM_HTML_MODE_COMPACT)
                }
                else -> {
                    binding.gameAbout.text= Html.fromHtml(newGame.about)
                }
            }

            //Kode for å sette metascore i UI'et, og sørger for å vise "N/A" dersom spillet ikke har en metascore
            if(newGame.metacritic != null) //setter metascore i UI'et til newGame.metascore dersom newGame.metascore != null
                binding.gameMetascore.text= newGame.metacritic.toString()
            else //setter metascore i UI'et til "N/A" ellers
                binding.gameMetascore.text = "N/A"

            //Kode for å sette release date i UI'et, og sørger for å vise "N/A" dersom spillet ikke har en releasedate
            if(newGame.released != null) //setter release date i UI'et til newGame.released dersom newGame.released != null
                binding.gameReleaseDate.text= newGame.released.toString()
            else //setter release date i UI'et til "N/A" ellers
                binding.gameReleaseDate.text = "N/A"

            //Kode for å sette genre i UI'et, og sørger for å vise "N/A" dersom spillet ikke har en noen sjangre
            if(newGame.genresList != null) //setter genre i UI'et til newGame.genresFormatted dersom newGame.genresFormatted != null
                binding.gameGenre.text= listToPresentableString(newGame.genresList!!)
            else //setter genre i UI'et til "N/A" ellers
                binding.gameGenre.text = "N/A"

            //Kode for å sette platform i UI'et, og sørger for å vise "N/A" dersom spillet ikke har en noen plattform
            if(newGame.platformsList != null) //setter platform i UI'et til newGame.plattform dersom newGame.plattform != null
                binding.gamePlatforms.text= listToPresentableString(newGame.platformsList!!)
            else //setter platform i UI'et til "N/A" ellers
                binding.gamePlatforms.text = "N/A"

            //Log.i("ObserverMetaScore", "${binding.gameMetascore.text}")
        })

        //Shows a toast of the gameId belonging to the game the user clicked
        //Toast.makeText(context, "Game Id: ${gameId}", Toast.LENGTH_LONG).show()
        //Logs the gamId to console
        Log.i("GamelistDetail","Game Id: $gameId")

        //Sets the text of aboutTitle to the gameId retrieved from GamelistDetailArgs
        //binding.aboutTitle.text = args.gameId.toString()

        Log.i("GamelistDetailsFragment", "Called ViewModelProvider.get")

        return binding.root
    }
}