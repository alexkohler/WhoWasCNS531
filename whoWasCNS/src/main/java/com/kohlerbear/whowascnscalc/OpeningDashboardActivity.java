package com.kohlerbear.whowascnscalc;

import android.content.res.TypedArray;
import android.os.Bundle;

public class OpeningDashboardActivity extends BaseActivity {

	
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

}
