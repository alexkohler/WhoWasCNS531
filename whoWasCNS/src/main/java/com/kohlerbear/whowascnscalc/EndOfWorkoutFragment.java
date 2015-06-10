package com.kohlerbear.whowascnscalc;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class EndOfWorkoutFragment extends Fragment {


    public EndOfWorkoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_end_of_workout, container, false);

        Button finishWorkoutButton = (Button) rootView.findViewById(R.id.finishWorkoutButton);
        ColorManager cm = ColorManager.getInstance(getActivity());
        // Inflate the layout for this fragment
        finishWorkoutButton.setBackgroundColor(cm.getPrimaryColor());
        finishWorkoutButton.setTextColor(cm.getTextColor());
        return rootView;
    }


}
