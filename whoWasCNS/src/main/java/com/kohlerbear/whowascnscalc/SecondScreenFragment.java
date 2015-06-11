package com.kohlerbear.whowascnscalc;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.widget.DrawerLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.kohlerbear.whowascnscalc.ToastWrapper.Boast;
import com.kohlerbear.whowascnscalc.hoang8f.segmented.SegmentedGroup;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;


public class SecondScreenFragment extends android.support.v4.app.Fragment {


    TextView option1, option2, option3, option4, option5;
    TextView[] options = {option1, option2, option3, option4, option5};

    TextView choice1, choice2, choice3, choice4, choice5, choice6, choice7;
    TextView[] choices = {choice1, choice2, choice3, choice4, choice5, choice6, choice7};

//    TextView patternSizeTV;
    EditText choice1F, choice2F, choice3F, choice4F, choice5F, choice6F, choice7F;
    EditText[] choiceFields = {choice1F, choice2F, choice3F, choice4F, choice5F, choice6F, choice7F};

    Button customButton, saveButton;
    String[] liftPattern = new String[7]; //max size of 7
    String[] defaultPattern = {"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"};
    String[] emptyPattern = {"First", "Second", "Third", "Fourth", "Fifth", "Sixth"};

    EditText benchEditText;
    EditText squatEditText;
    EditText ohpEditText;
    EditText deadEditText;

    //TM widgets
    SegmentedGroup patternSegmentGroup;
    RadioButton patternFourDaysRadioButton;
    RadioButton patternFiveDaysRadioButton;
    RadioButton patternSixDaysRadioButton;
    RadioButton patternSevenDaysRadioButton;

    RadioButton lbRadioButton;
    RadioButton kgRadioButton;


    //TM vars
    Boolean lbs = true;
    String unit_mode;
    String startingDate;


    Tracker tracker = null;

    DrawerLayout drawerLayout;

    public enum CUSTOM_BUTTON_STATE {NORMAL, CUSTOM};

    CUSTOM_BUTTON_STATE customButtonState;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity faActivity = (FragmentActivity) super.getActivity();
        // Replace LinearLayout by the type of the root element of the layout you're trying to load
        drawerLayout = (DrawerLayout) inflater.inflate(R.layout.activity_second_screen_prototype, container, false);
        // Of course you will want to faActivity and llLayout in the class and not this method to access them in the rest of
        // the class, just initialize them here

        // Content of previous onCreate() here
        // ...

        // Don't use this method, it's handled by inflater.inflate() above :
        customButton = (Button) drawerLayout.findViewById(R.id.customButton);
        saveButton = (Button) drawerLayout.findViewById(R.id.adjustActivitySaveButton);

        customButtonState = CUSTOM_BUTTON_STATE.NORMAL;
        customButton.setOnClickListener(customListener);



        SegmentedGroup unitModeGroup = (SegmentedGroup) drawerLayout.findViewById(R.id.poundKilogramSegmentedButtonGroup);



        lbRadioButton = (RadioButton) drawerLayout.findViewById(R.id.lbSegmentedButton);
        kgRadioButton = (RadioButton) drawerLayout.findViewById(R.id.kgSegmentedButton);
        unitModeGroup.setOnCheckedChangeListener(poundKilogramSegmentListener);


        patternSegmentGroup = (SegmentedGroup) drawerLayout.findViewById(R.id.patternSegmentGroup);







        patternFourDaysRadioButton = (RadioButton) drawerLayout.findViewById(R.id.patternButtonFourDays);
        patternFiveDaysRadioButton = (RadioButton) drawerLayout.findViewById(R.id.patternButtonFiveDays);
        patternSixDaysRadioButton = (RadioButton) drawerLayout.findViewById(R.id.patternButtonSixDays);
        patternSevenDaysRadioButton = (RadioButton) drawerLayout.findViewById(R.id.patternButtonSevenDays);
        patternSegmentGroup.setOnCheckedChangeListener(patternSizeSegmentListener);

//        patternSizeTV = (TextView) drawerLayout.findViewById(R.id.PatternSizeTV);
//        patternSizeTV.setVisibility(View.INVISIBLE);


        //set up analytics tracking
        tracker = GoogleAnalytics.getInstance(getActivity()).getTracker("UA-55018534-1");
        HashMap<String, String> hitParameters = new HashMap<String, String>();
        hitParameters.put(Fields.HIT_TYPE, "appview");
        hitParameters.put(Fields.SCREEN_NAME, "Pattern Screen");

        tracker.send(hitParameters);


        //views to drag
        option1 = (TextView) drawerLayout.findViewById(R.id.option1);
        option2 = (TextView) drawerLayout.findViewById(R.id.option2);
        option3 = (TextView) drawerLayout.findViewById(R.id.option3);
        option4 = (TextView) drawerLayout.findViewById(R.id.option4);
        option5 = (TextView) drawerLayout.findViewById(R.id.option5);

        options = new TextView[]{option1, option2, option3, option4, option5};
        //views to drop onto
        choice1 = (TextView) drawerLayout.findViewById(R.id.choice_1);
        choice2 = (TextView) drawerLayout.findViewById(R.id.choice_2);
        choice3 = (TextView) drawerLayout.findViewById(R.id.choice_3);
        choice4 = (TextView) drawerLayout.findViewById(R.id.choice_4);
        choice5 = (TextView) drawerLayout.findViewById(R.id.choice_5);
        choice6 = (TextView) drawerLayout.findViewById(R.id.choice_6);
        choice7 = (TextView) drawerLayout.findViewById(R.id.choice_7);

        choices = new TextView[]{choice1, choice2, choice3, choice4, choice5, choice6, choice7};

        //If there is an error on the choice, make sure the user is able to touch the choice and show the error.
        for (TextView choice : choices) {
            choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView choiceClicked = (TextView) drawerLayout.findViewById(view.getId());
                    if (choiceClicked.getError() != null)
                        choiceClicked.requestFocus();
                }
            });

        }

        //fields views are attributed with
        choice1F = (EditText) drawerLayout.findViewById(R.id.choice1Field);
        choice2F = (EditText) drawerLayout.findViewById(R.id.choice2Field);
        choice3F = (EditText) drawerLayout.findViewById(R.id.choice3Field);
        choice4F = (EditText) drawerLayout.findViewById(R.id.choice4Field);
        choice5F = (EditText) drawerLayout.findViewById(R.id.choice5Field);
        choice6F = (EditText) drawerLayout.findViewById(R.id.choice6Field);
        choice7F = (EditText) drawerLayout.findViewById(R.id.choice7Field);

        choiceFields = new EditText[]{choice1F, choice2F, choice3F, choice4F, choice5F, choice6F, choice7F};


        //set touch listeners
        option1.setOnTouchListener(new ChoiceTouchListener());
        option2.setOnTouchListener(new ChoiceTouchListener());
        option3.setOnTouchListener(new ChoiceTouchListener());
        option4.setOnTouchListener(new ChoiceTouchListener());
        option5.setOnTouchListener(new ChoiceTouchListener());

        //set drag listeners
        choice1.setOnDragListener(new ChoiceDragListener());
        choice2.setOnDragListener(new ChoiceDragListener());
        choice3.setOnDragListener(new ChoiceDragListener());
        choice4.setOnDragListener(new ChoiceDragListener());
        choice5.setOnDragListener(new ChoiceDragListener());
        choice6.setOnDragListener(new ChoiceDragListener());
        choice7.setOnDragListener(new ChoiceDragListener());


//	    inflatePatternButtons(origin);
        inflatePatternButtons(defaultPattern, false);//not custom, in oncreate the choices wil be


        //touch interceptor to clear errors if user presses outside the error bubble
        FrameLayout touchInterceptor = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();//to stop warnings
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    for (TextView choice : choices) {
                        if (choice.isFocused()) {
                            Rect outRect = new Rect();
                            choice.getGlobalVisibleRect(outRect);
                            if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                                choice.clearFocus();
                                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            }
                        }
                    }
                }
                return false;
            }
        });


        //Manage colors
        //COLORS
        int primaryColor = ColorManager.getInstance(getActivity()).getPrimaryColor();
        ColorManager.clear();

        unitModeGroup.setTintColor(primaryColor);
        patternSegmentGroup.setTintColor(primaryColor);
        customButton.setBackgroundColor(primaryColor);

        for (TextView option : options) {
            GradientDrawable backgroundGradient = (GradientDrawable) option.getBackground();
            backgroundGradient.setColor(primaryColor);
        }


        // The FragmentActivity doesn't contain the layout directly so we must use our instance of     LinearLayout :
        // Instead of :
        // drawerLayout.findViewById(R.id.someGuiElement);
        return drawerLayout; // We must return the loaded Layout
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // Make sure that we are currently visible
        if (this.isVisible()) {

            String origin = TabPrototype.origin;

            if (origin.equals("first")) {
                startingDate = TabPrototype.formattedDate;
            }
            // If we are **becoming** invisible, then...
            if (!isVisibleToUser) {
                TabPrototype.origin = "second";
//                if (TabPrototype.nextFrag.equals("third")) {
/*                    int currentPage = 1;
                    TabPrototype.builder.setMessage("Overwrite existing projection?");
                    TabPrototype.builder.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", resetListener);
                    TabPrototype.builder.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", resetListener);
                    if (validatePattern() == false || canMoveToThird() == false) {

                    }
                    else
                        TabPrototype.builder.show();
                }
                if (TabPrototype.nextFrag.equals("first"))
                    Toast.makeText(getActivity(), "first", Toast.LENGTH_SHORT).show();*/
//                }
            }
        }
    }

/*                        DialogInterface.OnClickListener resetListener = new DialogInterface.OnClickListener() {
//                            final ThirdScreenFragment fragment2 = (ThirdScreenFragment) TabPrototype.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
*//*                                        TabPrototype.mViewPager.setCurrentItem(2);//go to third screen
                                        fragment2.setInsertStatus(false);
                                        getSupportFragmentManager()
                                                .beginTransaction()
                                                .detach(fragment2)
                                                .attach(fragment2)
                                                .commit();//recreate view*//*
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.cancel();
                                        break;
                                }
                            }
                        };*/

    private void inflatePatternButtons(String[] pattern, boolean custom) {
        liftPattern = pattern;
        if (custom) {
            liftPattern = new String[]{"First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh"};
//            patternSizeTV.setVisibility(View.VISIBLE);
        }
        int patternIndex = 0;
        while (patternIndex < liftPattern.length) {
            choices[patternIndex].setText(liftPattern[patternIndex]);
            if (!custom) //if we aren't coming from a custom, bold the pattern and hide the fields for rest
            {
//                patternSizeTV.setVisibility(View.INVISIBLE);//hide pattern size text view
                choices[patternIndex].setTypeface(Typeface.DEFAULT_BOLD);
                if (choices[patternIndex].getText().toString().intern().equals("Rest"))
                    choiceFields[patternIndex].setVisibility(View.INVISIBLE);
            } else //otherwise if we are, hide all the fields and make sure we have no bold! (Because they are numerics)
            {
                choices[patternIndex].setTypeface(Typeface.DEFAULT);
                choices[patternIndex].setError(null);
                choiceFields[patternIndex].setVisibility(View.INVISIBLE);
            }


            patternIndex++;
        }
        if (patternIndex < choices.length) {
            while (patternIndex < choices.length) {
                TextView choice = choices[patternIndex];
                choice.setVisibility(View.INVISIBLE);
                choiceFields[patternIndex].setVisibility(View.INVISIBLE);
                patternIndex++;
            }

        }

        if (custom)
            patternSevenDaysRadioButton.setChecked(true);
        else if (!custom)
            patternSixDaysRadioButton.setChecked(true);
    }

    private RadioGroup.OnCheckedChangeListener poundKilogramSegmentListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            if (lbRadioButton.isChecked()) {
                lbs = true;
            }


            if (kgRadioButton.isChecked())
                lbs = false;


        }
    };

    private RadioGroup.OnCheckedChangeListener patternSizeSegmentListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                //TODO do this with loops good christ are you stupid?
                case R.id.patternButtonFourDays: //pattern size is 4
                    //choices
                    liftPattern = new String[4];
                    choice1.setVisibility(View.VISIBLE);
                    choice2.setVisibility(View.VISIBLE);
                    choice3.setVisibility(View.VISIBLE);
                    choice4.setVisibility(View.VISIBLE);
                    choice5.setVisibility(View.INVISIBLE);
                    choice6.setVisibility(View.INVISIBLE);
                    choice7.setVisibility(View.INVISIBLE);
                    //fields
                    //choice 1 field
                    if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
                        choice1F.setVisibility(View.VISIBLE);
                    else
                        choice1F.setVisibility(View.INVISIBLE);

                    //choice 2 field
                    if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
                        choice2F.setVisibility(View.VISIBLE);
                    else
                        choice2F.setVisibility(View.INVISIBLE);

                    //choice 3 field
                    if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
                        choice3F.setVisibility(View.VISIBLE);
                    else
                        choice3F.setVisibility(View.INVISIBLE);

                    //choice 4 field
                    if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
                        choice4F.setVisibility(View.VISIBLE);
                    else
                        choice4F.setVisibility(View.INVISIBLE);

                    choice5F.setVisibility(View.INVISIBLE);
                    choice6F.setVisibility(View.INVISIBLE);
                    choice7F.setVisibility(View.INVISIBLE);
                    break;
                case R.id.patternButtonFiveDays: //pattern size is 5
                    liftPattern = new String[5];
                    //choices
                    choice1.setVisibility(View.VISIBLE);
                    choice2.setVisibility(View.VISIBLE);
                    choice3.setVisibility(View.VISIBLE);
                    choice4.setVisibility(View.VISIBLE);
                    choice5.setVisibility(View.VISIBLE);
                    choice6.setVisibility(View.INVISIBLE);
                    choice7.setVisibility(View.INVISIBLE);
                    //fields

                    //choice 1 field
                    if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
                        choice1F.setVisibility(View.VISIBLE);
                    else
                        choice1F.setVisibility(View.INVISIBLE);

                    //choice 2 field
                    if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
                        choice2F.setVisibility(View.VISIBLE);
                    else
                        choice2F.setVisibility(View.INVISIBLE);

                    //choice 3 field
                    if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
                        choice3F.setVisibility(View.VISIBLE);
                    else
                        choice3F.setVisibility(View.INVISIBLE);

                    //choice 4 field
                    if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
                        choice4F.setVisibility(View.VISIBLE);
                    else
                        choice4F.setVisibility(View.INVISIBLE);

                    //choice 5 field
                    if (!choice5.getText().toString().equals("Rest") && !choice5.getText().toString().equals("Fifth"))
                        choice5F.setVisibility(View.VISIBLE);
                    else
                        choice5F.setVisibility(View.INVISIBLE);


                    choice6F.setVisibility(View.INVISIBLE);
                    choice7F.setVisibility(View.INVISIBLE);
                    break;
                case R.id.patternButtonSixDays://patterm size is 6
                    liftPattern = new String[6];
                    //choices
                    choice1.setVisibility(View.VISIBLE);
                    choice2.setVisibility(View.VISIBLE);
                    choice3.setVisibility(View.VISIBLE);
                    choice4.setVisibility(View.VISIBLE);
                    choice5.setVisibility(View.VISIBLE);
                    choice6.setVisibility(View.VISIBLE);
                    choice7.setVisibility(View.INVISIBLE);
                    //fields
                    //choice 1 field
                    if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
                        choice1F.setVisibility(View.VISIBLE);
                    else
                        choice1F.setVisibility(View.INVISIBLE);

                    //choice 2 field
                    if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
                        choice2F.setVisibility(View.VISIBLE);
                    else
                        choice2F.setVisibility(View.INVISIBLE);

                    //choice 3 field
                    if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
                        choice3F.setVisibility(View.VISIBLE);
                    else
                        choice3F.setVisibility(View.INVISIBLE);

                    //choice 4 field
                    if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
                        choice4F.setVisibility(View.VISIBLE);
                    else
                        choice4F.setVisibility(View.INVISIBLE);

                    //choice 5 field
                    if (!choice5.getText().toString().equals("Rest") && !choice5.getText().toString().equals("Fifth"))
                        choice5F.setVisibility(View.VISIBLE);
                    else
                        choice5F.setVisibility(View.INVISIBLE);

                    //choice 6 field
                    if (!choice6.getText().toString().equals("Rest") && !choice6.getText().toString().equals("Sixth"))
                        choice6F.setVisibility(View.VISIBLE);
                    else
                        choice6F.setVisibility(View.INVISIBLE);

                    choice7F.setVisibility(View.INVISIBLE);
                    break;
                case R.id.patternButtonSevenDays:// pattern size is 7
                    liftPattern = new String[7];
                    //choices
                    choice1.setVisibility(View.VISIBLE);
                    choice2.setVisibility(View.VISIBLE);
                    choice3.setVisibility(View.VISIBLE);
                    choice4.setVisibility(View.VISIBLE);
                    choice5.setVisibility(View.VISIBLE);
                    choice6.setVisibility(View.VISIBLE);
                    choice7.setVisibility(View.VISIBLE);
                    //fields
                    //choice 1 field
                    if (!choice1.getText().toString().equals("Rest") && !choice1.getText().toString().equals("First"))
                        choice1F.setVisibility(View.VISIBLE);
                    else
                        choice1F.setVisibility(View.INVISIBLE);

                    //choice 2 field
                    if (!choice2.getText().toString().equals("Rest") && !choice2.getText().toString().equals("Second"))
                        choice2F.setVisibility(View.VISIBLE);
                    else
                        choice2F.setVisibility(View.INVISIBLE);

                    //choice 3 field
                    if (!choice3.getText().toString().equals("Rest") && !choice3.getText().toString().equals("Third"))
                        choice3F.setVisibility(View.VISIBLE);
                    else
                        choice3F.setVisibility(View.INVISIBLE);

                    //choice 4 field
                    if (!choice4.getText().toString().equals("Rest") && !choice4.getText().toString().equals("Fourth"))
                        choice4F.setVisibility(View.VISIBLE);
                    else
                        choice4F.setVisibility(View.INVISIBLE);

                    //choice 5 field
                    if (!choice5.getText().toString().equals("Rest") && !choice5.getText().toString().equals("Fifth"))
                        choice5F.setVisibility(View.VISIBLE);
                    else
                        choice5F.setVisibility(View.INVISIBLE);

                    //choice 6 field
                    if (!choice6.getText().toString().equals("Rest") && !choice6.getText().toString().equals("Sixth"))
                        choice6F.setVisibility(View.VISIBLE);
                    else
                        choice6F.setVisibility(View.INVISIBLE);

                    //choice 7 field
                    if (!choice7.getText().toString().equals("Rest") && !choice7.getText().toString().equals("Seventh"))
                        choice7F.setVisibility(View.VISIBLE);
                    else
                        choice7F.setVisibility(View.INVISIBLE);
            }

        }
    };


    private final class ChoiceTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            view.performClick();
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //setup drag
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                //start dragging the item touched
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }

    private class ChoiceDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //handle drag events
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;
                case DragEvent.ACTION_DROP:
                    //handle the dragged view being dropped over a drop view
                    View view = (View) event.getLocalState();
                    //stop displaying the view where it was before it was dragged
//		    	view.setVisibility(View.INVISIBLE);
                    //view dragged item is being dropped on
                    TextView dropTarget = (TextView) v;
                    //view being dragged and dropped
                    TextView dropped = (TextView) view;

                    String droppedLiftName = dropped.getText().toString();
                    String viewName = getResources().getResourceEntryName(dropTarget.getId());
                    Integer viewSuffix = Integer.valueOf(viewName.substring(viewName.length() - 1));//eg _1, _2, etc..
                    if (droppedLiftName.equals("Rest"))
                        choiceFields[viewSuffix - 1].setVisibility(View.INVISIBLE);//arays are zero indexed
                    else //make sure field is visible
                        choiceFields[viewSuffix - 1].setVisibility(View.VISIBLE);
                    //regardless, clear the error on the field.
                    choiceFields[viewSuffix - 1].setError(null);
                    String dropArea = (String) dropTarget.getText();
                    //update the text in the target view to reflect the data being dropped
                    dropTarget.setText(dropped.getText());
                    updateLiftArray(dropArea.toLowerCase(Locale.getDefault()), (String) dropped.getText());
                    //make it bold to highlight the fact that an item has been dropped
                    dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
                    dropTarget.setError(null);
                    //if an item has already been dropped here, there will be a tag
                    Object tag = dropTarget.getTag();
                    //if there is already an item here, set it back visible in its original place
                    if (tag != null) {
                        //the tag is the view id already dropped here
                        int existingID = (Integer) tag;
                        //set the original view visible again
                        drawerLayout.findViewById(existingID).setVisibility(View.VISIBLE);
                    }
                    //set the tag in the target view to the ID of the view being dropped
                    dropTarget.setTag(dropped.getId());
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }
            return true;
        }

        private void updateLiftArray(String dropArea, String liftName) {

            switch (dropArea) {
                case "first":
                    liftPattern[0] = liftName;
                    break;
                case "second":
                    liftPattern[1] = liftName;
                    break;
                case "third":
                    liftPattern[2] = liftName;
                    break;
                case "fourth":
                    liftPattern[3] = liftName;
                    break;
                case "fifth":
                    liftPattern[4] = liftName;
                    break;
                case "sixth":
                    liftPattern[5] = liftName;
                    break;
                case "seventh":
                    liftPattern[6] = liftName;
                    break;
            }

        }
    }//end class ChoiceDragListener


    private View.OnClickListener customListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(1000); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
            if (customButtonState == CUSTOM_BUTTON_STATE.NORMAL) {
                inflatePatternButtons(emptyPattern, true);
                customButton.setText("Reset to default");
                customButton.startAnimation(animation);
                customButtonState = CUSTOM_BUTTON_STATE.CUSTOM;
                showOptions(true);
            }
            else if (customButtonState == CUSTOM_BUTTON_STATE.CUSTOM){
                inflatePatternButtons(defaultPattern, false);
                customButton.setText("Custom Pattern");
                customButton.clearAnimation();
                customButtonState = CUSTOM_BUTTON_STATE.NORMAL;
                showOptions(false);
            }
        }



    };




    void showOptions(boolean show) {

            for (TextView option : options)  {
                if (show)
                    option.setVisibility(View.VISIBLE);
                else
                    option.setVisibility(View.INVISIBLE);
            }

        }


/*    private View.OnClickListener saveListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (validatePattern()) {
                forwardToThird();
            }
        }
    };*/


    public boolean validatePattern() {
        //traverse the array and make sure all four lifts are used.
        int benchCount = 0, squatCount = 0, ohpCount = 0, deadCount = 0;
        boolean liftValidity = false, toastThrown = false;
        for (int i = 0; i < liftPattern.length; i++) {
            liftPattern[0] = choice1.getText().toString();
            liftPattern[1] = choice2.getText().toString();
            liftPattern[2] = choice3.getText().toString();
            liftPattern[3] = choice4.getText().toString();
            if (liftPattern.length > 4)
                liftPattern[4] = choice5.getText().toString();
            if (liftPattern.length > 5)
                liftPattern[5] = choice6.getText().toString();
            if (liftPattern.length > 6)
                liftPattern[6] = choice7.getText().toString();
            String currentLift = liftPattern[i];
            //check if null and break if so..
            if (currentLift.equals(null)) {
                Toast.makeText(getActivity(), "You left spot " + (i + 1) + " open! Please use all spots or choose a smaller pattern size.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if ((currentLift.equals("First") || currentLift.equals("Second") || currentLift.equals("Third") || currentLift.equals("Fourth")
                    || currentLift.equals("Fifth") || currentLift.equals("Sixth") || currentLift.equals("Seventh"))/* && !toastThrown*/) {
                //TODO there is a bug somewhere around here... showing an error on a hidden textview
//			Toast.makeText(REVAMPEDSecondScreenActivity.this, "You left a spot open! Please use all spots or choose a smaller pattern size.", Toast.LENGTH_LONG).show();
                //experimental error handling
                choices[i].requestFocus();
                choices[i].setError("You left this spot empty!");
                toastThrown = true;
            } else {
                choices[i].setError(null);
            }
            switch (currentLift) {
                case "Bench":
                    benchCount++;
                    break;
                case "Squat":
                    squatCount++;
                    break;
                case "Deadlift":
                    deadCount++;
                    break;
                case "OHP":
                    ohpCount++;
                    break;
            }
        }

        liftValidity = (benchCount == 1) && (squatCount == 1) && (deadCount == 1) && (ohpCount == 1) && !toastThrown;
        if (liftValidity)
            return true;
        else {
            boolean multipleLifts = false;
            boolean zeroLifts = false;
            ;
            String addBuffer = "Please add the following days to your pattern: ";
            String multipleBuffer = "You have multiples of the following lifts: ";
            if ((benchCount == 0)) {
                addBuffer = addBuffer + "Bench ";
                zeroLifts = true;
            } else if (benchCount > 1) {
                multipleBuffer = multipleBuffer + "Bench ";
                multipleLifts = true;
            }
            if ((squatCount == 0)) {
                addBuffer = addBuffer + "Squat ";
                zeroLifts = true;
            } else if (squatCount > 1) {
                multipleBuffer = multipleBuffer + "Squat ";
                multipleLifts = true;
            }
            if ((deadCount == 0)) {
                addBuffer = addBuffer + "Deadlift ";
                zeroLifts = true;
            } else if (deadCount > 1) {
                multipleLifts = true;
                multipleBuffer = multipleBuffer + "Deadlift ";
            }
            if ((ohpCount == 0)) {
                addBuffer = addBuffer + "OHP ";
                zeroLifts = true;
            } else if (ohpCount > 1) {
                multipleLifts = true;
                multipleBuffer = multipleBuffer + "OHP";
            }
            if (zeroLifts) {
                Boast.makeText(getActivity(), addBuffer, Toast.LENGTH_LONG).show();
            }

            if (multipleLifts)
                Boast.makeText(getActivity(), multipleBuffer, Toast.LENGTH_LONG).show();


            return false;

        }

    }

    public boolean canMoveToThird()//back end to SAVE button, where ever that may end up laying. Still need to bring in 'lbs' boolean, 'unit_mode' boolean, make sure you grab all depenendencies
    {
        int buttonID = patternSegmentGroup.getCheckedRadioButtonId();
        View selectedRadioButton = patternSegmentGroup.findViewById(buttonID);
        int visibleFieldsNew = patternSegmentGroup.indexOfChild(selectedRadioButton) + 4; //add 4?

        for (int i = 0; i < visibleFieldsNew; i++) {
            String liftName = choices[i].getText().toString();
            switch (liftName) {
                case "Bench":
                    benchEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "benchEditText associated with choiceField[" + i + "]", Toast.LENGTH_SHORT).show();
                    break;
                case "Squat":
                    squatEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "squatEditText associated with choiceField[" + i + "]", Toast.LENGTH_SHORT).show();
                    break;
                case "OHP":
                    ohpEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "ohpEditText associated with choiceField[" + i + "]", Toast.LENGTH_SHORT).show();
                    break;
                case "Deadlift":
                    deadEditText = choiceFields[i];
//				Toast.makeText(getApplicationContext(), "deadEditText associated with choiceField[" + i + "]", Toast.LENGTH_LONG).show();
                    break;
                default://don't need to identify any fields for rest or numerical values
                    break;
            }

        }

        //plug our associated EditTexts back into old back end code from original second screen activity

        //if we throw errors, we will need the erraneous column value
        String errorBench = benchEditText.getText().toString();
        String errorSquat = squatEditText.getText().toString();
        String errorOHP = ohpEditText.getText().toString();
        String errorDead = deadEditText.getText().toString();
        String errorStream = "";
        //error name definitions (to make appends cleaner looking)
        return handleErrors(errorBench, errorSquat, errorOHP, errorDead,
                errorStream);


    }


    public boolean handleErrors(String errorBench, String errorSquat,
                                String errorOHP, String errorDead, String errorStream) {
        String emptyBenchString = "Please enter a starting bench number!";
        String thousandBenchString = errorBench + " lbs? Let's be real here. Enter your actual bench.";
        String thousandBenchStringKgs = errorBench + "kgs? Let's be real here. Enter your actual bench.";
        String zeroBenchString = "Please enter a bench greater than 0lbs!";

        String emptySquatString = "Please enter a starting squat number!";
        String thousandSquatString = errorSquat + " lbs? Let's be real here. Enter your actual squat.";
        String thousandSquatStringKgs = errorSquat + " kgs? Let's be real here. Enter your actual squat.";
        String zeroSquatString = "Please enter a squat greater than 0lbs!";

        String emptyOHPString = "Please enter a starting OHP number!";
        String thousandOHPString = errorOHP + " lbs? Let's be real here. Enter your actual OHP.";
        String thousandOHPStringKgs = errorOHP + " kgs? Let's be real here. Enter your actual OHP.";
        String zeroOHPString = "Please enter a OHP greater than 0lbs!";

        String emptyDeadliftString = "Please enter a starting deadlift number!";
        String thousandDeadliftString = errorDead + " lbs? Let's be real here. Enter your actual deadlift.";
        String thousandDeadliftStringKgs = errorDead + " kgs? Let's be real here. Enter your actual deadlift.";
        String zeroDeadliftString = "Please enter a deadlift greater than 0lbs!";

        //all error flags initially false...
        Boolean benchErrorFlag = false;
        Boolean squatErrorFlag = false;
        Boolean ohpErrorFlag = false;
        Boolean deadErrorFlag = false;
//		Boolean spinnerErrorFlag = false; //possibly deprecated if number of cycles gets removed from projection
        //first, get data from previous screen (starting date passed to second from first)
        Intent intent = getActivity().getIntent();

        ConfigTool ct = new ConfigTool(getActivity());
        if (startingDate == null && !ct.dbEmpty())
            startingDate = ct.getStartingDateFromDatabase();

        TabPrototype.formattedDate = startingDate; //technically not necessary since we now have fields and don't have to worry on an activity to activity basis like we did w/ intents
        TabPrototype.liftPattern = liftPattern;
        NumberFormat nf = NumberFormat.getInstance(); //get user's locale to make sure we parse correctly

        //second, get our starting lifts
        String bench = benchEditText.getText().toString();

        double benchDouble = 0;
        try {
            benchDouble = nf.parse(bench).doubleValue();
        } catch (ParseException e) {
            if (bench.matches("^[0-9]+(.[0-9]{1,3})?$"))
                sendTrackerException("ParseException", bench);

            else
                benchEditText.setError("Please check the format of your training max");
        }

        //Some android phones are capable of accessing their full keyboard, add error checking to ensure that no commas,

        //null error handling
        if (bench.equals("")) {
            benchEditText.setError(emptyBenchString);
            benchErrorFlag = true;
        } else {
            if ((benchDouble >= 1000 && lbs) || (benchDouble >= 500 && !lbs)) {
                if (lbs)
                    benchEditText.setError(thousandBenchString);
                if (!lbs)
                    benchEditText.setError(thousandBenchStringKgs);
                benchErrorFlag = true;

            }
            if (benchDouble == 0) {
                benchEditText.setError(zeroBenchString);
                benchErrorFlag = true;
            }
        }

        if (benchErrorFlag.equals(false))
            TabPrototype.benchTM = bench;

        //end bench error handling


        String squat = squatEditText.getText().toString();

        double squatDouble = 0;
        try {
            squatDouble = nf.parse(squat).doubleValue();
        } catch (ParseException e) {

            if (squat.matches("^[0-9]+(.[0-9]{1,3})?$")) //If a legitimate number was entered (regex for decimal, regardless of comma/decimal
                sendTrackerException("ParseException", squat);

            else
                squatEditText.setError("Please check the format of your training max.");
        }

        //null error handling
        if (squat.equals("")) {
            squatEditText.setError(emptySquatString);
            squatErrorFlag = true;
        } else {
            if ((squatDouble >= 1500 && lbs) || (squatDouble > 600 && !lbs)) {
                if (lbs)
                    squatEditText.setError(thousandSquatString);
                if (!lbs)
                    squatEditText.setError(thousandSquatStringKgs);
                squatErrorFlag = true;
            }
            if (squatDouble == 0) {
                squatEditText.setError(zeroSquatString);
                squatErrorFlag = true;
            }
        }

        if (squatErrorFlag.equals(false))
           TabPrototype.squatTM = squat;

        //end squat error handling


        String OHP = ohpEditText.getText().toString();


        double ohpDouble = 0;
        try {
            ohpDouble = nf.parse(OHP).doubleValue();
        } catch (ParseException e) {
            if (OHP.matches("^[0-9]+(.[0-9]{1,3})?$"))
                sendTrackerException("ParseException", OHP);

            else
                ohpEditText.setError("Please check the format of your training max.");
        }


        //null error handling
        if (OHP.equals("")) {
            //technically don't need endline char first time...
            ohpEditText.setError(emptyOHPString);
            ohpErrorFlag = true;
        } else {
            if ((ohpDouble >= 1000 && lbs) || (ohpDouble >= 400 && !lbs)) {
                if (lbs)
                    ohpEditText.setError(thousandOHPString);
                if (!lbs)
                    ohpEditText.setError(thousandOHPStringKgs);
                ohpErrorFlag = true;
            }
            if (ohpDouble == 0) {
                ohpEditText.setError(zeroOHPString);
                ohpErrorFlag = true;
            }
        }

        if (ohpErrorFlag.equals(false))
            TabPrototype.ohpTM = OHP;
        //end OHP error handling


        String dead = deadEditText.getText().toString();

        double deadDouble = 0;
        try {
            deadDouble = nf.parse(dead).doubleValue();
        } catch (ParseException e) {
            if (dead.matches("^[0-9]+(.[0-9]{1,3})?$"))
                sendTrackerException("ParseException", dead);

            else
                deadEditText.setError("Please check the format of your training max.");
        }


        if (dead.equals("")) {
            //technically don't need endline char first time...
            deadEditText.setError(emptyDeadliftString);
            deadErrorFlag = true;
        } else {
            if ((deadDouble >= 1500 && lbs) || (deadDouble >= 700 && !lbs)) {
                if (lbs)
                    deadEditText.setError(thousandDeadliftString);
                if (!lbs)
                    deadEditText.setError(thousandDeadliftStringKgs);
                deadErrorFlag = true;
            }
            if (deadDouble == 0) {
                deadEditText.setError(zeroDeadliftString);
                deadErrorFlag = true;
            }
        }
        if (deadErrorFlag.equals(false))
            TabPrototype.deadTM = dead;

        TabPrototype.numberOfCycles = "5";

        if (!benchErrorFlag && !squatErrorFlag && !ohpErrorFlag && !deadErrorFlag) {


            if (lbs.equals(true)) {
                unit_mode = "Lbs";
//                Toast.makeText(getActivity(), "Displaying in lbs", Toast.LENGTH_SHORT).show();
                TabPrototype.unitMode = unit_mode;
            }
            if (lbs.equals(false)) {
                unit_mode = "Kgs";
//                Toast.makeText(getActivity(), "Displaying in kgs", Toast.LENGTH_SHORT).show();
                TabPrototype.unitMode = unit_mode; //TODO refactor
            }

            TabPrototype.origin = "second";
            //Instead of startActivity, allow swiping
            return true;

        }

        return false;
    }

        protected void sendTrackerException(String exceptionType, String value) {
            Tracker tracker = GoogleAnalytics.getInstance(getActivity()).getTracker("UA-55018534-1");
            tracker.send(MapBuilder
                    .createEvent("Exception",     // Event category (required)
                            exceptionType,  // Event action (required)
                            value,   // Event label
                            null)            // Event value
                    .build());



        }


    }

