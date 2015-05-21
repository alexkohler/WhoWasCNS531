package com.kohlerbear.whowascnscalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kohlerbear.whowascnscalc.dragndroplist.DragNDropListView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex on 5/13/15.
 */


public class ProgressOverviewFragment extends Fragment {

    //Keep track of menu for sake of delete button
    Menu mMenu;
    ListView progress_listView;
    ThirdScreenFragment.CURRENT_VIEW m_currentView = ThirdScreenFragment.CURRENT_VIEW.DEFAULT;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity faActivity  = (FragmentActivity)    super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        DrawerLayout drawerLayout = (DrawerLayout) inflater.inflate(R.layout.fragment_view_progress, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
        // ...



        //take care of colors
        Button viewByButton = (Button) drawerLayout.findViewById(R.id.progressConfigureButton);
        ColorManager manager = ColorManager.getInstance(getActivity());
        final int primaryColor = manager.getPrimaryColor();
        viewByButton.setBackgroundColor(primaryColor);
        ColorManager.clear();

        //Manage back end for view by button
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence optionsArray[] = new CharSequence[] {"Show all", "Bench only", "Squat only", "OHP only", "Deadlift only", "5-5-5 only", "3-3-3 only", "5-3-1 only", "Cancel" };

        builder.setTitle("View only..");
        builder.setItems(optionsArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                switch (which)
                {
                    case 0: //Show all
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.DEFAULT;
                        break;
                    case 1://Bench
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.BENCH;
                        break;
                    case 2: //Squat
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.SQUAT;
                        break;
                    case 3: //OHP
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.OHP;
                        break;
                    case 4: //Deadlift
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.DEAD;
                        break;
                    case 5: //5-5-5
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.FIVES;
                        break;
                    case 6: //3-3-3
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.THREES;
                        break;
                    case 7://5-3-1
                        m_currentView = ThirdScreenFragment.CURRENT_VIEW.ONES;
                        break;
                    case 8: //cancel

                        break;
                }

                LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity());
                ViewProgressArrayAdapter mArrayAdapter = new ViewProgressArrayAdapter(getActivity(), helper.getProgressList(m_currentView), false);
                progress_listView.setAdapter(mArrayAdapter);

            }

        });

        viewByButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = builder.show();
                int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                TextView tv = (TextView) d.findViewById(textViewId);
                tv.setTextColor(primaryColor);
                int x = Resources.getSystem().getIdentifier("titleDivider","id", "android");
                View titleDivider = d.findViewById(x);
                titleDivider.setBackgroundColor(primaryColor);
//                int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
//                View divider = d.findViewById(dividerId);
//                if (dividerId != 0) {
//                    divider.setBackgroundColor(getResources().getColor(primaryColor));
//                }
            }
        });



        progress_listView = (ListView) drawerLayout.findViewById(R.id.prog_listView);
        List<LongTermEvent> events = new ArrayList<LongTermEvent>();
        LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
//        ArrayAdapter<LongTermEvent> mArrayAdapter = new ArrayAdapter<LongTermEvent>(getActivity(), R.layout.row_with_arrow, R.id.liftText, helper.getProgressList());
        ViewProgressArrayAdapter mArrayAdapter = new ViewProgressArrayAdapter(getActivity(), helper.getProgressList(m_currentView), false);
        progress_listView.setAdapter(mArrayAdapter);
        drawerLayout.setBackgroundColor(Color.BLACK);
        registerForContextMenu(progress_listView);

        setHasOptionsMenu(true);
        // Don't use this method, it's handled by inflater.inflate() above :
        // setContentView(R.layout.activity_layout);

        //Set up on onitemclicklistener for listview
        progress_listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                LongTermEvent clickedEvent = (LongTermEvent) progress_listView.getItemAtPosition(position);
                if (!clickedEvent.toString().contains("No previous")) {
                    Intent intent = new Intent(getActivity(), ProgressIndividualActivity.class);
                    intent.putExtra("date", clickedEvent.getLiftDate());
                    startActivity(intent);
                }

            }
        });


        // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :
        // Instead of :
        // findViewById(R.id.someGuiElement);
        return drawerLayout; // We must return the loaded Layout
    }


    /*************************************************
     Methods to manage context button
     ***************************************************/

    private Menu m_menu;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        m_menu = menu;
        menu.setHeaderTitle("Modify entry");
        menu.add(0, v.getId(), 0, "Delete Item");
        //edit may be able to be supported within workout
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        if (item.getTitle() == "Delete Item") {
            //remove item from database here and refresh listview...
//            progress_listView


            LongTermEvent clickedEvent = (LongTermEvent) progress_listView.getAdapter().getItem(info.position);
            if (!clickedEvent.toString().contains("No previous")) {//don't allow deletion of the "empty listview" entry
                removeLiftBasedOnListViewEntry(clickedEvent.getLiftDate(), clickedEvent.getLiftType(), clickedEvent.getFrequency());
            }
        }
        return super.onContextItemSelected(item);
    }

    public void removeLiftBasedOnListViewEntry(String date, String liftType, String freq)
    {
        String[] args = new String[3];
        args[0] = date;
        args[1] = liftType;
        args[2] = freq;

        LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(LongTermDataSQLHelper.TABLE, LongTermDataSQLHelper.LIFTDATE + "=? and " + LongTermDataSQLHelper.LIFT_TYPE + "=? and " + LongTermDataSQLHelper.FREQUENCY + "=?", args);
        //ArrayAdapter<LongTermEvent> mArrayAdapter = new ArrayAdapter<LongTermEvent>(getActivity(), R.layout.row_with_arrow, R.id.liftText, helper.getProgressList());//have separate layout with visibility
        ViewProgressArrayAdapter mArrayAdapter = new ViewProgressArrayAdapter(getActivity(), helper.getProgressList(m_currentView), true);
        progress_listView.setAdapter(mArrayAdapter);
        toggleListViewDeleteButtonShown(true);
    }




    /*************************************************
     Methods to manage edit/done button
    ***************************************************/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.accessory_menu, menu);
        mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem editMenuOptionItem = mMenu.findItem(R.id.editMenuOption);
        editMenuOptionItem.setEnabled(true);
        editMenuOptionItem.setVisible(true);
        //We need to initialize what image we want
        TextView v = new TextView(getActivity());
        v.setTextColor(Color.WHITE);
        v.setText("Edit");
        v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        v.setDrawingCacheEnabled(true);
        editMenuOptionItem.setIcon(createDrawableFromView(v));
    }


    public ArrayList<String> getCurrentListViewItems() {
        ListAdapter adapter = progress_listView.getAdapter();
        ArrayList<String> currentListViewItems = new ArrayList<String>();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (progress_listView.getChildAt(i) != null) {
                TextView t = (TextView) progress_listView.getChildAt(i).findViewById(R.id.liftText);
                final ImageView deleteButton = (ImageView) progress_listView.getChildAt(i).findViewById(R.id.deleteButton);
                deleteButton.setVisibility(View.VISIBLE);
                if (t != null)
                    currentListViewItems.add(t.getText().toString());
            }
        }
        return currentListViewItems;
    }



    public void toggleListViewDeleteButtonShown(boolean shown) {
        int count = getCurrentListViewItems().size();
        for (int i = 0; i < count/*adapter.getCount()*/; i++) {
            final View currentChild = progress_listView.getChildAt(i);
            final int currentPosition = i;
            if (currentChild != null) {
                final ImageView deleteButton = (ImageView) currentChild.findViewById(R.id.deleteButton);
                final TextView currentRowText = (TextView) currentChild.findViewById(R.id.liftText);
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
                                    Animation listViewAnimation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right); //load animation for listview
                                    listViewAnimation.setDuration(600);
                                    currentChild.startAnimation(listViewAnimation);
                                    // Execute some code after 2 seconds have passed
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity());
                                            SQLiteDatabase db = helper.getWritableDatabase();
                                            LongTermEvent eventToRemove = (LongTermEvent) progress_listView.getAdapter().getItem(currentPosition);
                                            removeLiftBasedOnListViewEntry(eventToRemove.getLiftDate(), eventToRemove.getLiftType(), eventToRemove.getFrequency());

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


    static boolean toggle = true;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMenuOption:
                if (toggle) {//Edit Pressed
                    TextView v = new TextView(getActivity());
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
                    TextView v = new TextView(getActivity());
                    v.setTextColor(Color.WHITE);
                    v.setText("Edit");
                    v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    v.setDrawingCacheEnabled(true);
                    item.setIcon(createDrawableFromView(v));
                    toggleListViewDeleteButtonShown(false);

                    toggle = !toggle;
                }

        }

//        Toast.makeText(getActivity(), "Clicked " + item.getItemId(), Toast.LENGTH_LONG).show();
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
