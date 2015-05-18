package com.kohlerbear.whowascnscalc;

import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ProgressIndividualActivity extends ActionBarActivity {


    private String m_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_individual);

        Bundle args = getIntent().getExtras();
        m_date = args.getString("date");

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(m_date);
        actionBar.setDisplayUseLogoEnabled(false);

        //Get listview and populate it with our accessories for the current date
        ListView individual_progress_listView = (ListView) findViewById(R.id.individual_progress_listview);
        LongTermDataSQLHelper helper = new LongTermDataSQLHelper(this);
        ArrayAdapter<LongTermEvent> mArrayAdapter = new ArrayAdapter<LongTermEvent>(this, R.layout.row_no_config, R.id.liftText, helper.getProgressList());
        individual_progress_listView.setAdapter(mArrayAdapter);

        //Manage colors
        ColorManager manager = ColorManager.getInstance(getApplicationContext());
        getActionBar().setBackgroundDrawable(new ColorDrawable(manager.getPrimaryColor()));
        individual_progress_listView.setBackgroundColor(Color.BLACK);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
