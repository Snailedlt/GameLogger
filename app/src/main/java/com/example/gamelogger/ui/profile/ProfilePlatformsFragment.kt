package com.example.gamelogger.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gamelogger.R
import com.example.gamelogger.helpers.intToRGB
import com.example.gamelogger.services.getUserGamePlatform
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*

class ProfilePlatformsFragment : Fragment() {

    var stackedChart: HorizontalBarChart? = null
    var prevID1: Int = 1
    var prevID2: Int = 1
    var prevID3: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile_platforms, container, false)
        stackedChart = rootView.findViewById(R.id.stacked_HorizontalBarChart_platforms)
        val layout: RelativeLayout = rootView.findViewById(R.id.wrapper)

        getUserGamePlatform {
            var colorClassArrayList = arrayListOf<Int>()
            var platformNamesMutableList = mutableListOf<String>()
            var platformCountsArrayList = arrayListOf<Float>()


            var imageView: View // imageview to the Left
            var textView2: View // texview to the Right of textView
            var textView3: View // texview to the Right

            var count = 1
            /* Iterates through the hashMap provided by getUserGamePlatform, and puts the keys and
             * values into two seperate ArrayLists. The key corresponds to the platform name,
             * and the value corresponds to the number of games the user owns, that is of a certain platform
             */
            for((key, value) in it) {
                /* Takes the key (which is a String) as input and converts it into a hashCode: Int.
                 * Then uses intToRGB method to convert from Int to a String Hexcode in #RRGGBB format
                 * Lastly ColorTemplate.rgb is used to parse the RGB color from a string to an int value
                */
                var color = ColorTemplate.rgb("#" + intToRGB(key.hashCode()))
                platformNamesMutableList.add(key) // adds platformname to the end of the platformNamesArrayList
                platformCountsArrayList.add(value) // adds value to the end of the platformCountsArrayList
                colorClassArrayList.add(color) //adds color to the end of the colorClassArrayList

                // imageview to the left
                imageView = ImageView(context)
                imageView.id =  View.generateViewId()
                imageView.setBackgroundResource(R.drawable.ic_circle_black);
                val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.addRule(RelativeLayout.BELOW, prevID2)
                params.addRule(RelativeLayout.ALIGN_PARENT_START)
                imageView.setLayoutParams(params)
                layout.addView(imageView)

                //textview to the right of imageView
                textView2 = TextView(context)
                textView2.id =  View.generateViewId()
                textView2.text = key
                val params3 = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                if(count>1) params3.addRule(RelativeLayout.BELOW, prevID2)
                params3.addRule(RelativeLayout.END_OF, imageView.id)
                params3.setMargins(100, 0, 0, 0)
                textView2.setLayoutParams(params3)
                layout.addView(textView2)

                //textview to the right
                textView3 = TextView(context)
                textView3.id =  View.generateViewId()
                textView3.text = value.toInt().toString()
                val params2 = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                if(count>1) params2.addRule(RelativeLayout.BELOW, prevID2)
                params2.addRule(RelativeLayout.ALIGN_PARENT_END)
                textView3.setLayoutParams(params2)
                layout.addView(textView3)

                prevID1 = imageView.id
                prevID2 = textView2.id
                prevID3 = textView3.id

                count++
            }
            val platformCountsArray = FloatArray(platformCountsArrayList.size)
            var index = 0
            for (value in platformCountsArrayList) {
                platformCountsArray[index++] = value
            }
            val barDataSet = BarDataSet(dataValuesPlatforms(platformCountsArray), "Bar Set")
            val barData = BarData(barDataSet)

            barDataSet.setDrawValues(false)
            barDataSet.colors = colorClassArrayList
            stackedChart?.setData(barData)!!
            stackedChart?.invalidate()
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