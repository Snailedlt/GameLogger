package com.example.gamelogger.ui.mygamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelogger.R
import com.example.gamelogger.databinding.FragmentGamelistBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_gamelist.*

/**
 * Fragment class for My game list
 */
class MygamelistFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var spinner: Spinner

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

        // gives instructions to the clicklisteners handled by the recyclerview's adapter
        val adapter = GamelistAdapter(
            GameButtonListener { game, state ->
                viewModel.changeGameState(game, state) // changes the clicked game's curent state
                binding.mygameList.adapter?.notifyDataSetChanged() // ensures that the interface is updated to reflect any changes
            },
            GameCardListener { game ->
                viewModel.onGamelistDetailClicked(game)
            }
        )

        val recyclerView = binding.mygameList

        // initializes an ItemTouchHelper object to enable swiping cards to delete objects
        val helper = ItemTouchHelper(
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not implemented but method override is required")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.LEFT) {
                        val indexOfGame = viewHolder.adapterPosition
                        viewModel.removeGame(indexOfGame)
                        // notifies adapter when and where a game is removed to
                        // let the interface reflect the changes
                        adapter.notifyItemRemoved(indexOfGame)
                        if (viewModel.lastInteractedGame.value != null) {
                            showSnackBar(adapter, indexOfGame)
                        }
                    }
                }
            }
        )
        helper.attachToRecyclerView(recyclerView.findViewById(R.id.mygame_list))

        // mygameList corresponds to the id of the RecyclerView from the layout file
        binding.mygameList.adapter = adapter

        // Add an Observer on the state variable for Navigating when the game image is pressed.
        viewModel.navigateToGameListDetail.observe(viewLifecycleOwner, { game ->
            game?.let {
                this.findNavController().navigate(
                    MygamelistFragmentDirections
                        .actionNavigationGameslistToGamelistDetail(game.id)
                )
                viewModel.onGamelistDetailNavigated()
            }
        })

        // Sets the layoutmanager for the fragment
        binding.mygameList.layoutManager = LinearLayoutManager(context)

        // Make the whole searchview clickable, instead of just the icon
        searchView = binding.root.findViewById(R.id.searchBar)
        searchView.setOnClickListener { searchView.isIconified = false }

        // Spinner that lets you sort the list
        spinner = binding.root.findViewById(R.id.sortSpinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // observes the gamelist in the viewmodel and sends it in a function
                viewModel.games.observe(viewLifecycleOwner, { gamelost ->
                    // Function that sort and adds the list
                    viewModel.sortMyGamesList(spinner, gamelost)
                    viewModel.games.removeObservers(viewLifecycleOwner);
                    adapter.notifyDataSetChanged();
                })
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        return binding.root
    }

    // Show snackbar when deleting a game
    private fun showSnackBar(adapter: GamelistAdapter, position: Int) {
        val snackbar = view?.let {
            Snackbar
                .make(
                    it.findViewById(R.id.mygamelistLayout),
                    "${viewModel.lastInteractedGame.value?.title} deleted from your list",
                    Snackbar.LENGTH_LONG
                )
                .setAction(
                    "UNDO",
                    View.OnClickListener {
                        viewModel.undoRemoveGame(position)
                        adapter.notifyItemInserted(position)
                        restoreScrollPositionAfterAdded()
                    }
                )
        }
        // Code to make the snackbar not overlap the navigation bar
        val bottomNavigationView = this.view?.rootView?.findViewById<BottomNavigationView>(R.id.nav_view)
        snackbar?.anchorView = bottomNavigationView
        // Animates the snackbar with a fade in instead of sliding in from the bottom
        snackbar?.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE

        snackbar?.show()
    }

    // Scroll the view up if an element is added at index 0
    // https://stackoverflow.com/questions/43114837/notifyiteminserted-not-working-when-inserting-first-item-at-zero-index
    private fun restoreScrollPositionAfterAdded() {
        val recyclerView = this.activity?.mygame_list
        val layoutManager = recyclerView?.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (firstVisibleItemPosition == 0) {
            layoutManager.scrollToPosition(0)
        }
    }
}