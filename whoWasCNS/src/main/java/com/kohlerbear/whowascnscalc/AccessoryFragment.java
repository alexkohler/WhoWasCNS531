package com.kohlerbear.whowascnscalc;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kohlerbear.whowascnscalc.dragndroplist.DragNDropCursorAdapter;
import com.kohlerbear.whowascnscalc.dragndroplist.DragNDropListView;


/* NEW FUCKIN PLAN, WHEN A VIEW IS CHANGED, OR ONDESTROY IS CALLED OR SOME SHIT, MAKE SURE THERE IS AN UPDATE LIKE YOUR CONFIGUREBUTTON PRESSED. BOOM ROASTED*/
    public class AccessoryFragment extends Fragment {
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    DragNDropListView dragNDroplistView;
    SwipeDismissListViewTouchListener touchListener;
    DragNDropCursorAdapter adapter;
    Button changeLiftButton;
    AccessoryLiftSQLHelper helper;
    Menu mMenu;
    LayoutInflater mInflater;

    public enum CHANGELIFT_BUTTON_STATE {NORMAL, EXIT}

    ;
    CHANGELIFT_BUTTON_STATE currentButtonState;

    AccessoryLiftSQLHelper.ACCESSORY_TYPE currentAccessoryType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentActivity faActivity = (FragmentActivity) super.getActivity();
        drawerLayout = (DrawerLayout) inflater.inflate(R.layout.fragment_accessory_lift_tab_host, container, false);
        dragNDroplistView = (DragNDropListView) drawerLayout.findViewById(R.id.liftListView);
        registerForContextMenu(dragNDroplistView);
        //Colors
        changeLiftButton = (Button) drawerLayout.findViewById(R.id.changeLiftButton);
        final int primaryColor = ColorManager.getInstance(getActivity()).getPrimaryColor();
        changeLiftButton.setBackgroundColor(primaryColor);


        helper = new AccessoryLiftSQLHelper(getActivity());
        currentAccessoryType = AccessoryLiftSQLHelper.ACCESSORY_TYPE.BENCH; //will always open on bench
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("drop table " + helper.TABLE);
        db.execSQL("create table " + helper.TABLE + "(ACCESSORY text not null, ACCESSORY_TYPE text not null, LIFT_ORDER integer);");
        helper.makeSampleData();

        //Manage buttons
        changeLiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentButtonState == CHANGELIFT_BUTTON_STATE.NORMAL) {

                } else if (currentButtonState == CHANGELIFT_BUTTON_STATE.EXIT) {
                    changeLiftButton.clearAnimation();
                    changeLiftButton.setText("Change Lift");
                    currentButtonState = CHANGELIFT_BUTTON_STATE.NORMAL;
                    dragNDroplistView.setDraggingEnabled(false);
                    registerForContextMenu(dragNDroplistView);
                    helper.repopulateDB(getCurrentListViewItems(), currentAccessoryType.name().toUpperCase());
                    MenuItem editMenuOptionItem = mMenu.findItem(R.id.editMenuOption);
                    editMenuOptionItem.setEnabled(true);

                }
            }
        });


        ColorManager.clear();


        Cursor cursor = db.query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = 'BENCH' ORDER BY LIFT_ORDER ASC", null, null,
                null, null);
        adapter = new DragNDropCursorAdapter(getActivity().getApplicationContext(), R.layout.row, cursor, new String[]{AccessoryLiftSQLHelper.ACCESSORY}, new int[]{R.id.liftText}, R.id.liftText);
//            SimpleCursorAdapter c = new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.row, cursor, new String[] {AccessoryLiftSQLHelper.ACCESSORY}, new int[] {R.id.liftText});
        dragNDroplistView.setDragNDropAdapter(adapter);
        touchListener =
                new SwipeDismissListViewTouchListener(
                        dragNDroplistView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    SQLiteDatabase dbAccessories = helper.getWritableDatabase();
                                    Cursor c = dbAccessories.query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = 'BENCH' ORDER BY LIFT_ORDER ASC", null, null,
                                            null, null);
                                    c.moveToPosition(position);
                                    String removedLift = c.getString(1);
                                    dbAccessories.execSQL("delete from AccessoryTemplates where ACCESSORY = '" + removedLift + "'");//TODO add and clause here
                                    //requery after deleting to ensure we get those changes into our updated adapter
                                    c = dbAccessories.query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = 'BENCH' ORDER BY LIFT_ORDER ASC", null, null,
                                            null, null);//TODO type query
                                    DragNDropCursorAdapter updatedAdapter;
                                    updatedAdapter = new DragNDropCursorAdapter(getActivity().getApplicationContext(), R.layout.row_with_delete, c, new String[]{AccessoryLiftSQLHelper.ACCESSORY}, new int[]{R.id.liftText}, R.id.liftText);
                                    dragNDroplistView.setDragNDropAdapter(updatedAdapter);

                                    Toast.makeText(getActivity(), "Removing " + removedLift, Toast.LENGTH_LONG).show();
//                                                     final Cursor updatedCursor = dbLifts.query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.LIFTDATE}, "Lift = 'Bench'", null, null,
//                                                         null, null);



                                }
                                adapter.notifyDataSetChanged();
                                adapter.setDismisser(touchListener);
//                                addDeleteButtonListenersToCurrentListView();


                            }

                            public boolean canDismiss(int position) {
                                return true;
                            }

                        });

//            dragNDroplistView.setOnTouchListener(touchListener);
//            dragNDroplistView.setOnScrollListener(touchListener.makeScrollListener());
        dragNDroplistView.setDraggingEnabled(false);



        setHasOptionsMenu(true);

        dragNDroplistView.setItemsCanFocus(true);

        return drawerLayout; // We must return the loaded Layout
    }



    @Override
    public void onStop() {
        super.onStop();
        helper.repopulateDB(getCurrentListViewItems(), currentAccessoryType.name().toUpperCase());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Modify entry");
        menu.add(0, v.getId(), 0, "Move Item");
        menu.add(0, v.getId(), 0, "Rename Item");
        menu.add(0, v.getId(), 0, "Delete Item");
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);
        MenuItem i = menu.getItem(0);
        TextView v = new TextView(getActivity());
        v.setTextColor(Color.WHITE);
        v.setText("Edit");
//                    v.setTextSize(14);
        v.setTypeface(Typeface.DEFAULT_BOLD);
        v.setDrawingCacheEnabled(true);
        i.setIcon(createDrawableFromView(v));
//code here
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        String touchedLiftName = ((TextView) dragNDroplistView.getChildAt(info.position).findViewById(R.id.liftText)).getText().toString();
        if (item.getTitle() == "Move Item") {
            changeLiftButton.setText("Exit rearrange mode");
            changeLiftButton.startAnimation(animation);
            dragNDroplistView.setDraggingEnabled(true);
            unregisterForContextMenu(dragNDroplistView);
            currentButtonState = CHANGELIFT_BUTTON_STATE.EXIT;
//            mRowId = info.id;
//            DialogFragment_Item idFragment = new DialogFragment_Item();
//            idFragment.show(getFragmentManager(), "dialog");
            MenuItem editMenuOptionItem = mMenu.findItem(R.id.editMenuOption);
            editMenuOptionItem.setEnabled(false);
        } else if (item.getTitle() == "Delete Item") {
            dragNDroplistView.setOnTouchListener(touchListener);
            dragNDroplistView.setOnScrollListener(touchListener.makeScrollListener());
            dragNDroplistView.setOnTouchListener(null);
            dragNDroplistView.setOnScrollListener(null);
            adapter.notifyDataSetChanged();
//            Toast.makeText(getActivity(), "info " + ((TextView) dragNDroplistView.getChildAt(info.position).findViewById(R.id.liftText)).getText().toString(), Toast.LENGTH_SHORT).show();
            touchListener.dismiss(info.position);//TODO make sure this works in all cases and that saving happens in right spot
            return true;
        } else if (item.getTitle() == "Rename Item") {
            promptUserWithEditText(touchedLiftName);
        }
        return super.onContextItemSelected(item);
    }


    public ArrayList<String> getCurrentListViewItems() {
        ListAdapter adapter = dragNDroplistView.getAdapter();
        ArrayList<String> currentListViewItems = new ArrayList<String>();
        for (int i = 0; i < adapter.getCount(); i++) {
            TextView t = (TextView) dragNDroplistView.getChildAt(i).findViewById(R.id.liftText);
            currentListViewItems.add(t.getText().toString());
        }
        return currentListViewItems;
    }


    public void addDeleteButtonListenersToCurrentListView(ListAdapter a){
        dragNDroplistView = (DragNDropListView) drawerLayout.findViewById(R.id.liftListView);
        ListAdapter adapter = a;
        ArrayList<String> currentListViewItems = new ArrayList<String>();
        for (int childIndex = 0; childIndex < adapter.getCount(); childIndex++) {
            final int currentChildIndex = childIndex;
            ImageView deleteButton = (ImageView) dragNDroplistView.getChildAt(childIndex).findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                turnDeleteButtonAndRemoveListValueEntry(0, currentChildIndex);
//                toggleListViewDeleteButtonShown(true);
                }
            });
        }
    }


    public void toggleListViewDeleteButtonShown(boolean shown) {
        for (int i = 0; i < adapter.getCount(); i++) {
            View currentChild = dragNDroplistView.getChildAt(i);
            if (currentChild != null) {
                ImageView deleteButton = (ImageView) currentChild.findViewById(R.id.deleteButton);
                if (shown)
                    deleteButton.setVisibility(View.VISIBLE);
                else
                    deleteButton.setVisibility(View.GONE);
            }
        }
    }


    public void turnDeleteButtonAndRemoveListValueEntry(int currentRotation, int childIndex)
    {
        RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation + 90,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        currentRotation = (currentRotation + 30) % 360;

        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(400);
        anim.setFillEnabled(true);

        anim.setFillAfter(true);
        ImageView deleteButton = (ImageView) dragNDroplistView.getChildAt(childIndex).findViewById(R.id.deleteButton);
        final int currentChildIndex = childIndex;
        deleteButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                touchListener.dismiss(currentChildIndex);
            }
        }, 410);
        deleteButton.startAnimation(anim);
    }

    public void promptUserWithEditText(final String oldLiftName) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Rename entry");
//        alert.setMessage("Message");


// Set an EditText view to get user input
        final EditText input = new EditText(getActivity());
        alert.setView(input);

        alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String renamedLift = input.getText().toString();
                helper.updateEntry(oldLiftName, renamedLift, currentAccessoryType);
                refreshListView();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        AlertDialog myDialog = alert.show();


        //Fix up UI of dialog
        Resources resources = alert.getContext().getResources();

        int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");

        View titleDivider = myDialog.getWindow().getDecorView().findViewById(titleDividerId);
        titleDivider.setBackgroundColor(Color.TRANSPARENT); // change divider color
    }

    //Called after a change to cursor (Source of information for our listview)
    public void refreshListView() {
        SQLiteDatabase dbAccessories = helper.getWritableDatabase();//TODO spread these out
        Cursor c = dbAccessories.query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = 'BENCH' ORDER BY LIFT_ORDER ASC", null, null,
                null, null);//TODO change query
        DragNDropCursorAdapter updatedAdapter = new DragNDropCursorAdapter(getActivity().getApplicationContext(), R.layout.row, c, new String[]{AccessoryLiftSQLHelper.ACCESSORY}, new int[]{R.id.liftText}, R.id.liftText);
        dragNDroplistView.setDragNDropAdapter(updatedAdapter);
        updatedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.accessory_menu, menu);
        mMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

   boolean toggle = true;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editMenuOption:
                if (toggle) {//Edit Pressed
                    TextView v = new TextView(getActivity());
                    v.setTextColor(Color.WHITE);

//                    v.setTypeface(Typeface.DEFAULT_BOLD);
                    v.setText("Done");
                    v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    v.setDrawingCacheEnabled(true);
                    item.setIcon(createDrawableFromView(v));
                    toggleListViewDeleteButtonShown(true);
                    addDeleteButtonListenersToCurrentListView(adapter);
                    //Long click listener


                    //Null testing
//                    View v = dragNDroplistView.getChildAt(0);
//                    ImageView i = v.findViewWithTag();


                    dragNDroplistView.setLongClickable(false);
                    toggle = !toggle;
                    return true;
                } else //Done presse
                {
//                    item.setIcon(R.drawable.ic_edit);
                    TextView v = new TextView(getActivity());
                    v.setTextColor(Color.WHITE);
                    v.setText("Edit");
                    v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                    v.setTypeface(Typeface.DEFAULT_BOLD);
                    v.setDrawingCacheEnabled(true);
                    item.setIcon(createDrawableFromView(v));
                    toggleListViewDeleteButtonShown(false);
                    dragNDroplistView.setLongClickable(true);
//                    mDrawerToggle.syncState();
                     toggle = !toggle;

                }

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
