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
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

public class ServiceAdapter extends ArrayAdapter<Service> {

    private int colorid;

    private static final String LOG_TAG = ProviderAdaptor.class.getSimpleName();

    public ServiceAdapter(Activity context, ArrayList<Service> words, int color) {
        super(context, 0, words);

        colorid=color;
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.service_listview, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Service currentword = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.provider_name_text_view);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentword.getProvider_id());

        TextView RnumberView = (TextView) listItemView.findViewById(R.id.number_of_reviews);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        RnumberView.setText(currentword.getRate()+"");

        ImageView imgeid = (ImageView) listItemView.findViewById(R.id.image);
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
