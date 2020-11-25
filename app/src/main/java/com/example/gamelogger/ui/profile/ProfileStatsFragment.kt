package com.example.gamelogger.ui.profile

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamelogger.R
import com.example.gamelogger.services.getUserGameState
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.fragment_profile_stats.view.*
import java.util.ArrayList

class ProfileStatsFragment : Fragment() {

    var stackedChart: HorizontalBarChart? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile_stats, container, false)
        stackedChart = rootView.findViewById(R.id.stacked_HorizontalBarChart_stats)


        //Remove gridlines and labels
        stackedChart?.xAxis!!.isEnabled = false
        stackedChart?.axisLeft!!.isEnabled = false
        stackedChart?.axisRight!!.isEnabled = false
        stackedChart?.description!!.isEnabled = false
        stackedChart?.legend!!.isEnabled = false
        stackedChart?.isDragEnabled = false
        stackedChart?.setTouchEnabled(false)
        stackedChart?.setScaleEnabled(false)
        stackedChart?.isScaleXEnabled = false
        stackedChart?.isScaleYEnabled = false
        stackedChart?.setPinchZoom(false)


        getUserGameState {
            val backlog: Float = it[1]
            val playing: Float = it[0]
            val done: Float = it[2]

            //update numbers in textviews
            rootView.tV_num_playing!!.text= playing.toInt().toString()
            rootView.tV_num_done!!.text= done.toInt().toString()
            rootView.tV_num_backlog!!.text= backlog.toInt().toString()

            var colorClassArray : IntArray
            var statusArrayFirebase : FloatArray

            if(backlog == 0f && playing == 0f && done == 0f) { //If all playlists contain 0 games, show a gray graph
                statusArrayFirebase = floatArrayOf(1f)
                colorClassArray = intArrayOf(Color.GRAY)
            }
            else {
                statusArrayFirebase = floatArrayOf(done, playing, backlog)
                colorClassArray = intArrayOf(Color.GREEN, Color.BLUE, Color.YELLOW)
            }
            val barDataSet = BarDataSet(dataValuesStats(statusArrayFirebase), "Bar Set")
            barDataSet.setColors(*colorClassArray)
            val barData = BarData(barDataSet)
            barDataSet.setDrawValues(false)
            stackedChart?.setData(barData)!!
            stackedChart?.invalidate()
        }

        return rootView
    }

    companion object{
        fun newInstance() = ProfileStatsFragment()
    }



    private fun dataValuesStats(floatArray: FloatArray): ArrayList<BarEntry> {
        val dataVals = ArrayList<BarEntry>()
        dataVals.add(BarEntry(0f, floatArray))
        return dataVals
    }
}