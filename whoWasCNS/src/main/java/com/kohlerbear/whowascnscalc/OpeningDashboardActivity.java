package com.kohlerbear.whowascnscalc;

import android.content.res.TypedArray;
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
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



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
        btn_newsfeed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            }
        });

        // Listening Friends button click
        btn_friends.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
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
