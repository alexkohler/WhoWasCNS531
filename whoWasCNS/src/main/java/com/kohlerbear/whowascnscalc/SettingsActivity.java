package com.kohlerbear.whowascnscalc;

import android.content.res.TypedArray;
import android.os.Bundle;

/**
 * In case settings are ever needed..
 *
 */
public class SettingsActivity extends BaseActivity {

	//nav drawer vars
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//Set up our navigation drawer
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);// load icons from strings.xml

		set(navMenuTitles, null);
		navMenuIcons.recycle();
	}

}
