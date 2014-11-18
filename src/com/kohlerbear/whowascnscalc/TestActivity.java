package com.kohlerbear.whowascnscalc;

import android.content.res.TypedArray;
import android.os.Bundle;

public class TestActivity extends BaseActivity {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
																					// titles
																					// from
																					// strings.xml

		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
															// strings.xml

		set(navMenuTitles, navMenuIcons);
	}
}
