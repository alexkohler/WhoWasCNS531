package com.kohlerbear.whowascnscalc;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class IndividualViewAccessoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mAcessoryName;
    private ArrayAdapter<String> listAdapter;
    private MyAdapter accessoryListAdapter;
    private int m_numberSets = 1;




        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_individual_view_accessory, container, false);
            Bundle args = getArguments();
            final ListView myListView = (ListView) rootView.findViewById(R.id.accessoryListView);
            Button addSetButton = (Button) rootView.findViewById(R.id.addSetButton);
            Button removeSetButton = (Button) rootView.findViewById(R.id.removeSetButton);


            final ArrayList<rowSet> rows = new ArrayList<rowSet>();
            rows.add(new rowSet(-1, -1, "Set 1: "));
            accessoryListAdapter = new MyAdapter(getActivity(), rows);
            myListView.setAdapter(accessoryListAdapter);

            addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_numberSets < 20) {
                        m_numberSets++;
                        rows.add(new rowSet(-1, -1, "Set " + m_numberSets + ": "));
                        myListView.setAdapter(accessoryListAdapter);
                    }
                }
            });

            removeSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_numberSets > 0) {
                        rows.remove(m_numberSets - 1);
                        m_numberSets--;
                        myListView.setAdapter(accessoryListAdapter);
                    }
                }
            });

            /*TextView t = (TextView) rootView.findViewById(R.id.accessoryName);
            String s = args.getString("accessory");
            t.setText(args.getString("accessory"));*/





            return rootView;
        }

    //Create a small custom listview adapter
    public class MyAdapter extends ArrayAdapter<rowSet> {

        private final Context context;
        private final ArrayList<rowSet> itemsArrayList;

        public MyAdapter(Context context, ArrayList<rowSet> itemsArrayList) {

            super(context, R.layout.row, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.accessory_listview_row, parent, false);

            // 3. Get the two text view from the rowView
            TextView setView = (TextView) rowView.findViewById(R.id.setText);
            EditText weightEditText = (EditText) rowView.findViewById(R.id.weightEditText);
            EditText repsEditText = (EditText) rowView.findViewById(R.id.repsEditText); //nothing needs set here

            //Add listeners for changes on our editTexts for sake of persisting data
            weightEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    itemsArrayList.get(position).setWeight(Double.valueOf(editable.toString()));

                }
            });

            repsEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    itemsArrayList.get(position).setReps(Integer.valueOf(editable.toString()));
                }
            });



//            repsView.setText(itemsArrayList.get(position));
            setView.setText(itemsArrayList.get(position).getSet());

            if (itemsArrayList.get(position).getWeight() > 0) {
                weightEditText.setText(String.valueOf(itemsArrayList.get(position).getWeight()));
            }

            if (itemsArrayList.get(position).getReps() > 0) {
                repsEditText.setText(String.valueOf(itemsArrayList.get(position).getReps()));
            }

            return rowView;
        }
    }
    public class rowSet {
        private double mrowSet_weight;
        private int mrowSet_reps;
        private String mrowSet_set;

        public rowSet (double weight, int reps, String setString) {
            mrowSet_weight = weight;
            mrowSet_reps = reps;
            mrowSet_set = setString;
        }


        public double getWeight()
        {
            return mrowSet_weight;
        }

        public int getReps()
        {
            return mrowSet_reps;
        }

        public String getSet()
        {
            return mrowSet_set;
        }

        public void setWeight(double weight)
        {
            mrowSet_weight = weight;
        }

        public void setReps(int reps)
        {
            mrowSet_reps = reps;
        }


    }


}
