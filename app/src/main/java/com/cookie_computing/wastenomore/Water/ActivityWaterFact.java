package com.cookie_computing.wastenomore.Water;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cookie_computing.wastenomore.R;


public class ActivityWaterFact extends FragmentActivity {

    // When requested, this adapter returns a TrashFactFragment,
    // representing an object in the collection.
    WaterPagerAdapter waterFactPagerAdapter;
    ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_fact);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        waterFactPagerAdapter = new WaterPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.water_pager);
        viewPager.setAdapter(waterFactPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_water_fact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the Home button
     */
    public void goHome(View view) {
        Intent intent = new Intent(this, ActivityWaterHome.class);
        startActivity(intent);
    }

    public class WaterPagerAdapter extends FragmentPagerAdapter {
        public WaterPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new WaterObjectFragment();
            Bundle args = new Bundle();
            // Our object is just an integer
            args.putInt(WaterObjectFragment.ARG_PAGE_NUM, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }


    // Instances of this class are fragments representing a single
    // object in our collection.
    public static class WaterObjectFragment extends Fragment {

        public static final String ARG_PAGE_NUM = "page number";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated properly.
            View rootView = inflater.inflate(R.layout.fragment_fact, container, false);
            Bundle args = getArguments();

            int page = args.getInt(ARG_PAGE_NUM);
            String fact = "";
            String source = "";
            if(page == 1) {
                fact = getResources().getString(R.string.water_fact_1);
                source = getResources().getString(R.string.water_fact_1_source);
            } else if(page == 2) {
                fact = getResources().getString(R.string.water_fact_2);
                source = getResources().getString(R.string.water_fact_2_source);
            } else if(page == 3) {
                fact = getResources().getString(R.string.water_fact_3);
                source = getResources().getString(R.string.water_fact_3_source);
            } else if(page == 4) {
                fact = getResources().getString(R.string.water_fact_4);
                source = getResources().getString(R.string.water_fact_4_source);
            } else if(page == 5) {
                fact = getResources().getString(R.string.water_fact_5);
                source = getResources().getString(R.string.water_fact_5_source);
            } else if(page == 6) {
                fact = getResources().getString(R.string.water_fact_6);
                source = getResources().getString(R.string.water_fact_6_source);
            } else if(page == 7) {
                fact = getResources().getString(R.string.water_fact_7);
                source = getResources().getString(R.string.water_fact_7_source);
            } else {
                fact = getResources().getString(R.string.water_fact_8);
                source = getResources().getString(R.string.water_fact_8_source);
            }
            // Get the text view and set text for fact and source
            final TextView textView = (TextView) rootView.findViewById(R.id.fact);
            textView.setText(fact);

            final TextView textSourceView = (TextView) rootView.findViewById(R.id.factSource);
            textSourceView.setText(source);

            final TextView textPageNum = (TextView) rootView.findViewById(R.id.page);
            String pageString = "Page " + page + " of 8";
            textPageNum.setText(pageString);

            return rootView;
        }
    }
}
