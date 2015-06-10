    package com.kohlerbear.whowascnscalc;

import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class TabPrototype extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    CustomPagerAdapter mCustomPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    static CustomViewPager mViewPager;

    //for sake of determining scroll direction
    static int lastPage;


    //nav drawer vars
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    //First screen dependencies
    static String origin = "first"; //is first by default
    static String formattedDate = "12-25-14";//default value should actually be the current date, mess with these params //TODO
    static String[] liftPattern =  {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest" };//placeholder values

     //Second screen dependencies
    static String benchTM;
    static String squatTM;
    static String ohpTM;
    static String deadTM;
    static String numberOfCycles;
    static String unitMode;
    static AlertDialog builder;// = new AlertDialog.Builder(TabPrototype.this).create();
    static String nextFrag;

   //Third screen dependencies
    static String cycle;
    static String liftType;
    static String frequency;
    static String firstLift;
    static String secondLift;
    static String thirdLift;
    static String date;
    static String viewMode;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_prototype);
        builder = new AlertDialog.Builder(TabPrototype.this).create();

        //Set up our navigation drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml

        set(navMenuTitles, navMenuIcons);
        navMenuIcons.recycle();


        //Colors
        //hack away at action bar title color
        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(Color.WHITE);
            }
        }
        getActionBar().setTitle("New Projection");
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setBackgroundColor(ColorManager.getInstance(getApplicationContext()).getPrimaryColor());
        ColorManager.clear();

//        titleStrip.setBackgroundColor();


        // Set up the action bar.
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mViewPager.getAdapter().getPageTitle(lastPage).toString().toUpperCase().equals("SET TMS")
                        && mViewPager.getAdapter().getPageTitle(position).toString().toUpperCase().equals("PROJECT")) {
                    nextFrag = "third";
                    final SecondScreenFragment fragment = (SecondScreenFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + lastPage);
                    final ThirdScreenFragment fragment2 = (ThirdScreenFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
                    final EventsDataSQLHelper eventsData = new EventsDataSQLHelper(TabPrototype.this);
                    final ConfigTool ct = new ConfigTool(getApplicationContext());
                    final int finalPos = position;
                    if (fragment.validatePattern() == false || fragment.canMoveToThird() == false) {
                        mViewPager.setCurrentItem(lastPage);//stay on second screen
//                        actionBar.setSelectedNavigationItem(lastPage);
                    } else { //otherwise there are no errors when moving from first to second screen
                        getSupportFragmentManager()
                                .beginTransaction()
                                .detach(fragment2)
                                .attach(fragment2)
                                .commit();//recreate view
                        fragment2.setInsertStatus(false);
                        mViewPager.setCurrentItem(finalPos);
                        DialogInterface.OnClickListener resetListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        mViewPager.setCurrentItem(finalPos);//go to third screen
/*                                        fragment2.setInsertStatus(false);
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .detach(fragment2)
                                                .attach(fragment2)
                                                .commit();//recreate viEW*/
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
//                                        mViewPager.setCurrentItem(lastPage);
                                        dialog.cancel();
                                        break;
                                }
                            }
                        };
                        SQLiteDatabase db = eventsData.getWritableDatabase();
                        builder.setMessage("Overwrite existing projection?");
                        builder.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", resetListener);
                        builder.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", resetListener);
/*                        if (!ct.dbEmpty()) {
//                            mViewPager.setCurrentItem(lastPage);
                            builder.show();
                        }
                        else {
                            builder.show();
                            builder.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                            }*/
                    }
                } else {
                    lastPage = position;
//                    actionBar.setSelectedNavigationItem(position);
                    nextFrag = "first";
                    mViewPager.setCurrentItem(position);
                }

            }

        });

    }


    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new FirstScreenFragment();
                case 1:
                    return new SecondScreenFragment();//SECOND SCREEN FRAGMENT CURRENTLY LOADING WITH BOGUS VALUES, make them reload (or even initialize) via onViewShowed or something?
                case 2:
                    return new ThirdScreenFragment();
                default:
                    return new FirstScreenFragment();
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Set Date";
                case 1:
                    return "Set TMs";
                case 2:
                    return "Project";
                default:
                    return "tabError";
            }
        }
    }

}
