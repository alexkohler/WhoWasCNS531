package com.kohlerbear.whowascnscalc;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


/**
 *MainActivity - contains DatePicker for user's beginning cycle
 *
 */

public class FirstScreenFragment extends Fragment {


    public static DatePicker dp;
    static Calendar startingCal = new GregorianCalendar();//for sake of getting starting days
    static int startingDateDay = startingCal.getTime().getDay();
    static int startingDateMonth = startingCal.getTime().getMonth();
    static int startingDateYear = startingCal.getTime().getYear();
    DrawerLayout drawerLayout;
    String[] defaultPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"};
    String[] liftPattern = new String[7];
    TextView liftTicker;
    Tracker tracker = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();
        DrawerLayout        drawerLayout    = (DrawerLayout)    inflater.inflate(R.layout.activity_main, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
        //Initialize tracker
        GoogleAnalytics.getInstance(getActivity()).getTracker("UA-55018534-1");
        tracker = GoogleAnalytics.getInstance(getActivity()).getTracker("UA-55018534-1");
        HashMap<String, String> hitParameters = new HashMap<String, String>();
        hitParameters.put(Fields.HIT_TYPE, "appview");
        hitParameters.put(Fields.SCREEN_NAME, "Home Screen");

        tracker.send(hitParameters);

        dp = (DatePicker) drawerLayout.findViewById(R.id.dp);
        dp.setCalendarViewShown(false);
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        TabPrototype.formattedDate = (currentMonth + 1) + "-" + currentDay + "-" + currentYear;
        dp.init(currentYear, currentMonth, currentDay, new DatePicker.OnDateChangedListener()
        {

            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
//                Toast.makeText(getActivity(), "Date Selected: " + (dp.getMonth() + 1) + "-" + dp.getDayOfMonth() + "-" + dp.getYear(),  Toast.LENGTH_SHORT).show();
                startingDateDay = dp.getDayOfMonth();
                startingDateMonth = dp.getMonth();
                startingDateYear = dp.getYear();

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(startingDateYear, startingDateMonth, startingDateDay);

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
                Date myDate = cal.getTime();
                String formattedDate = dateFormat.format(myDate);
                EventsDataSQLHelper eventsData = new EventsDataSQLHelper(/*this*/getActivity());
                SQLiteDatabase db = eventsData.getWritableDatabase();
                //TODO Look into a better way of upgrading tables, you might want to do this on secm
                db.execSQL("drop table Lifts");
                db.execSQL("create table Lifts (liftDate text not null, Cycle integer, Lift text not null, Frequency text not null, First_Lift real, Second_Lift real, Third_Lift real, Training_Max integer, column_LbFlag integer, RoundFlag integer, pattern text not null)");
                TabPrototype.formattedDate = formattedDate;
                TabPrototype.origin = "first";
                TabPrototype.liftPattern = liftPattern;
                db.close();
            }
        });

        liftTicker = (TextView) drawerLayout.findViewById(R.id.liftTicker);

        //TODO is stuff below needed?
        Intent patternAdjusterIntent = getActivity().getIntent();
        String origin = patternAdjusterIntent.getStringExtra("origin");


        if (origin != null)//if we came from patternAdjuster
            liftPattern = patternAdjusterIntent.getStringArrayExtra("liftPattern");
        else
            liftPattern = defaultPattern;

        String liftTickerBuffer = "Current lift Pattern";
        for (int i=0; i < liftPattern.length; i++)
            liftTickerBuffer = liftTickerBuffer + " " + liftPattern[i].substring(0, 1) + " - ";
        liftTickerBuffer = liftTickerBuffer.substring(0, liftTickerBuffer.length() - 2); //remove last dash

        liftTicker.setText(liftTickerBuffer);

        return drawerLayout; // We must return the loaded Layout
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(getActivity()).activityStart(getActivity());  // Add this method.
    }
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(getActivity()).activityStop(getActivity());  // Add this method.
        updateData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Make sure that we are currently visible
        if (this.isVisible()) {

            // If we are becoming invisible, then...
            if (!isVisibleToUser) {
                updateData();
            }
        }
    }



        private void updateData()
    {
        startingDateDay = dp.getDayOfMonth();
        startingDateMonth = dp.getMonth();
        startingDateYear = dp.getYear();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(startingDateYear, startingDateMonth, startingDateDay);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
        Date myDate = cal.getTime();
        String formattedDate = dateFormat.format(myDate);
        TabPrototype.formattedDate = formattedDate;
        TabPrototype.origin = "first";
        TabPrototype.liftPattern = liftPattern;
    }
}
