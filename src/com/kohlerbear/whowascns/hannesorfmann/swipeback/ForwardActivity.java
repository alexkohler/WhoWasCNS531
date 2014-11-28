package com.kohlerbear.whowascns.hannesorfmann.swipeback;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.hannesdorfmann.swipeback.SwipeBack.Type;
import com.kohlerbear.whowascnscalc.R;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ForwardActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_foward);

	
		// Init the swipe back mechanism
/*		SwipeBack.attach(this, Position.RIGHT)
		.setContentView(R.layout.activity_foward)
		.setSwipeBackView(R.layout.swipeback_forward)
		.setSwipeBackTransformer(new SwipeForwardTransformer());*/
		// Init the swipe back mechanism
		/*SwipeBack
		.attach(this, Type.BEHIND, Position.BOTTOM,
				SwipeBack.DRAG_WINDOW)
				.setContentView(R.layout.activity_bottom)
				.setSwipeBackView(R.layout.swipeback_bottom)
				.setSwipeBackTransformer(new SwipeForwardTransformer());*/

		
	}


}
