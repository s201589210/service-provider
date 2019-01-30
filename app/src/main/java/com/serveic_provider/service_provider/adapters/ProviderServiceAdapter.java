package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.serviceProvider.Service;

import java.util.ArrayList;

public class ProviderServiceAdapter extends ArrayAdapter<Service> {
    Context context;
    public ProviderServiceAdapter(Activity context, ArrayList<Service> serviceArrayList) {
        super(context,0, serviceArrayList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_pending_service, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        Service currentService = (Service) getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.item_job);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentService.getJob());
        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView numberTextView = (TextView) listItemView.findViewById(R.id.item_provider_name);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        numberTextView.setText(currentService.getProvider_id());

        TextView PriceTextView = (TextView) listItemView.findViewById(R.id.item_date);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        PriceTextView.setText(currentService.getDate());

        TextView FromTextView = (TextView) listItemView.findViewById(R.id.item_location);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        FromTextView.setText(currentService.getLocation());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;

    }
}
