package com.example.gamelogger.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.gamelogger.R
import com.example.gamelogger.services.getUser
import kotlinx.android.synthetic.main.fragment_profile.view.*
import me.relex.circleindicator.CircleIndicator

/**
 * This fragment hosts the viewpager that will use a FragmentPagerAdapter to display child fragments.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        // Henter og bytter username, tar litt for lang tid
        getUser {
            root.text_username.text = it
        }

        val viewPager = root.findViewById(R.id.view_pager2) as ViewPager
        // Important: Must use the child FragmentManager or you will see side effects.
        viewPager.adapter = MyAdapter(childFragmentManager)

        val indicator = root.findViewById<CircleIndicator>(R.id.indicator) as CircleIndicator
        indicator.setViewPager(viewPager)





        return root
    }

    @Suppress("DEPRECATION")
    class MyAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment {
            return if(position == 0) ProfileStatsFragment.newInstance() else ProfilePlatformsFragment.newInstance()
        }

        override fun getPageTitle(position: Int): CharSequence = "Tab $position"
    }

    companion object {
        val TAG: String = ProfileFragment::class.java.name
    }

}
