package com.serveic_provider.service_provider.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.serviceProvider.Rate;
import com.serveic_provider.service_provider.serviceProvider.User;

import java.util.List;


public class RateAdaptor extends BaseAdapter {

    Activity activity;
    List<User> users;
    List<Rate> rates;
    LayoutInflater inflater;


    TextView username;
    TextView profession;
    TextView city;
    RatingBar rateBar;
    TextView raterNum;
    TextView comment;
    TextView location;

    TextView profBtn;

    public RateAdaptor(Activity activity) {
        this.activity = activity;
    }

    public RateAdaptor(Activity activity, List<Rate> rates) {
        this.activity   = activity;
        this.rates      = rates;
        inflater        = activity.getLayoutInflater();

    }


    @Override
    public int getCount() {
        return rates.size();
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
            view = inflater.inflate(R.layout.rate_item, viewGroup, false);

            username = (TextView)view.findViewById(R.id.providerName_textView);
            profession = (TextView) view.findViewById(R.id.profession_textView);
            rateBar = (RatingBar) view.findViewById(R.id.rateRatingBar);
            comment = (TextView) view.findViewById(R.id.comment_textView);
            profBtn = (TextView)view.findViewById(R.id.profileBtn);

        }


        Rate rate = rates.get(i);

        if(rate.getRaterId()!=null);
        username.setText(rate.getRaterName());
        if(rate.getProfession()!=null)
        profession.setText(rate.getProfession());
        if(rate.getComment()!=null)
        comment.setText(rate.getComment());

        rateBar.setRating(rate.getRate());




        return view;

    }

    public void updateRecords(List<Rate> rates){
        this.rates = rates;

        notifyDataSetChanged();
    }
    public void setComntListener(final String uid){
        /*final Intent intent1 = new Intent(activity, ProfileActivity.class);
        profBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Bundle bundle1 = new Bundle();
                bundle1.putString("userId",uid);
                intent1.putExtras(bundle1);
               activity.startActivity(intent1);
            }
        });*/
    }


}
