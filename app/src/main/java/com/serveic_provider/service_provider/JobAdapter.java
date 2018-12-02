package com.serveic_provider.service_provider;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class JobAdapter extends ArrayAdapter<Job> {
    private int colorid;

    private static final String LOG_TAG = JobAdapter.class.getSimpleName();


    public JobAdapter(Activity context, ArrayList<Job> jobs,int color) {
        super(context, 0, jobs);

        colorid=color;
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and CheckBox, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
    }



    @Override

    public View getView(int position,  View convertView,  ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.job_list_layout, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Job currentword = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView titleTextView = listItemView.findViewById(R.id.textViewJob);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        titleTextView.setText(currentword.getTitle());


        // Return the whole list item layout (containing 2 TextViews and checkbox)
        // so that it can be shown in the ListView
        return listItemView;
    }
}