package com.example.profilside.ui.mygamelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.profilside.R

class MygamelistFragment : Fragment() {

    private lateinit var mygamelistViewModel: MygamelistViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mygamelistViewModel =
                ViewModelProviders.of(this).get(MygamelistViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gamelist, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        mygamelistViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}