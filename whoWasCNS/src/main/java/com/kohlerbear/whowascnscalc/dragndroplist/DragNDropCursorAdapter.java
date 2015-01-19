/*
 * Copyright 2012 Terlici Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kohlerbear.whowascnscalc.dragndroplist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kohlerbear.whowascnscalc.AccessoryFragment;
import com.kohlerbear.whowascnscalc.AccessoryLiftSQLHelper;
import com.kohlerbear.whowascnscalc.R;
import com.kohlerbear.whowascnscalc.SwipeDismissListViewTouchListener;

import java.util.ArrayList;

public class DragNDropCursorAdapter extends SimpleCursorAdapter implements DragNDropAdapter {
	int mPosition[];
	int mHandler;
    LayoutInflater mLayoutInflater;
    SwipeDismissListViewTouchListener mDismisser;

	public DragNDropCursorAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int handler) {
		super(context, layout, cursor, from, to, 0);
		
		mHandler = handler;
        mLayoutInflater = LayoutInflater.from(context);
        setup();

	}

	@Override
	public Cursor swapCursor(Cursor c) {
		Cursor cursor = super.swapCursor(c);
		
		mPosition = null;
		setup();
		
		return cursor;
	}
	
	private void setup() {
		Cursor c = getCursor();
		
		if (c == null || !c.moveToFirst()) return;
		
		mPosition = new int[c.getCount()];
		
		for (int i = 0; i < mPosition.length; ++i) mPosition[i] = i;
	}


	@Override
	public View getDropDownView(int position, View view, ViewGroup group) {
		return super.getDropDownView(mPosition[position], view, group);
	}
	
	@Override
	public Object getItem(int position) {
		return super.getItem(mPosition[position]);
	}
	
	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(mPosition[position]);
	}
	
	@Override
	public long getItemId(int position) {
		return super.getItemId(mPosition[position]);
	}

	/*
	In the newView() method, you simply inflate the view and return it. In the bindView() method, you set the elements of your view.
	 */
	@Override
	public View getView(int position, View view, ViewGroup group) {
		return super.getView(mPosition[position], view, group);
	}



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.row, parent, false);
    }




    @Override
    public void bindView(View view, final Context context, Cursor c) {

        final TextView rowTitle = (TextView) view.findViewById(R.id.liftText);
        final String s = c.getString(c.getColumnIndex(AccessoryLiftSQLHelper.ACCESSORY)); //could do another static jawn to make sure you're getting the most updated cursor
        final int position = c.getPosition();
        rowTitle.setText(s);
        final ImageView deleteButton = (ImageView) view.findViewById(R.id.deleteButton);
        final TextView liftText = (TextView) view.findViewById(R.id.liftText);
        if (AccessoryFragment.areDeleteButtonShown())
            deleteButton.setVisibility(View.VISIBLE);
        else
            deleteButton.setVisibility(View.GONE);
        final Context myContext = context;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SwipeDismissListViewTouchListener touchListener = AccessoryFragment.getCurrentDismisser();
                int currentRotation = 0;
                RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation + 90,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
                currentRotation = (currentRotation + 30) % 360;

                anim.setInterpolator(new LinearInterpolator());
                anim.setDuration(400);
                anim.setFillEnabled(true);

                anim.setFillAfter(true);
                deleteButton.postDelayed(new Runnable() {
                    @Override
                    public void run() { //refresh database before attempting to delete, since our dataset is small, we can get away with this.
                        AccessoryLiftSQLHelper helper = new AccessoryLiftSQLHelper(context);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.execSQL("DELETE FROM " + AccessoryLiftSQLHelper.TABLE);
                        int orderCounter = 0;
                        ArrayList<String> values = AccessoryFragment.getCurrentListViewItemsStatic();
                        for (String liftValue : values)
                        {
                            helper.addAccessoryLift(liftValue, AccessoryLiftSQLHelper.ACCESSORY_TYPE.BENCH, orderCounter);
                            orderCounter++;
                        }
                        int positionToDelete = values.indexOf(rowTitle.getText().toString());
                        touchListener.dismiss(positionToDelete);
                    }
                }, 410);
                deleteButton.startAnimation(anim);

            }
        });
        System.out.println();


    }
	
	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(mPosition[position]);
	}

	@Override
	public void onItemDrag(DragNDropListView parent, View view, int position, long id) {
		
	}
    //TODO the swap has to happen right here I think
	@Override
	public void onItemDrop(DragNDropListView parent, View view, int startPosition, int endPosition, long id) {

        //Base code
        int position = mPosition[startPosition];
		
		if (startPosition < endPosition)
			for(int i = startPosition; i < endPosition; ++i)
				mPosition[i] = mPosition[i + 1];
		else if (endPosition < startPosition)
			for(int i = startPosition; i > endPosition; --i)
				mPosition[i] = mPosition[i - 1];
		
		mPosition[endPosition] = position;


	}


    @Override
    public void onPostItemDrop(DragNDropListView parent, View view, int startPosition, int endPosition) {

/*
        //Update everything after item has been dropped
        AccessoryLiftSQLHelper helper = new AccessoryLiftSQLHelper(parent.getContext());
        ListAdapter adapter = parent.getAdapter();
        final DragNDropListView finalParent = parent;
        ArrayList<String> currentListViewItems = new ArrayList<String>();
        for (int i = 0; i < adapter.getCount(); i++) {
            TextView t = (TextView) parent.getChildAt(i).findViewById(R.id.liftText);
            currentListViewItems.add(t.getText().toString());
        }
        Toast.makeText(parent.getContext(), "Updated", Toast.LENGTH_SHORT).show();
        helper.repopulateDB(currentListViewItems, "benchButItDoesn'tReallyMatterRightNow");//TODO must determine accessory type somehow

        //Refresh listview
        SQLiteDatabase dbAccessories = helper.getWritableDatabase();//TODO spread these out
        Cursor c = dbAccessories.query(AccessoryLiftSQLHelper.TABLE, new String[]{"rowid _id", AccessoryLiftSQLHelper.ACCESSORY}, "ACCESSORY_TYPE = 'BENCH' ORDER BY LIFT_ORDER ASC", null, null,
                null, null);//TODO change query
        DragNDropCursorAdapter updatedAdapter = new DragNDropCursorAdapter(parent.getContext(), R.layout.row, c, new String[]{AccessoryLiftSQLHelper.ACCESSORY}, new int[]{R.id.liftText}, R.id.liftText);
        parent.setDragNDropAdapter(updatedAdapter);
        updatedAdapter.notifyDataSetChanged();
*/



    }

	@Override
	public int getDragHandler() {
		return mHandler;
	}
	
}
