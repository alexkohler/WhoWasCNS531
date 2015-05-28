package com.kohlerbear.whowascnscalc;

/**
 * Created by alex on 5/25/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

//
/**
 * Created by alex on 5/17/15.
 */
public class IndividualProgressArrayAdapter extends ArrayAdapter<LongTermEvent> implements StickyListHeadersAdapter {

    private final Context context;
    private LongTermEvent[] values;
    private boolean visible = false;
    private LayoutInflater inflater;

    public IndividualProgressArrayAdapter(Context context, List<LongTermEvent> values, boolean visible) {
        super(context, R.layout.row_individual_view, values);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.values = new LongTermEvent[values.size()];
        this.values  = values.toArray(this.values);
        this.visible = visible;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_individual_view, parent, false);
        TextView liftText = (TextView) rowView.findViewById(R.id.liftNameTV);
        TextView setsRepsText = (TextView) rowView.findViewById(R.id.setRepsTV);
        final ImageView deleteButton = (ImageView) rowView.findViewById(R.id.deleteButtonIndV);

        LongTermEvent selectedEvent = values[position];
        liftText.setText(selectedEvent.getLiftName());
        setsRepsText.setText(selectedEvent.getWeight() + "x" + selectedEvent.getReps());
        if (visible)
            deleteButton.setVisibility(View.VISIBLE);
        else
            deleteButton.setVisibility(View.GONE);

        final int finalPos = position;
        final View finalConvertView = convertView;

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
                        Animation listViewAnimation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right); //load animation for listview
                        listViewAnimation.setDuration(600);
                        convertView.startAnimation(listViewAnimation);
                        // Execute some code after 2 seconds have passed
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                LongTermDataSQLHelper helper = new LongTermDataSQLHelper(context);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                LongTermEvent eventToRemove = values[finalPos];
                                //.makeText(context, "remove", Toast.LENGTH_SHORT).show();
                                String[] args = new String[4];
                                args[0] = eventToRemove.getLiftDate();
                                args[1] = eventToRemove.getLiftName();
                                args[2] = eventToRemove.getFrequency();
                                args[3] = eventToRemove.getLiftType();
                                db.delete(LongTermDataSQLHelper.TABLE, LongTermDataSQLHelper.LIFTDATE + "=? and " + LongTermDataSQLHelper.LIFT_NAME + "=? and " + LongTermDataSQLHelper.FREQUENCY + "=? and " + LongTermDataSQLHelper.LIFT_TYPE + "=?", args);
                            }
                        },610);


                    }
                }, 410);
                deleteButton.startAnimation(anim);
            }
        });


        return rowView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.row_individual_view, parent, false);
        TextView liftTitle = (TextView) convertView.findViewById(R.id.liftNameTV);
        liftTitle.setText("Name");


        TextView setRepsTV = (TextView) convertView.findViewById(R.id.setRepsTV);
        setRepsTV.setText("SetsxReps");
        convertView.setBackgroundColor(ColorManager.getInstance(context).getStickyHeaderBackgroundColor());
        convertView.setMinimumHeight(15);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return 7861;
    }




}

