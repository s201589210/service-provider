package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.serviceProvider.User;

import java.util.List;

import butterknife.BindView;


public class multSelcProvAdaptor extends BaseAdapter {

    Activity activity;
    List<User> users;
    LayoutInflater inflater;

    ImageView image;
    TextView username;
    TextView profession;
    TextView city;
    RatingBar rate;
    TextView raterNum;
    TextView company;
    TextView location;
    ImageView ivCheckBox;


    public multSelcProvAdaptor(Activity activity) {
        this.activity = activity;
    }

    public multSelcProvAdaptor(Activity activity, List<User> users) {
        this.activity   = activity;
        this.users      = users;

        inflater        = activity.getLayoutInflater();
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){

            view = inflater.inflate(R.layout.service_listview, viewGroup, false);



            username = (TextView)view.findViewById(R.id.provider_name_text_view);
            profession = (TextView) view.findViewById(R.id.profission_text_view);
            image = (ImageView) view.findViewById(R.id.image);
            city = (TextView) view.findViewById(R.id.city_text_view);
            rate = (RatingBar) view.findViewById(R.id.rating_bar);
            raterNum = (TextView) view.findViewById(R.id.number_of_reviews);
            company = (TextView) view.findViewById(R.id.company_name);
            location = (TextView) view.findViewById(R.id.location);

        }


        User user = users.get(i);

        username.setText(user.getName());
        profession.setText(user.getProfession());
        //image
        city.setText(user.getLocation());
        rate.setRating(user.getRate());
        raterNum.setText("23");
        company.setText("temp");
        location.setText("temp");


        if (user.isSelected())
            view.setBackgroundColor(Color.GRAY);
        else
            view.setBackgroundColor(Color.WHITE);

        return view;

    }

    public void updateRecords(List<User> users){
        this.users = users;

        notifyDataSetChanged();
    }


}
