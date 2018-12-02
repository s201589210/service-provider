package com.serveic_provider.service_provider.adapters;


import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.word;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<word> {
    private int colorid;

    private static final String LOG_TAG = WordAdapter.class.getSimpleName();


    public WordAdapter(Activity context, ArrayList<word> words,int color) {
        super(context, 0, words);

        colorid=color;
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
    }



    @Override

    public View getView(int position,  View convertView,  ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        word currentword = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = listItemView.findViewById(R.id.name_text_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentword.toNames());
        // Find the TextView in the list_item.xml layout with the ID version_number
         TextView numberTextView = listItemView.findViewById(R.id.description);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        numberTextView.setText(currentword.todefult());

        TextView PriceTextView = listItemView.findViewById(R.id.ServicePrice);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        PriceTextView.setText(currentword.ToPrice());

        TextView FromTextView = listItemView.findViewById(R.id.from);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
       FromTextView.setText(currentword.getFromUser());

        ImageView imgeid = listItemView.findViewById(R.id.image);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        if(currentword.hasImage()){
            imgeid.setImageResource(currentword.getId());
            imgeid.setVisibility(View.VISIBLE);}
        else{
            imgeid.setVisibility(View.GONE);
        }

        View textcontainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(),colorid);
        textcontainer.setBackgroundColor(color);



        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }
}