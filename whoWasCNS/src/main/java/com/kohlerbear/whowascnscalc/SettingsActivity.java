package com.kohlerbear.whowascnscalc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.Toast;

/**
 * Created by alex on 1/4/15.
 */


public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    ///nav drawer vars
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;


    //Settings strings
    public static final String KEY_PREF_COLOR_SCHEME = "pref_colorScheme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load titles from strings.xml
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);// load icons from
        // strings.xml

        set(navMenuTitles, navMenuIcons, "Settings");
        navMenuIcons.recycle();

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new SettingsFragment())
                .commit();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Settings </font>"));
    }


    //Color changes don't automatically take place, (will change upon user opening another activity/fragment), so listen for changes here to make change immediate
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(KEY_PREF_COLOR_SCHEME)) {
            String colorSchemePref = sharedPreferences.getString(SettingsActivity.KEY_PREF_COLOR_SCHEME, "");
            String actionBarColorString;
            switch (colorSchemePref)
            {
                case "Teal":
                    actionBarColorString = "#607D8B";
                    break;

                case "Orange":
                    actionBarColorString = "#FF5722";
                    break;
                case "Indigo":
                    actionBarColorString = "#3F51B5";
                    break;
                case "Red":
                    actionBarColorString = "#F44336";
                    break;

                default:
                    actionBarColorString = "#607D8B";//Anything goes awry just go with teal
                    break;
            }

            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(actionBarColorString)));
        }
    }




}
