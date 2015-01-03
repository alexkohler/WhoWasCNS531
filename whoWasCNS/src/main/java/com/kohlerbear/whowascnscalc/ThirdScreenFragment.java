package com.kohlerbear.whowascnscalc;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//TODO WATCH ALL YOUR REMOVEALLVIEW STUFF, IT MAY BE CAUSING TROUBLE
public class ThirdScreenFragment extends Fragment
        {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a {@link android.support.v13.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this
     * becomes too memory intensive, it may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */

    ViewPager mViewPager;

    //for nav drawer
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    EventsDataSQLHelper eventsData;
    String MODE_FORMAT;
    Integer NUMBER_CYCLES;
    static String CURRENT_SELECT_QUERY;
    static int lbMode;
    boolean insertStatus = false;
    boolean changedView = false;
    String retStringSaver; //for sake of changing views
    Cursor cursor;
    String lbmode;
    DrawerLayout drawerLayout;

//    TableRow titleTableRow;

    boolean toggleButtonCalled = false;

    Tracker tracker = null;

    public enum CURRENT_VIEW {
        DEFAULT('D'), BENCH('B'), SQUAT('S'), OHP('O'), DEAD('D'), FIVES('5'), THREES('3'), ONES('1');
        @SuppressWarnings("unused")
        private int value;

        private CURRENT_VIEW(char value) {
            this.value = value;
        }


    };


    public enum CURRENT_FREQ
    {
        FIVES ('F'), THREES ('T'), ONES ('O');

        @SuppressWarnings("unused")
        private int value;

        private CURRENT_FREQ(char value) {
            this.value = value;
        }
    }

    public enum ROW_LISTENER {NORMAL, SHIFTFORWARD, SHIFTBACKWARD};

//    String currentCycleSelected = "%";//On creation, user will always be on cycle 1.

    static CURRENT_VIEW curView = CURRENT_VIEW.DEFAULT;//start with default (show all) view (for overview)

    static String[] liftPattern;

    ROW_LISTENER rowListenerStatus = ROW_LISTENER.NORMAL;

    //initialize processor to process all lifts, dates, etc...
    DateAndLiftProcessor Processor = new DateAndLiftProcessor();


    TableLayout tableLayout;
    Button configureButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity faActivity  = (FragmentActivity)    super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        drawerLayout = (DrawerLayout)    inflater.inflate(R.layout.activity_third_screen_prototype, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here
        tableLayout = (TableLayout) drawerLayout.findViewById(R.id.tableLayout1Prototype);
        // Content of previous onCreate() here
        // ...
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        //Take care of actual third screen festivities
        configureButton = (Button) drawerLayout.findViewById(R.id.configureButtonPrototype);

//        configureButton.setBackgroundColor(Color.GRAY);
        configureButton.setTextColor(Color.WHITE);

        configureButton.setOnClickListener(optionsListener);

//        Intent intent = getIntent();
        if (TabPrototype.origin != null)
        {
            if (TabPrototype.origin.equals("dashboard"))
            {
                Processor.setRoundingFlag(true);//rounding on by default
                ConfigTool configtool = new ConfigTool(getActivity());
                eventsData = new EventsDataSQLHelper(getActivity()); //careful leaving two of these open
                TabPrototype.numberOfCycles = "5"; //TODO if this is ever configurable this will need to change
                Processor.setUnitMode(configtool.getLbModeFromDatabase());
                setQuery(null);
                Cursor cursor = getEvents();
                showDefaultEvents(cursor);
            }
        }


        // drawerLayout.findViewById(R.id.someGuiElement);00
        return drawerLayout; // We must return the loaded Layout
    }
            //called twice
            public void onPositiveButtonPressed()
            {
                Processor.setRoundingFlag(true);//rounding on by default
                String mode = TabPrototype.unitMode;//intent.getStringExtra("mode");
                Processor.setUnitMode(mode);
                eventsData.inflateTable(ThirdScreenFragment.this, TabPrototype.formattedDate, eventsData.getWritableDatabase());
                new AsyncCaller(liftPattern).execute();
            }

            @Override
            public void setUserVisibleHint(boolean isVisibleToUser) {
                super.setUserVisibleHint(isVisibleToUser);
                // Make sure that we are currently visible
                if (this.isVisible()) {
                    final String startingDate = TabPrototype.formattedDate;//intent.getStringExtra("key2");
                    liftPattern = TabPrototype.liftPattern;//intent.getStringArrayExtra("liftPattern");

                    tracker = GoogleAnalytics.getInstance(getActivity()).getTracker("UA-55018534-1");
                    HashMap<String, String> hitParameters = new HashMap<String, String>();
                    hitParameters.put(Fields.HIT_TYPE, "appview");
                    hitParameters.put(Fields.SCREEN_NAME, "Third Screen");

                    tracker.send(hitParameters);

                    eventsData = new EventsDataSQLHelper(getActivity());
                    final SQLiteDatabase db = eventsData.getWritableDatabase();

//                    titleTableRow = (TableRow) drawerLayout.findViewById(R.id.insertValuesPrototype);



                    String origin = TabPrototype.origin;//intent.getStringExtra("origin");

                    if (origin.equals("individualViews"))		//if we are coming from second screen
                    {
                        Processor.setRoundingFlag(true);
                        ConfigTool configtool = new ConfigTool(getActivity());
                        Processor.setUnitMode(configtool.getLbModeFromDatabase());
                        eventsData.reinflateTable(this);
                    }
                    else if (origin.equals("second"))
                    {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Overwrite existing projection?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onPositiveButtonPressed();
                                    }
                                })
                                .setNegativeButton("No, View existing", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        TabPrototype.mViewPager.setCurrentItem(1);
                                        Processor.setRoundingFlag(true);//rounding on by default
                                        ConfigTool configtool = new ConfigTool(getActivity());
                                        Processor.setUnitMode(configtool.getLbModeFromDatabase());
                                        setQuery(null);
                                        Cursor cursor = getEvents();
                                        showDefaultEvents(cursor);
                                    }
                                })
                                .create();
                          ConfigTool ct = new ConfigTool(getActivity());
                        if (!ct.dbEmpty())
                            builder.show();
                        else
                          {
                            onPositiveButtonPressed();
                          }

                    }
                    else//this is called during refreshes/view existing
                    {
                        Processor.setRoundingFlag(true);//rounding on by default
                        ConfigTool configtool = new ConfigTool(getActivity());
                        Processor.setUnitMode(configtool.getLbModeFromDatabase());
                        setQuery(null);
                        Cursor cursor = getEvents();
                        showDefaultEvents(cursor);

                    }

                    if (toggleButtonCalled)
                    {
                        if (Processor.getRoundingFlag())
                            Processor.setRoundingFlag(false);
                        else
                            Processor.setRoundingFlag(true);
                    }

                }
                // If we are becoming invisible, then...
                if (!isVisibleToUser) {
                    Log.d("MyFragment", "Not visible anymore.  Stopping audio.");
                    // TODO stop audio playback
                }


            }


    private View.OnClickListener optionsListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            CharSequence optionsArray[] = new CharSequence[] {"Toggle rounding", "Shift date", /*"Reset",*/ "View By...", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Options menu");
            builder.setItems(optionsArray, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    if (which == 0){//Toggle Rounding
                        if (Processor.getRoundingFlag())
                            Processor.setRoundingFlag(false);
                        else
                            Processor.setRoundingFlag(true);
                        Cursor optionsListenerCursor = getEvents();
                        TableLayout tableRowParent = (TableLayout) drawerLayout.findViewById(R.id.tableLayout1Prototype);
                        tableRowParent.removeAllViews();
                        optionsListenerCursor = getEvents();
                        changedView = true;
                        showDefaultEvents(optionsListenerCursor);
/*                        Fragment frag = new ThirdScreenFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.content_frame, frag);
      //                ft.addToBackStack(null);
                        ft.commit();*/
                    }
                    if (which == 1) //shift date
                    {
                        createShiftDateBuilder();
                    }
/*                    if (which == 2) //reset
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are you sure you want to reset?").setPositiveButton("Yes", resetListener)
                                .setNegativeButton("No", resetListener).show();

                    }*/
                    if (which == 2)//view by
                    {
                        createViewBuilder();
                    }
                    if (which == 3) //cancel
                    {
                        dialog.cancel();
                    }
                }

            });
            builder.show();

        }};

    private String[] getTrainingMaxesInDefaultOrder() {
        setQuery("Lift = 'Bench' AND Cycle = '1'");
        Cursor myCursor = getEvents();
        myCursor.moveToNext();
        String benchTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
        setQuery("Lift = 'Squat' AND Cycle = '1'");
        myCursor = getEvents(); //vladdy
        myCursor.moveToNext();
        String squatTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
        setQuery("Lift = 'OHP' AND Cycle = '1'");
        myCursor = getEvents(); //vladdy
        myCursor.moveToNext();
        String ohpTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
        setQuery("Lift = 'Deadlift' AND Cycle = '1'");
        myCursor = getEvents(); //vladdy
        myCursor.moveToNext();
        String deadTM  = myCursor.getString(myCursor.getColumnIndex(EventsDataSQLHelper.TRAINING_MAX));
        String[] trainingMaxes = new String[4];
        trainingMaxes[0] = benchTM;
        trainingMaxes[1] = squatTM;
        trainingMaxes[2] = ohpTM;
        trainingMaxes[3] = deadTM;

        return trainingMaxes;
    }


    //"Are you sure?" builder template (Used in reset)
    DialogInterface.OnClickListener resetListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    SQLiteDatabase db = eventsData.getWritableDatabase();
                    curView = CURRENT_VIEW.DEFAULT;
                    db.delete("Lifts", null, null);
//                    backToFirst();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
                    break;
            }
        }
    };


    public class DateShiftListener implements DialogInterface.OnClickListener
    {

        String StartingShiftDate;
        public DateShiftListener (String startingDate) {
            this.StartingShiftDate = startingDate;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (rowListenerStatus == ROW_LISTENER.SHIFTFORWARD) {
                        //Toast.makeText(getActivity(), "Shifting forward from date " + StartingShiftDate, Toast.LENGTH_SHORT).show();
                        String startingShiftDateWithFormat = StartingShiftDate.substring(6, 10) + "-" + StartingShiftDate.substring(0, 2) + "-" + StartingShiftDate.substring(3,5);
                        setQuery("date(substr(liftDate, 7, 7) || '-' || substr(liftDate, 1, 2) || '-' || substr(liftDate, 4, 2)) >= date('" + startingShiftDateWithFormat +"')");
                        TableLayout tableRowPrincipal = (TableLayout)drawerLayout.findViewById(R.id.tableLayout1Prototype);
                        Cursor cursor = getEvents();
                        String s = DatabaseUtils.dumpCursorToString(cursor);
                        System.out.println(s);
                        changedView = true;
                        ConfigTool configtool = new ConfigTool(getActivity());
                        configtool.shiftDates(cursor, rowListenerStatus, 1);//TODO don't hardcode 1, allow user to input with spinner or some shit
                        String viewAppendature = getQueryAppendBasedOnCurrentView();
                        setQuery(viewAppendature);
                        cursor = getEvents();
                        tableRowPrincipal.removeAllViews();
                        showDefaultEvents(cursor);
                    } else if (rowListenerStatus == ROW_LISTENER.SHIFTBACKWARD) {
                        Toast.makeText(getActivity(), "Shifting backward from date " + StartingShiftDate, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(), "Shifting forward from date " + StartingShiftDate, Toast.LENGTH_SHORT).show();
                        String startingShiftDateWithFormat = StartingShiftDate.substring(6, 10) + "-" + StartingShiftDate.substring(0, 2) + "-" + StartingShiftDate.substring(3,5);
                        setQuery("date(substr(liftDate, 7, 7) || '-' || substr(liftDate, 1, 2) || '-' || substr(liftDate, 4, 2)) <= date('" + startingShiftDateWithFormat +"')");
                        TableLayout tableRowPrincipal = (TableLayout)drawerLayout.findViewById(R.id.tableLayout1Prototype);
                        Cursor cursor = getEvents();
                        String s = DatabaseUtils.dumpCursorToString(cursor);
                        System.out.println(s);
                        changedView = true;
                        ConfigTool configtool = new ConfigTool(getActivity());
                        configtool.shiftDates(cursor, rowListenerStatus, 1);//TODO don't hardcode 1, allow user to input with spinner or some shit
                        String viewAppendature = getQueryAppendBasedOnCurrentView();
                        setQuery(viewAppendature);
                        cursor = getEvents();
                        tableRowPrincipal.removeAllViews();
                        showDefaultEvents(cursor);
                    }
                    rowListenerStatus = ROW_LISTENER.NORMAL;
                    configureButton.setOnClickListener(optionsListener);
                    configureButton.setText("Configure");
                    configureButton.clearAnimation();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    rowListenerStatus = ROW_LISTENER.NORMAL;//ensure row listener status is normal
                    dialog.cancel();
                    break;

            }
        }


    };


/*    public void clear()
    {
        TableLayout tableRowPrincipal = (TableLayout)drawerLayout.findViewById(R.id.tableLayout1Prototype);
        tableRowPrincipal.removeAllViews();
        changedView = true;
    }*/

    public void createViewBuilder()
    {
        CharSequence optionsArray[] = new CharSequence[] {"Show all", "Bench Only", "Squat Only", "OHP Only", "Deadlift only", "5-5-5 Days only", "3-3-3 Days only", "5-3-1 days only", "Back"};

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        builder2.setTitle("Show by:");

        builder2.setItems(optionsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = getEvents();
                TableLayout tableRowPrincipal = (TableLayout)drawerLayout.findViewById(R.id.tableLayout1Prototype);
                switch (which){
                    case 0:
                        curView = CURRENT_VIEW.DEFAULT;
//						Toast.makeText(getActivity(), "View Selected: Show All", Toast.LENGTH_SHORT).show();
                        setQuery(null);
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 1:
                        curView = CURRENT_VIEW.BENCH;
//						Toast.makeText(getActivity(), "View Selected: Bench Only", Toast.LENGTH_SHORT).show();
                        setQuery("Lift = 'Bench'");
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 2:
                        curView = CURRENT_VIEW.SQUAT;
//						Toast.makeText(getActivity(), "View Selected: Squat Only", Toast.LENGTH_SHORT).show();
                        setQuery("Lift = 'Squat'");
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 3:
                        curView = CURRENT_VIEW.OHP;
//						Toast.makeText(getActivity(), "View Selected: OHP Only", Toast.LENGTH_SHORT).show();
                        setQuery("Lift = 'OHP'");
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 4:
                        curView = CURRENT_VIEW.DEAD;
//						Toast.makeText(getActivity(), "View Selected: Deadlift Only", Toast.LENGTH_SHORT).show();
                        setQuery("Lift = 'Deadlift'");
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 5:
                        curView = CURRENT_VIEW.FIVES;
//						Toast.makeText(getActivity(), "View Selected: Fives Days Only", Toast.LENGTH_SHORT).show();
                        setQuery("Frequency = '5-5-5'");
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 6:
                        curView = CURRENT_VIEW.THREES;
//						Toast.makeText(getActivity(), "View Selected: Triples Days Only", Toast.LENGTH_SHORT).show();
                        setQuery("Frequency = '3-3-3'");
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 7:
                        curView = CURRENT_VIEW.ONES;
//						Toast.makeText(getActivity(), "View Selected: 5-3-1 Days Only", Toast.LENGTH_SHORT).show();
                        setQuery("Frequency = '5-3-1'");
                        tableRowPrincipal.removeAllViews();
                        cursor = getEvents();
                        changedView = true;
                        showDefaultEvents(cursor);
                        break;
                    case 8:
                        dialog.cancel();
                        break;


                }//end switch statement
            }//end outter onclick
        });//end outter which listener

        builder2.show();

    }//end createViewBuilder
    public void createShiftDateBuilder()
    {
        CharSequence optionsArray[] = new CharSequence[] {"Shift dates forward", "Shift dates backward", "Cancel"};

        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        builder2.setTitle("Shift date...");

        builder2.setItems(optionsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Cursor cursor = getEvents();
                TableLayout tableRowPrincipal = (TableLayout)drawerLayout.findViewById(R.id.tableLayout1Prototype);
                final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                animation.setDuration(1000); // duration - half a second
                animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
                switch (which){
                    case 0:
//                        Toast.makeText(getActivity(), "Shifting dates forward", Toast.LENGTH_SHORT).show();
                        configureButton.setText("Exit shift date mode");
                        configureButton.setOnClickListener(configureButtonDateModeListener);
                        rowListenerStatus = ROW_LISTENER.SHIFTFORWARD;
                        configureButton.startAnimation(animation);
                        break;
                    case 1:
//                        Toast.makeText(getActivity(), "Shifting dates backward", Toast.LENGTH_SHORT).show();
                        configureButton.setText("Exit shift date mode");
                        configureButton.setOnClickListener(configureButtonDateModeListener);
                        rowListenerStatus = ROW_LISTENER.SHIFTBACKWARD;
                        configureButton.startAnimation(animation);
                        break;
                    case 2:
                        dialog.cancel();
                        break;
                }//end switch statement
            }//end outter onclick
        });//end outter which listener

        builder2.show();

    }//end createShiftDateBui;der

            private View.OnClickListener configureButtonDateModeListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    configureButton.setOnClickListener(optionsListener);
                    configureButton.clearAnimation();
                    configureButton.setText("Configure");
                    rowListenerStatus = ROW_LISTENER.NORMAL;
                }};

    @SuppressWarnings("deprecation") Cursor getEvents() {
        SQLiteDatabase db = eventsData.getReadableDatabase();
        Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, getQuery(), null, null,
                null, null);

        getActivity().startManagingCursor(cursor);
        return cursor;
    }


    String stickyHeaderCycle = "-1";
    @SuppressWarnings("deprecation") void showDefaultEvents(Cursor cursor) {
        StringBuilder ret = new StringBuilder("");
        while (cursor.moveToNext()) {
            if (!this.insertStatus){//has title been inserted?
                String temp = getQuery(); //temporarily hold query
                String[] TMS = getTrainingMaxesInDefaultOrder();
                String benchTM = TMS[0];
                String squatTM = TMS[1];
                String ohpTM = TMS[2];
                String deadTM = TMS[3];
                setQuery(null);
                ret = new StringBuilder("Starting TMs \n [Bench: " + benchTM + "]");
                ret.append(" [Squat: " + squatTM + "]");
                ret.append(" [OHP: " + ohpTM + "]" );
                ret.append(" [Dead: " + deadTM + "]" );
                lbMode = cursor.getInt((cursor.getColumnIndex(EventsDataSQLHelper.LBFLAG)));
/*                if (lbMode == 1)
                    ret.append("	Mode: lbs");
                else if (lbMode == 0 )
                    ret.append("	Mode: kgs");
                else
                    ret.append("Mode error");*/
                cursor.moveToFirst();
                insertStatus = true;
                retStringSaver = ret.toString();
                setQuery(temp);
                TextView trainingMaxesStream = (TextView) drawerLayout.findViewById(R.id.trainingMaxesTVPrototype);
                trainingMaxesStream.setText(retStringSaver.toString());
                trainingMaxesStream.setTextColor(Color.WHITE);
            }
            else
            if (changedView){

/*						TextView title = new TextView(this);
						title.setText(retStringSaver.toString());
						title.setTextSize(12);
						title.setGravity(Gravity.CENTER);
						title.setTextColor(Color.WHITE);
						title.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,

								LayoutParams.WRAP_CONTENT));
						tableRowPrincipal.addView(title);
						TableRow titleRow = (TableRow) drawerLayout.findViewById(R.id.insertValues);*/
//                TableRow tr = new TableRow(getActivity());
//                ViewGroup.LayoutParams trParams = tableLayout.getLayoutParams();
//                tr.setLayoutParams(trParams);
//                tr.setGravity(Gravity.CENTER_HORIZONTAL);
//				eventsData.createRow(getActivity(), tr, "Date  ", "Cycle", "Lift", "Freq", "1st", "2nd", "3rd");
//                tableLayout.addView(tr);
//                ScrollView myScrollView = (ScrollView) drawerLayout.findViewById(R.id.scrollView1);
//                myScrollView.addView(titleTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//                if (titleTableRow != null)
//                    tableLayout.addView(titleTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//                else
//                {
//                    if (drawerLayout!= null) {
//                        titleTableRow = (TableRow) drawerLayout.findViewById(R.id.insertValuesPrototype);
//                        View myView = myScrollView.getChildAt(0);
//                        myView.addView(titleTableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//
//                    }
//                }
                changedView = false;
            }
            String liftDate = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));
            String cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
            String lift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
            String freq = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
            try
            {
                String first;
                Double second;
                Double third;
                if (Processor.getRoundingFlag())//getUnitMode = true -> using lbs, otherwise, using kgs
                {
                    if (Processor.getUnitMode())//using lbs
                    {
                        first = String.valueOf(round(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)), 5));
                        second = round(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)), 5);
                        third = round(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)), 5);
                    }
                    else //using kgs
                    {
                        first = String.valueOf(roundkg(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST)), 2.5));
                        second = roundkg(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)), 2.5);
                        third = roundkg(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)), 2.5);
                    }

                }
                else
                {
                    first = String.valueOf(roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.FIRST))));
                    second = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.SECOND)));
                    third = roundtoTwoDecimals(cursor.getDouble(cursor.getColumnIndex(EventsDataSQLHelper.THIRD)));
                }

                final String entryString = liftDate + "|" + cycle + "|" + lift + "|" + freq + "|" + first + "|" + second + "|" + third + "|\n";
                //creation of sticky header
                if (!stickyHeaderCycle.equals(cycle))
                {
                        TableRow tr = new TableRow(getActivity());
                    TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    trParams.setMargins(0, 0, 0, 0);
                    tr.setLayoutParams(trParams);
                    tr.setGravity(Gravity.CENTER);
                    tr.setTag("sticky");
                    tr.setPadding(0 ,0 ,0 ,0);
//                    tr.setBackgroundColor(Color.parseColor("#00695C"));//darker green
                    TableRow finalTR = eventsData.createStickyRow(getActivity(), tr, cycle);
                    stickyHeaderCycle = cycle;
                    tableLayout.addView(finalTR);
                }

                //creation of regular entry
                TableRow tr = new TableRow(getActivity());
                ViewGroup.LayoutParams trParams = tableLayout.getLayoutParams();
                tr.setLayoutParams(trParams);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                String insertDate = liftDate.substring(0, 6) + liftDate.substring(8, 10);
                eventsData.createRow(getActivity(), tr, insertDate, cycle, lift, freq, first, String.valueOf(second), String.valueOf(third));


                TextView entry = new TextView(getActivity());
                entry.setText(entryString);
                entry.setTextSize(12);
                entry.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams PO = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, 1f);
                entry.setLayoutParams(PO);
//                tr.setBackgroundColor(getResources().getColor(R.color.whi));

                tr.setOnClickListener(new TableRowClickListener(entryString){

                    @Override
                    public void onClick(View v) {
                        String myDate = entryString.substring(0, 10);//parse our date
                        //parsing cycle: account for cycles greater than 9

                        String dividerRegex = "(?<=\\|)[^|]++(?=\\|)";//regex to grab data between pipes (|)
                        Pattern pattern = Pattern.compile(dividerRegex);
                        Matcher matcher = pattern.matcher(entryString);

                        String myEntries[] = new String[6]; //for reference: 0 = date, 1 = cycle, 2 = liftType, 3 = frequency, 4 = firstLift, 5 = secondLift, 6 = secondLift
                        int iterator = 0;
                        while (matcher.find()) {
                            myEntries[iterator] = matcher.group(0);
                            iterator++;
                        }

                        TableLayout tableRowPrincipal = (TableLayout)drawerLayout.findViewById(R.id.tableLayout1Prototype);

                        if (rowListenerStatus == ROW_LISTENER.NORMAL) {
                            Intent intent = new Intent(getActivity(), IndividualViewsPrototype.class);

                            String myFrequency = myEntries[2];
                            String myLiftType = myEntries[1];
                            String myCycle = myEntries[0];
                            String myFirstLift = myEntries[3];
                            String mySecondLift = myEntries[4];
                            String myThirdLift = myEntries[5];
                            String viewMode = curView.name().toString();
                            String mode = String.valueOf(lbMode);//1 or 0


                            TabPrototype.cycle = myCycle;
                            TabPrototype.frequency = myFrequency;
                            TabPrototype.liftType = myLiftType;
                            TabPrototype.firstLift = myFirstLift;
                            TabPrototype.secondLift = mySecondLift;
                            TabPrototype.thirdLift = myThirdLift;
                            TabPrototype.viewMode = viewMode;
                            TabPrototype.unitMode = mode;
                            TabPrototype.date = myDate;

                            startActivity(intent);
                        }
                        else if (rowListenerStatus == ROW_LISTENER.SHIFTFORWARD || rowListenerStatus == ROW_LISTENER.SHIFTBACKWARD) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            String direction = rowListenerStatus == ROW_LISTENER.SHIFTFORWARD ? "forward" : "backward";
                            DateShiftListener shiftListener = new DateShiftListener(myDate);
                            builder.setMessage("Are you sure you want to shift all dates from " + myDate + " " + direction + "?").setPositiveButton("Yes", shiftListener)
                                    .setNegativeButton("No", shiftListener).show();
                        }
                    }});
                tableLayout.addView(tr);
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(getActivity(), "There was an error processing your lift numbers, please double check them!", Toast.LENGTH_LONG).show();
                sendTrackerException("ThirdScreen: onclick with dividerRegex", e.getLocalizedMessage());
            }
        }

    }

    //TODO support this
/*    private void backToFirst()
    {
        SQLiteDatabase db = eventsData.getWritableDatabase();
        db.delete("Lifts", null, null);
        startActivity(new Intent(this, MainActivity.class));
    }*/

    //Async caller for threading
    class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        String[] LiftPattern;
        public AsyncCaller(String[] liftPattern)
        {
            LiftPattern = liftPattern;
        }

        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading your gains...");

            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Processor.setPatternAcronym(LiftPattern);
            SQLiteDatabase db = eventsData.getWritableDatabase();
            db.delete("Lifts", null, null);
            Processor.calculateCycle(ThirdScreenFragment.this, LiftPattern);
            cursor = getEvents();

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            showDefaultEvents(cursor);

            pdLoading.dismiss();
        }

    }




    //setters/getters
    public void setMode (String myMode )
    {
        MODE_FORMAT = myMode;
    }

    String getModeFormat ()
    {
        return MODE_FORMAT;
    }


    public void setQuery(String myQuery)
    {
        CURRENT_SELECT_QUERY = myQuery;
    }

    public String getQuery ()
    {
        return CURRENT_SELECT_QUERY;
    }

    public void setNumberCycles(int numberCycles)
    {
        NUMBER_CYCLES = numberCycles;
    }

    public int getNumberCycles()
    {
        return NUMBER_CYCLES;
    }


    //rounding methods
    double roundkg(double valueToBeRounded, double roundVal) //first argument is rounded,
    {
        return (double) (Math.round(valueToBeRounded/roundVal) * roundVal);
    }

    double round(double valueToBeRounded, int roundVal)
    {
        return (double) (Math.round(valueToBeRounded/roundVal) * roundVal);
    }

    double roundtoTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }



    boolean init = false;

    private String getQueryAppendBasedOnCurrentView()
    {
        switch (curView)
        {
            case DEFAULT:
                return "";
            case BENCH:
                return "Lift = 'Bench'";
            case SQUAT:
                return "Lift = 'Squat'";
            case OHP:
                return "Lift= 'OHP'";
            case DEAD:
                return "Lift = 'Deadlift'";
            case FIVES:
                return "Frequency = '5-5-5'";
            case THREES:
                return "Frequency = '3-3-3'";
            case ONES:
                return "Frequency = '5-3-1'";
            default:
                return "";
        }

    }

    public void setInsertStatus(boolean status)
    {
        this.insertStatus = status;
    }

            protected void sendTrackerException(String exceptionType, String value) {
                Tracker tracker = GoogleAnalytics.getInstance(getActivity()).getTracker("UA-55018534-1");
                tracker.send(MapBuilder
                        .createEvent("Exception",     // Event category (required)
                                exceptionType,  // Event action (required)
                                value,   // Event label
                                null)            // Event value
                        .build());



            }


        }