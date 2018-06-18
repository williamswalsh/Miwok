package com.example.android.miwok;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Class which represents the Main Activity of the application.
 * The ListViews, Fragments and View Pager will be displayed within this activity.
 *
 * <p>
 * Author:      William Walsh
 * Version:     2.0 (Fragments)
 * Date:        19-6-2018
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Get a reference to the ActionBar
        ActionBar actionBar = MainActivity.this.getSupportActionBar();
        
        // Set the elevation of the action bar to 0 so that it appears unelevated from the activity screen.
        actionBar.setElevation(0);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager);

        // Create an adapter that knows which fragment should be shown on each page
        // Pass in the MainActivity Context and a FragmentManager
        SimpleFragmentPagerAdapter pagerAdapter = new SimpleFragmentPagerAdapter(this,getSupportFragmentManager());

        // Set the adapter to the view pager
        viewPager.setAdapter(pagerAdapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }
}
