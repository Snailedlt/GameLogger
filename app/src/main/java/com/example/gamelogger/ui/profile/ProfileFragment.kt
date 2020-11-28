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

        //Uses ViewPager instead of ViewPager 2, since ViewPager2 does not support childFragmentManager
        val viewPager = root.findViewById(R.id.view_pager2) as ViewPager
        // Important: Must use the child FragmentManager or you will see side effects.
        viewPager.adapter = MyAdapter(childFragmentManager)

        val indicator = root.findViewById<CircleIndicator>(R.id.indicator) as CircleIndicator
        indicator.setViewPager(viewPager)

        return root
    }

    /**
     * Deprecation warnings is supressed, since the updated version does not support this niche use, where we have to access a fragment's grandchild.
     * There is a guide on migrating from ViewPager to ViewPager2 (https://developer.android.com/training/animation/vp2-migration), but it does not
     * address the migration from childFragmentManager to an updated alternative. I went down quite the rabbithole to try and fix the issue, but there
     * seems to be no simple solution to fixing this issue. Therefore we chose to opt for this deprecated library, simply to get it working.
     */
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
