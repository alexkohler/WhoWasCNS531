package com.kohlerbear.whowascnscalc;

import android.content.res.TypedArray;
import android.os.Bundle;

public class About531 extends BaseActivity {

	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		setTheme(R.style.AppBaseLight);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about531);
		
		
		//Set up our navigation drawer
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml

		set(navMenuTitles, navMenuIcons);
		navMenuIcons.recycle();
 
        getActionBar().setDisplayHomeAsUpEnabled(true); 
	}


}
