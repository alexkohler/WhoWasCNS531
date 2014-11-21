package com.kohlerbear.whowascnscalc;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

 
        getActionBar().setDisplayHomeAsUpEnabled(true); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about531, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
