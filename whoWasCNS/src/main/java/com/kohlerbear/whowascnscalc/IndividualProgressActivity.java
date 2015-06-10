package com.kohlerbear.whowascnscalc;

import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

//Activity class is accessed via pressing a date in the individualViewsPrototype
public class IndividualProgressActivity extends ActionBarActivity {


    /*
                        intent.putExtra("liftType", clickedEvent.getLiftType());
                    intent.putExtra("origin", "progressOverview");
     */

    StickyListHeadersListView indProgLV;
    boolean m_isProgressOverview;//vs accessoryProgress.. can't justify a 2 valued enum.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_individual);

        Bundle args = getIntent().getExtras();
        String date = args.getString("date");
        String liftType = args.getString("liftType");


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);

        //Manage colors
        ColorManager cm = ColorManager.getInstance(getApplicationContext());


        LongTermDataSQLHelper helper = new LongTermDataSQLHelper(this);
        indProgLV = (StickyListHeadersListView) findViewById(R.id.individual_listView);

        if (args.getString("origin").equals("progressOverview")) {
            m_isProgressOverview= true;
            actionBar.setTitle(date.substring(0, 5) + " " + liftType + " accessories");
            //Populate based on data
            StickyListHeadersAdapter mArrayAdapter = new IndividualProgressArrayAdapter(getApplicationContext(), helper.getProgressListByDate(date), false);
            indProgLV.setAdapter(mArrayAdapter);
        }
        else if (args.getString("origin").equals("accessoryProgress")) {
            m_isProgressOverview = false;
            String liftName = args.getString("liftName");
            actionBar.setTitle(liftName + " Progress");
            //Populate based on data
            StickyListHeadersAdapter mArrayAdapter = new AccessoryProgressArrayAdapter(getApplicationContext(), helper.getProgressListByLiftName(liftName), false);
            indProgLV.setAdapter(mArrayAdapter);
        }

        //Get listview and populate it with our accessories for the current date


        //Manage colors
        ColorManager manager = ColorManager.getInstance(getApplicationContext());
        getActionBar().setBackgroundDrawable(new ColorDrawable(manager.getPrimaryColor()));


        //depending on origin, rows will have to be created differently


    }


    public void removeLiftBasedOnListViewEntry(LongTermEvent event)
    {
        String[] args = new String[6];
        args[0] = event.getLiftDate();
        args[1] = event.getLiftType();
        args[2] = event.getLiftName();
        args[3] = String.valueOf(event.getWeight());
        args[4] = String.valueOf(event.getReps());
        args[5] = event.getFrequency();

        LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(LongTermDataSQLHelper.TABLE, LongTermDataSQLHelper.LIFTDATE + "=? and " + LongTermDataSQLHelper.LIFT_TYPE + "=? and " + LongTermDataSQLHelper.LIFT_NAME + "=? and " + LongTermDataSQLHelper.WEIGHT + "=? and " + LongTermDataSQLHelper.REPS + "=? and " + LongTermDataSQLHelper.FREQUENCY + "=? /*LIMIT 1*/", args);
        //ArrayAdapter<LongTermEvent> mArrayAdapter = new ArrayAdapter<LongTermEvent>(getActivity(), R.layout.row_with_arrow, R.id.liftText, helper.getProgressListByView());//have separate layout with visibility
        StickyListHeadersAdapter mArrayAdapter;
        if (m_isProgressOverview)
            mArrayAdapter = new IndividualProgressArrayAdapter(getApplicationContext(), helper.getProgressListByDate(event.getLiftDate()), true);
        else
            mArrayAdapter = new AccessoryProgressArrayAdapter(getApplication(), helper.getProgressListByLiftName(event.getLiftName()), true);
        indProgLV.setAdapter(mArrayAdapter);
        //toggleListViewDeleteButtonShown(true);
    }


    public void toggleListViewDeleteButtonShown(boolean shown) {
//        int count = getCurrentListViewItems().size();
        int count= indProgLV.getAdapter().getCount();
        for (int i = 0; i < count;  i++) {
            final View currentChild = indProgLV.getListChildAt(i);
            final int currentPosition = i;
            if (currentChild != null) {
                final ImageView deleteButton = (ImageView) currentChild.findViewById(R.id.deleteButtonIndV);
                final TextView currentRowText = (TextView) currentChild.findViewById(R.id.liftNameTV);
                if (shown)
                    deleteButton.setVisibility(View.VISIBLE);
                else
                    deleteButton.setVisibility(View.GONE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int currentRotation = 0;
                        RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation + 90,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        currentRotation = (currentRotation + 30) % 360;

                        anim.setInterpolator(new LinearInterpolator());
                        anim.setDuration(400);
                        anim.setFillEnabled(true);
                        anim.setFillAfter(true);


                        deleteButton.postDelayed(new Runnable() {
                            @Override
                            public void run() { //refresh database before attempting to delete, since our dataset is small, we can get away with this.
                                Animation listViewAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right); //load animation for listview
                                listViewAnimation.setDuration(600);
                                currentChild.startAnimation(listViewAnimation);
                                // Execute some code after 2 seconds have passed
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getApplicationContext());
                                        SQLiteDatabase db = helper.getWritableDatabase();
                                        LongTermEvent eventToRemove = (LongTermEvent) indProgLV.getAdapter().getItem(currentPosition);
                                        removeLiftBasedOnListViewEntry(eventToRemove);

                                    }
                                }, 610);


                            }
                        }, 410);
                        deleteButton.startAnimation(anim);


                    }

                });
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.accessory_menu, menu);
        super.onCreateOptionsMenu(menu);

        MenuItem editMenuOptionItem = menu.findItem(R.id.editMenuOption);
        editMenuOptionItem.setEnabled(true);
        editMenuOptionItem.setVisible(true);
        //We need to initialize what image we want
        TextView v = new TextView(getApplicationContext());
        v.setTextColor(Color.WHITE);
        v.setText("Edit");
        v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        v.setDrawingCacheEnabled(true);
        editMenuOptionItem.setIcon(createDrawableFromView(v));


        return true;
    }




    static boolean toggle = true;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMenuOption:
                if (toggle) {//Edit Pressed
                    TextView v = new TextView(getApplicationContext());
                    v.setTextColor(Color.WHITE);
                    v.setText("Done");
                    v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    v.setDrawingCacheEnabled(true);
                    item.setIcon(createDrawableFromView(v));
                    toggleListViewDeleteButtonShown(true);
                    toggle = !toggle;
                    return true;
                } else //Done pressed
                {
                    TextView v = new TextView(getApplicationContext());
                    v.setTextColor(Color.WHITE);
                    v.setText("Edit");
                    v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    v.setDrawingCacheEnabled(true);
                    item.setIcon(createDrawableFromView(v));
                    toggleListViewDeleteButtonShown(false);

                    toggle = !toggle;
                    return true;
                }
            default:
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public Drawable createDrawableFromView(View v)
    {
        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false); // clear drawing cachee
        return new BitmapDrawable(getResources(), b);
    }



}
