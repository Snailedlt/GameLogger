package com.example.gamelogger.ui.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamelogger.R
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.fragment_profile_platforms.view.*
import java.util.ArrayList

class ProfilePlatformsFragment : Fragment() {

    var stackedChart: HorizontalBarChart? = null
    var colorClassArray = intArrayOf(Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED, Color.LTGRAY)
    var platformsArray = floatArrayOf(2f, 119f, 13f, 26f, 5f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile_platforms, container, false)
        stackedChart = rootView.findViewById(R.id.stacked_HorizontalBarChart_platforms)

        val barDataSet = BarDataSet(dataValuesPlatforms(platformsArray), "Bar Set")
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

        //update numbers in textviews
        rootView.tV_num_android!!.text= platformsArray.get(0).toInt().toString()
        rootView.tV_num_playstation_4!!.text= platformsArray.get(1).toInt().toString()
        rootView.tV_num_xbox_one!!.text= platformsArray.get(2).toInt().toString()
        rootView.tV_num_nintendo_switch!!.text= platformsArray.get(3).toInt().toString()
        rootView.tV_num_pc!!.text= platformsArray.get(4).toInt().toString()


        return rootView}

    companion object{
        fun newInstance() = ProfilePlatformsFragment()
    }


    private fun dataValuesPlatforms(floatArray: FloatArray): ArrayList<BarEntry> {
        val dataVals = ArrayList<BarEntry>()
        dataVals.add(BarEntry(0f, floatArray))
        return dataVals
    }
}