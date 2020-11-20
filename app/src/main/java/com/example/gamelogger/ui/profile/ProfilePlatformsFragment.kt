package com.example.gamelogger.ui.profile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamelogger.R
import com.example.gamelogger.services.getUserGamePlatform
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.fragment_profile_platforms.view.*
import java.util.ArrayList

class ProfilePlatformsFragment : Fragment() {

    var stackedChart: HorizontalBarChart? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile_platforms, container, false)
        stackedChart = rootView.findViewById(R.id.stacked_HorizontalBarChart_platforms)

        getUserGamePlatform {

            val ps4: Float = it[0]
            val xb1: Float = it[1]
            val pc: Float = it[2]
            val switch: Float = it[3]
            val android: Float = it[4]

            var colorClassArray : IntArray
            var platformsArraytest : FloatArray

            if(ps4 == 0f && xb1 == 0f && pc == 0f && switch == 0f && android == 0f) { //If all platforms contain 0 games, show a gray graph
                platformsArraytest = floatArrayOf(1f)
                colorClassArray = intArrayOf(Color.GRAY)
            }
            else {
                platformsArraytest = floatArrayOf(ps4, xb1, pc, switch, android)
                colorClassArray = intArrayOf(Color.BLUE, Color.YELLOW , Color.MAGENTA, Color.RED, Color.GREEN)
            }

            val barDataSet = BarDataSet(dataValuesPlatforms(platformsArraytest), "Bar Set")
            val barData = BarData(barDataSet)

            barDataSet.setDrawValues(false)
            barDataSet.setColors(*colorClassArray)
            stackedChart?.setData(barData)!!
            stackedChart?.invalidate()

            //update numbers in textviews
            rootView.tV_num_android!!.text = android.toInt().toString()
            rootView.tV_num_playstation_4!!.text = ps4.toInt().toString()
            rootView.tV_num_xbox_one!!.text = xb1.toInt().toString()
            rootView.tV_num_nintendo_switch!!.text = switch.toInt().toString()
            rootView.tV_num_pc!!.text = pc.toInt().toString()
        }


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

        return rootView
    }

    companion object {
        fun newInstance() = ProfilePlatformsFragment()
    }


    private fun dataValuesPlatforms(floatArray: FloatArray): ArrayList<BarEntry> {
        val dataVals = ArrayList<BarEntry>()
        dataVals.add(BarEntry(0f, floatArray))
        return dataVals
    }


}