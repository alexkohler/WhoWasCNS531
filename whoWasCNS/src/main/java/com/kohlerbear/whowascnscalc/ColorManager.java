package com.kohlerbear.whowascnscalc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

/**
 * Created by alex on 1/5/15.
 */
public class ColorManager {

    private static ColorManager instance = null;
    protected ColorManager() {
        // Exists only to defeat instantiation.
    }
    static Context m_context;
    static int primaryColor;
    static int textColor;

    //Third Screen
    static int stickyHeaderBackgroundColor;
    static int stickyHeaderTextColor;
    static int trainingMaxesStreamColor;
    static int configureButtonColor;
    static int calendarDividerColor;

    public static ColorManager getInstance(Context context) {
        if(instance == null) {
            instance = new ColorManager();
            m_context = context;
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(m_context);
            String currentColorScheme= sharedPrefs.getString(SettingsActivity.KEY_PREF_COLOR_SCHEME, "");

            switch (currentColorScheme) {
                case "Teal":
                    primaryColor = Color.parseColor("#607D8B");//Blue gray 500
                    stickyHeaderBackgroundColor = Color.parseColor("#546E7A");//600
                    stickyHeaderTextColor = Color.WHITE;
                    trainingMaxesStreamColor = Color.parseColor("#607D8B"); //500
                    configureButtonColor = Color.parseColor("#546E7A");//600
                    calendarDividerColor = Color.parseColor("#607D8B"); //500
                    textColor = Color.WHITE;
                    break;

                case "Orange":
                    primaryColor = Color.parseColor("#FF5722");//Orange 500
                    stickyHeaderBackgroundColor = Color.parseColor("#F4511E");//600
                    stickyHeaderTextColor = Color.WHITE;
                    trainingMaxesStreamColor = Color.parseColor("#FF5722"); //500
                    configureButtonColor = Color.parseColor("#F4511E");//600
                    calendarDividerColor = Color.parseColor("#FF5722"); //500
                    textColor = Color.WHITE;
                    break;

                default:
                    primaryColor = Color.parseColor("#607D8B");//Anything goes awry just go with blue gray
                    stickyHeaderBackgroundColor = Color.parseColor("#546E7A");
                    stickyHeaderTextColor = Color.WHITE;
                    trainingMaxesStreamColor = Color.parseColor("#607D8B"); //500
                    configureButtonColor = Color.parseColor("#546E7A");//600
                    calendarDividerColor = Color.parseColor("#607D8B"); //500
                    textColor = Color.WHITE;
                    break;
            }



        }
        return instance;
    }

    public static void clear()
    {
        instance = null;
    }

    int getPrimaryColor() {

        return primaryColor;
    }

    int getStickyHeaderBackgroundColor() {
        return stickyHeaderBackgroundColor;
    }

    int getStickyHeaderTextColor()
    {
        return stickyHeaderTextColor;
    }

    int getConfigureButtonColor() { return configureButtonColor; }

    int getTrainingMaxesStreamColor() { return trainingMaxesStreamColor; }

    int getCalendarDividerColor() { return calendarDividerColor; }

    int getTextColor() {return textColor; }
}
