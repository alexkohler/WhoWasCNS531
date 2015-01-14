package com.kohlerbear.whowascnscalc;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        set(navMenuTitles, navMenuIcons);
        navMenuIcons.recycle();

        //Set our default values for preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        /**
         * Creating all buttons instances
         * */
        // Dashboard News feed button
        Button btn_newsfeed = (Button) findViewById(R.id.btn_news_feed);

        // Dashboard Friends button
        Button btn_friends = (Button) findViewById(R.id.btn_friends);

        // Dashboard Messages button
        Button btn_messages = (Button) findViewById(R.id.btn_messages);

        // Dashboard Places button
        Button btn_places = (Button) findViewById(R.id.btn_places);

        // Dashboard Events button
        Button btn_events = (Button) findViewById(R.id.btn_events);

        // Dashboard Photos button
        Button btn_photos = (Button) findViewById(R.id.btn_photos);

        /**
         * Handling all button click events
         * */

        // Listening to News Feed button click
        btn_newsfeed.setOnClickListener(new View.OnClickListener() { //Go to today's lift placeholder
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
        btn_friends.setOnClickListener(new View.OnClickListener() { //Accesory template placeholder

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
        btn_messages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });

        // Listening to Places button click
        btn_places.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });

        // Listening to Events button click
        btn_events.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });

        // Listening to Photos button click
        btn_photos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });
    }
}
