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
    var colorClassArray = intArrayOf(Color.GREEN, Color.BLUE, Color.YELLOW)
    var statsArray = floatArrayOf(4f, 60f, 49f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile_stats, container, false)
        stackedChart = rootView.findViewById(R.id.stacked_HorizontalBarChart_stats)
        val barDataSet = BarDataSet(dataValuesStats(statsArray), "Bar Set")
        barDataSet.setColors(*colorClassArray)
        val barData = BarData(barDataSet)
        stackedChart?.setData(barData)!!

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
        barDataSet.setDrawValues(false)

        getUserGameState {
            var backlog: Float = it[1]
            var playing: Float = it[0]
            var done: Float = it[2]
            rootView.tV_num_playing!!.text= playing.toInt().toString()
            rootView.tV_num_done!!.text= done.toInt().toString()
            rootView.tV_num_backlog!!.text= backlog.toInt().toString()
        }

        //update numbers in textviews
        //rootView.tV_num_playing!!.text= statsArray.get(0).toInt().toString()
        //rootView.tV_num_done!!.text= statsArray.get(1).toInt().toString()
        //rootView.tV_num_backlog!!.text= statsArray.get(2).toInt().toString()
        //rootView.tV_num_dropped!!.text= statsArray.get(3).toInt().toString()
        //rootView.tV_num_plan_to_play!!.text= statsArray.get(4).toInt().toString()


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