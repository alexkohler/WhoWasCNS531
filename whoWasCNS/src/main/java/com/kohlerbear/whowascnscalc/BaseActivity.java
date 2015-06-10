package com.kohlerbear.whowascnscalc;

import java.util.ArrayList;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 *The backbone of our navigation drawer - any class that uses this must extend, have an appBaseTheme flavor in manifest, and follow a couple setup steps in the respective oncreate method (see almost any activity in this project for an example 
 *
 */
@SuppressLint("Registered")//Will also be exteneded from activities but will never be a standalone activity
@SuppressWarnings("deprecation")//TODO add in v7 toggle
public class BaseActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	protected RelativeLayout _completeLayout, _activityLayout;
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer);
		// if (savedInstanceState == null) {
		// // on first time display view for first nav item
		// // displayView(0);
		// }
	}

	public void set(String[] navMenuTitles, TypedArray navMenuIcons) {
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items
		if (navMenuIcons == null) {
			for (int i = 0; i < navMenuTitles.length; i++) {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
			}
		} else {
			for (int i = 0; i < navMenuTitles.length; i++) {
				navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
						navMenuIcons.getResourceId(i, -1)));
			}
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);//DON'T show home icon\

        //COLORS
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ColorManager.getInstance(this).getPrimaryColor()));
        ColorManager.clear();


		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
		// accessibility
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				supportInvalidateOptionsMenu();
				mDrawerList.bringToFront();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		ConfigTool ct = new ConfigTool(this);
		switch (position) {
		case 0://dashboard
            Intent intent = new Intent(this, OpeningDashboardActivity.class);
            startActivity(intent);
            finish();// finishes the current activity
			break;
		case 1://Create new projection
            Intent intent2 = new Intent(this, TabPrototype.class);
            TabPrototype.origin = "dashboard";
            if (!ct.dbEmpty())
                TabPrototype.liftPattern = ct.populateArrayBasedOnDatabase();
            else
                {
                    String[] defaultPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"  };
                    TabPrototype.liftPattern = defaultPattern;
                }
            startActivity(intent2);
            finish();
			break;
		 case 2://view existing
			if (!ct.dbEmpty())
			{
//				Intent viewExistingProjectionIntent = new Intent(this, ThirdScreenFragment.class);

				TabPrototype.origin = "dashboard";//viewExistingProjectionIntent.putExtra("origin", "dashboard");
				TabPrototype.liftPattern = ct.populateArrayBasedOnDatabase();//viewExistingProjectionIntent.putExtra("liftPattern", ct.populateArrayBasedOnDatabase());
				
				String startingDate = ct.getStartingDateFromDatabase();
				TabPrototype.formattedDate = startingDate;//viewExistingProjectionIntent.putExtra("key2", startingDate);
				
				String modeString = ct.getLbModeFromDatabase().intern();
				TabPrototype.unitMode = modeString;//viewExistingProjectionIntent.putExtra("mode", modeString);
				
				int roundingFlag = Integer.valueOf(ct.getRoundingFlagFromDatabase());
				String intentRoundingStringBool = "true";
				if (roundingFlag == 1)
					intentRoundingStringBool = "true";
				else
					intentRoundingStringBool = "false";
				
				//viewExistingProjectionIntent.putExtra("round", intentRoundingStringBool); this isn't really needed anymore

				//startActivity(viewExistingProjectionIntent);
                //finish();
                Fragment thirdFrag = new ThirdScreenFragment();
                FragmentManager thirdFM = getSupportFragmentManager();
                FragmentTransaction thirdFragTransaction = thirdFM.beginTransaction();
                thirdFragTransaction.replace(R.id.content_frame, thirdFrag);
//                ft.addToBackStack(null);
                thirdFragTransaction.commit();
			}
			else
				Toast.makeText(this, "No previous projection exists!", Toast.LENGTH_SHORT).show();
			break;
		 case 3://about
             Fragment aboutFrag = new About531Fragment();
             FragmentManager aboutFM = getSupportFragmentManager();
             FragmentTransaction aboutFT = aboutFM.beginTransaction();
             aboutFT.replace(R.id.content_frame, aboutFrag);
//                ft.addToBackStack(null);
             aboutFT.commit();
			 break;
		 case 4://settings
             //TODO not exactly stable or anywhere near working
//             Fragment settingsFrag = new SettingsFragment();
//             FragmentManager settingsFM = getSupportFragmentManager();
//             FragmentTransaction settingsFT = settingsFM.beginTransaction();
//             settingsFT.replace(R.id.content_frame, settingsFrag);
//             settingsFT.addToBackStack(null);
//             settingsFT.commit();
             Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
             startActivity(settings);
             finish();
		 break;
		/* case 5://testing arena
			 String[] TO = {"amrood.admin@gmail.com"};
			 Intent emailIntent = new Intent(Intent.ACTION_SEND);
			 emailIntent.setData(Uri.parse("mailto:"));
			 emailIntent.setType("message/rfc822");


			 emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"toaddress@gmail.com"});
			 emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
			 emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

			 try {
				 startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				 finish();
			 } catch (android.content.ActivityNotFoundException ex) {
				 Toast.makeText(getApplicationContext(),
						 "There is no email client installed.", Toast.LENGTH_SHORT).show();
			 }
             break;*/
		default:
			break;
		}

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}



	
	protected void sendTrackerException(String exceptionType, String value) {
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker("UA-55018534-1");
		  tracker.send(MapBuilder
			      .createEvent("Exception",     // Event category (required)
			                   exceptionType,  // Event action (required)
			                   value,   // Event label
			                   null)            // Event value
			      .build());
		
	}
}
