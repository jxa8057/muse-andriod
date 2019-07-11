package com.example.finalproject.activities

import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.example.finalproject.R
import com.example.finalproject.tabs.AboutFragment
import com.example.finalproject.tabs.AllRecordingsFragment
import com.example.finalproject.tabs.CalendarFragment
import com.example.finalproject.tabs.RecordFragment
import kotlinx.android.synthetic.main.activity_main_tabbed.*

class MainTabbedActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tabbed)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        tabs.setupWithViewPager(viewPager)

        // Set up the ViewPager with the sections adapter.
        viewPager.adapter = mSectionsPagerAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_tabbed, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_about) {
            viewPager.setCurrentItem(3, false)
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            when (position) {
                0 -> return RecordFragment.newInstance()
                1 -> return AllRecordingsFragment.newInstance()
                2 -> return CalendarFragment.newInstance()
                3 -> return AboutFragment.newInstance()
            }
            return RecordFragment.newInstance()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Record"
                1 -> return "Recordings"
                2 -> return "Calendar"
                3 -> return "About"
            }
            return ""
        }

        override fun getCount(): Int {
            // Show 4 total pages.
            return 4
        }
    }
}
