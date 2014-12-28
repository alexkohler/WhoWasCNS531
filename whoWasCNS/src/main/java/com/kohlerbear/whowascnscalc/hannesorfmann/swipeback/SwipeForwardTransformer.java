package com.kohlerbear.whowascnscalc.hannesorfmann.swipeback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.swipeback.R;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.hannesdorfmann.swipeback.transformer.SwipeBackTransformer;
import com.hannesdorfmann.swipeback.util.MathUtils;
import com.kohlerbear.whowascnscalc.EventsDataSQLHelper;
import com.kohlerbear.whowascnscalc.deprecated.MainActivity;
import com.kohlerbear.whowascnscalc.SecondScreenPrototype;

public class SwipeForwardTransformer implements SwipeBackTransformer {

	protected View arrowTop;
	protected View arrowBottom;
	protected TextView textView;
	
	
	Context m_context; 
	
	public SwipeForwardTransformer(Context mainScreen)
	{
		m_context = mainScreen;
	}
	
	
	private void goToSecond()
	{
		int startingDateDay = MainActivity.dp.getDayOfMonth();
		int startingDateMonth = MainActivity.dp.getMonth();
		int startingDateYear = MainActivity.dp.getYear();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		cal.set(startingDateYear, startingDateMonth, startingDateDay);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", java.util.Locale.getDefault());
		Date myDate = cal.getTime();
		String formattedDate = dateFormat.format(myDate);
		//if this db does not have a database....
		EventsDataSQLHelper eventsData = new EventsDataSQLHelper(m_context);
		SQLiteDatabase db = eventsData.getWritableDatabase();
		Intent intent = new Intent(m_context, SecondScreenPrototype.class); //TODO just change this back to second if things go awry
		intent.putExtra("key", formattedDate );
		intent.putExtra("origin", "first");
		intent.putExtra("liftPattern", new String[]{"Bench", "Squat", "Rest", "OHP", "Deadlift", "Rest"});
		db.close();
		m_context.startActivity(intent);

	}
	

	@Override
	public void onSwipeBackViewCreated(SwipeBack swipeBack, Activity activity,
			final View swipeBackView) {

		arrowTop = swipeBackView.findViewById(R.id.arrowTop);
		arrowBottom = swipeBackView.findViewById(R.id.arrowBottom);
		textView = (TextView) swipeBackView.findViewById(R.id.text);

		onSwipeBackReseted(swipeBack, activity);

	}

	@Override
	public void onSwipeBackCompleted(SwipeBack swipeBack, Activity activity) {
//		activity.finish();
//		Intent i = new Intent();
//		activity.overridePendingTransition(R.anim.swipeback_slide_right_in,
//				R.anim.swipeback_slide_left_out);
		
		
		goToSecond();
		activity.overridePendingTransition(R.anim.swipeback_slide_right_in,
				R.anim.swipeback_slide_left_out);
		//activity.finish();
	}

	@SuppressLint("NewApi")
	@Override
	public void onSwipeBackReseted(SwipeBack swipeBack, Activity activity) {

		// Reset the values to the initial state
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			textView.setAlpha(0);
		} else {
			// Pre Honeycomb
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onSwiping(SwipeBack swipeBack, float openRatio, int pixelOffset) {

		// Do step by step animations
		float startAlphaAt = 0.5f;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Android 3 and above

			// Animate the textview
			textView.setAlpha(MathUtils.mapPoint(openRatio, startAlphaAt, 1f,
					0f, 1f));

		} else {
			// Pre Honeycomb (Android 2.x)

			// No good idea how to animate without nineold androids ( I will not
			// bring dependencies to nineold android into the library)
		}

	}

}

