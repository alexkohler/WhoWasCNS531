package com.kohlerbear.whowascnscalc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


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

    private String m_liftDate;
    private int m_cycle;
    private String m_liftType;
    private String m_frequency;
    private String m_accessoryName;
    private boolean m_usingLbs;




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

            //Take care of theming (Button colors)
            ColorManager colormanager = ColorManager.getInstance(getActivity());
            addSetButton.setBackgroundColor(colormanager.getPrimaryColor());
            addSetButton.setTextColor(colormanager.getTextColor());
            removeSetButton.setBackgroundColor(colormanager.getPrimaryColor());
            removeSetButton.setTextColor(colormanager.getTextColor());
            colormanager.clear();//not sure why I used singleton pattern here.. guess there was still some script kiddy in me back then

            final ArrayList<rowSet> rows = new ArrayList<rowSet>();
            rows.add(new rowSet(-1, -1, "Set 1: ", 1));
            accessoryListAdapter = new MyAdapter(getActivity(), rows);
            myListView.setAdapter(accessoryListAdapter);

            addSetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_numberSets < 20) {
                        m_numberSets++;
                        rows.add(new rowSet(-1, -1, "Set " + m_numberSets + ": ", m_numberSets));
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

            //Grab our arguments
            m_cycle = args.getInt("cycle");
	        m_frequency = args.getString("frequency");
            m_liftType = args.getString("liftType");
            m_liftDate = args.getString("date");
            m_accessoryName = args.getString("accessory");
            String mode = args.getString("mode");
            if (mode.equals("0"))
                m_usingLbs = false;
            else
                m_usingLbs = true;



            return rootView;
        }

    //Once this goes out of view, the viewpager handler will call this method and the data will be created or updated in the progress database
    public void persistData() {


        //This will start by simply making toast of what it should be inserting into the database
        String buffer = "";
        for (rowSet entry : accessoryListAdapter.getAccessoryEntries())
        {
            if (entry.getWeight() > 0) { //initialized with -1, we don't want these empty entries
                //buffer = buffer + entry.getSetString() + " " + entry.getWeight() + "x" + entry.getReps();
//                public LongTermEvent(String liftDate, String cycle, String liftType, String liftName, String frequency, double weight, int reps, boolean lbs)
                LongTermEvent accessoryEvent = new LongTermEvent(m_liftDate, String.valueOf(m_cycle), m_liftType, m_accessoryName, m_frequency, entry.getWeight(), entry.getReps(), m_usingLbs, entry.getSet());
                LongTermDataSQLHelper helper = new LongTermDataSQLHelper(getActivity());
                helper.addEvent(accessoryEvent);

            }
        }
        Toast.makeText(getActivity(), "Accessories added", Toast.LENGTH_SHORT).show();

    }

    //Create a small custom listview adapter
    public class MyAdapter extends ArrayAdapter<rowSet> {

        private final Context context;
        private ArrayList<rowSet> itemsArrayList;

        public MyAdapter(Context context, ArrayList<rowSet> itemsArrayList) {

            super(context, R.layout.row, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        public ArrayList<rowSet> getAccessoryEntries() {
            return itemsArrayList;
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
            setView.setText(itemsArrayList.get(position).getSetString());

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
        private String mrowSet_setString;
        private int mrowSet;

        public rowSet (double weight, int reps, String setString, int setNumb) {
            mrowSet_weight = weight;
            mrowSet_reps = reps;
            mrowSet_setString = setString;
            mrowSet = setNumb;
        }


        public double getWeight()
        {
            return mrowSet_weight;
        }

        public int getReps()
        {
            return mrowSet_reps;
        }

        public String getSetString()
        {
            return mrowSet_setString;
        }

        public int getSet() {return mrowSet;}

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
