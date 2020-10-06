package com.example.profilside.ui.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.profilside.R
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.ArrayList

class ProfilePlatformsFragment : Fragment() {

    var stackedChart: HorizontalBarChart? = null
    var colorClassArray = intArrayOf(Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED, Color.LTGRAY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile_platforms, container, false)
        stackedChart = rootView.findViewById(R.id.stacked_HorizontalBarChart_platforms)
        val barDataSet = BarDataSet(dataValuesPlatforms(), "Bar Set")
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
        return rootView}

    companion object{
        fun newInstance() = ProfilePlatformsFragment()
    }


    private fun dataValuesPlatforms(): ArrayList<BarEntry> {
        val dataVals = ArrayList<BarEntry>()
        dataVals.add(BarEntry(0f, floatArrayOf(2f, 119f, 13f, 26f, 5f)))
        return dataVals
    }
}