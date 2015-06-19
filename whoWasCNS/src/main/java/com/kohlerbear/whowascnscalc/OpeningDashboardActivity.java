package com.kohlerbear.whowascnscalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

/*public class OpeningDashboardActivity extends BaseActivity {

	
	///nav drawer vars
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opening_dashboard);
		
		//Set up our navigation drawer
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml

		set(navMenuTitles, navMenuIcons);
		navMenuIcons.recycle();
		
	}

}*/
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class OpeningDashboardActivity extends BaseActivity {

    ///nav drawer vars
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);

        //Set up our navigation drawer
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons, "Dashboard");
        navMenuIcons.recycle();
        /*SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        /*SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (!sp.getBoolean("first", false)) {
            SharedPreferences.Editor editor = sp.edit();
            new ShowcaseView.Builder(this)
                    .setTarget(new ActionViewTarget(this, ActionViewTarget.Type.HOME))
                    .setContentTitle("cold stone creamery bitch")
                    .setContentText("This is highlighting the Home button")
                    .hideOnTouchOutside()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .build();
            //editor.putBoolean("first", true);
            //editor.commit();
            Toast.makeText(getApplicationContext(), "first time", Toast.LENGTH_SHORT).show();
        }*/


        //Set our default values for preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        /**
         * Creating all buttons instances
         * */

        //int primaryColor = ColorManager.getInstance(getApplicationContext()).getPrimaryColor();
        // Dashboard News feed button
        Button btn_today_lift = (Button) findViewById(R.id.btn_news_feed);
        btn_today_lift.setTextColor(Color.BLACK);

        // Dashboard Friends button
        Button btn_acc_temp = (Button) findViewById(R.id.btn_friends);
        btn_acc_temp.setTextColor(Color.BLACK);


        // Dashboard Messages button
        Button btn_track_bw = (Button) findViewById(R.id.btn_messages);
        btn_track_bw.setTextColor(Color.BLACK);

        // Dashboard Places button
        Button btn_view_prog = (Button) findViewById(R.id.btn_places);
        btn_view_prog.setTextColor(Color.BLACK);

        // Dashboard Events button
        Button btn_events = (Button) findViewById(R.id.btn_events);
        btn_events.setTextColor(Color.BLACK);

        // Dashboard Photos button
        Button btn_photos = (Button) findViewById(R.id.btn_photos);
        btn_photos.setTextColor(Color.BLACK);
        /**
         * Handling all button click events
         * */

        // Listening to News Feed button click
        btn_today_lift.setOnClickListener(new View.OnClickListener() { //Go to today's lift placeholder
/*            String myFrequency = myEntries[2];
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

            startActivity(intent);*/

            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat databaseDateFormat = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
                String currentDateString = databaseDateFormat.format(c.getTime());
                EventsDataSQLHelper eventsData = new EventsDataSQLHelper(getApplicationContext());
                SQLiteDatabase db = eventsData.getReadableDatabase();
                Cursor cursor = db.query(EventsDataSQLHelper.TABLE, null, "liftDate = '" + currentDateString + "'", null, null,
                        null, null);
//                String s = DatabaseUtils.dumpCurrentRowToString(cursor);
                ConfigTool ct = new ConfigTool(getApplicationContext());
                if (!ct.dbEmpty()) {
                    if (cursor.moveToNext()) {

                        Intent intent = new Intent(getApplicationContext(), IndividualViewsPrototype.class);


                        TabPrototype.cycle = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.CYCLE));
                        TabPrototype.numberOfCycles = "5";
                        TabPrototype.frequency = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FREQUENCY));
                        TabPrototype.liftType = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFT));
                        TabPrototype.firstLift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.FIRST));
                        TabPrototype.secondLift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.SECOND));
                        TabPrototype.thirdLift = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.THIRD));
                        TabPrototype.viewMode = "It don't matta";
                        TabPrototype.unitMode = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LBFLAG));
                        TabPrototype.date = cursor.getString(cursor.getColumnIndex(EventsDataSQLHelper.LIFTDATE));

                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No lift found for today!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No previous projection exists!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Listening Friends button click
        btn_acc_temp.setOnClickListener(new View.OnClickListener() { //Accesory template placeholder

            @Override
            public void onClick(View view) {
                Fragment accessoryFragment = new AccessoryFragment();
                FragmentManager accessoryFM = getSupportFragmentManager();
                FragmentTransaction accessoryFT = accessoryFM.beginTransaction();
                accessoryFT.replace(R.id.home_root, accessoryFragment);
//             accessoryFT.addToBackStack(null);
                accessoryFT.commit();
            }


        });

        // Listening Messages button click
        btn_track_bw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        // Listening to Places button click
        btn_view_prog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Fragment viewprogfragment = new ProgressOverviewFragment();
                FragmentManager accessoryFM = getSupportFragmentManager();
                FragmentTransaction accessoryFT = accessoryFM.beginTransaction();
                accessoryFT.replace(R.id.home_root, viewprogfragment);
                accessoryFT.commit();
            }
        });

        // Listening to Events button click
//        btn_events.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        // Listening to Photos button click
//        btn_photos.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//            }
//        });

        //Finally, ensure all our tables exist
        LongTermDataSQLHelper LThelper = new LongTermDataSQLHelper(getApplicationContext());
        SQLiteDatabase db = LThelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=?", new String[]{LongTermDataSQLHelper.TABLE});
        int count = c.getCount();
//        if (count == 0) {
//            db.execSQL("drop table " + LongTermDataSQLHelper.TABLE);
            db.execSQL("create table if not exists " + LongTermDataSQLHelper.TABLE + " (liftDate text not null, Cycle text not null, Lift_Type text not null, Lift_name text not null, Frequency text not null, Weight real, Reps integer, Theoretical_Onerep real, Lb_Flag integer, setNumber integer);");
//        }
        c = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=?", new String[]{AccessoryLiftSQLHelper.TABLE});
        count = c.getCount();
//        if (count == 0) {
//            db.execSQL("drop table " + AccessoryLiftSQLHelper.TABLE);
            db.execSQL("create table if not exists " + AccessoryLiftSQLHelper.TABLE + " (ACCESSORY text not null, ACCESSORY_TYPE text not null, LIFT_ORDER integer);");

//        }
        c = db.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=?", new String[]{EventsDataSQLHelper.TABLE});
        count = c.getCount();
//        if (count == 0) {
//            db.execSQL("drop table " + EventsDataSQLHelper.TABLE);
            db.execSQL("create table if not exists " + EventsDataSQLHelper.TABLE + " (liftDate text not null, Cycle integer, Lift text not null, Frequency text not null, First_Lift real, Second_Lift real, Third_Lift real, Training_Max integer, column_LbFlag integer, RoundFlag integer, Pattern text);");

//        }

//        "create table " + TABLE + "(liftDate text not null, Cycle text not null, Lift_Type text not null, Lift_name text not null, Frequency text not null, Weight real, Reps integer, Theoretical_Onerep real, Lb_Flag integer, setNumber integer);";

    }
}
