package com.example.profilside.ui.mygamelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.profilside.R
import kotlinx.android.synthetic.main.fragment_gamelist.*

class MygamelistFragment : AppCompatActivity() {

    private lateinit var gameList: ArrayList<Game>
    private lateinit var linLayoutManager: RecyclerView.LayoutManager
    private lateinit var gamelistAdapter: RecyclerView.Adapter<*>
    private lateinit var gamelistRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_gamelist)

        gameList = Game.makeGameList()
        linLayoutManager = LinearLayoutManager(this)
        gamelistAdapter = GamelistAdapter(this, gameList)

        gamelistRecyclerView = findViewById<RecyclerView>(R.id.mygamelist).apply {
            setHasFixedSize(true)
            layoutManager = linLayoutManager
            adapter = gamelistAdapter
        }
    }

//    private lateinit var mygamelistViewModel: MygamelistViewModel
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? {
//        mygamelistViewModel =
//                ViewModelProviders.of(this).get(MygamelistViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_gamelist, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        mygamelistViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        return root
//    }
}