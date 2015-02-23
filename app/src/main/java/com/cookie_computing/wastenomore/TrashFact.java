package com.cookie_computing.wastenomore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class TrashFact extends ActionBarActivity { //extends FragmentActivity {
//        // When requested, this adapter returns a TrashFactFragment,
//        // representing an object in the collection.
//        FactPagerAdapter trashFactPagerAdapter;
//        ViewPager viewPager;
//
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_trash_fact);
//
//            // ViewPager and its adapters use support library
//            // fragments, so use getSupportFragmentManager.
//            trashFactPagerAdapter =
//                    new FactPagerAdapter(
//                            getSupportFragmentManager());
//            viewPager = (ViewPager) findViewById(R.id.pager);
//            viewPager.setAdapter(trashFactPagerAdapter);
//        }
//    }
//
//    // Since this is an object collection, use a FragmentStatePagerAdapter,
//    // and NOT a FragmentPagerAdapter.
//    public class FactPagerAdapter extends FragmentPagerAdapter {
//        public FactPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            Fragment fragment = new FactObjectFragment();
//            Bundle args = new Bundle();
//            // Our object is just an integer :-P
//            args.putInt(FactObjectFragment.ARG_OBJECT, i + 1);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 100;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "OBJECT " + (position + 1);
//        }
//    }
//
//    // Instances of this class are fragments representing a single
//    // object in our collection.
//    public static class FactObjectFragment extends Fragment {
//        public static final String ARG_OBJECT = "object";
//
//        @Override
//        public View onCreateView(LayoutInflater inflater,
//                                 ViewGroup container, Bundle savedInstanceState) {
//            // The last two arguments ensure LayoutParams are inflated
//            // properly.
//            View rootView = inflater.inflate(
//                    R.layout.fragment_collection_object, container, false);
//            Bundle args = getArguments();
//            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                    Integer.toString(args.getInt(ARG_OBJECT)));
//            return rootView;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_fact);

        // Get the intent
        Intent intent = getIntent();

        String fact = "The average person generates over 4 pounds of trash every day and " +
                "about 1.5 tons of solid waste per year.";
        // Get the text view and set text
        final TextView textView = (TextView) findViewById(R.id.trashFact);
        textView.setText(fact);

        String source = getResources().getString(R.string.trash_fact_1_source);
        final TextView textSourceView = (TextView) findViewById(R.id.trashFactSource);
        textSourceView.setText(source);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trash_fact, menu);
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

    /** Called when the user clicks the Home button */
    public void goHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Next Fact button */
    public void nextFact(View view) {
        Intent intent = new Intent(this, TrashFact2.class);
        startActivity(intent);
    }
}
