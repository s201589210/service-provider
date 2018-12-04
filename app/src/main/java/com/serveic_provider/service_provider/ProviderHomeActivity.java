package com.serveic_provider.service_provider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.serveic_provider.service_provider.R;
import com.serveic_provider.service_provider.adapters.WordAdapter;
import com.serveic_provider.service_provider.word;

import java.util.ArrayList;

public class ProviderHomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_homepage);

        setTitle("Service Provider");

        // Create a list of words
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("description", "name","11" ,"ETGGRG"));
        words.add(new word("description", "name","11" ,"ETGGRG"));
        words.add(new word("description", "name","11" ,"ETGGRG"));
        words.add(new word("description", "name","11" ,"ETGGRG"));
        words.add(new word("description", "name","11" ,"ETGGRG"));









        final ArrayList<word> words2 = new ArrayList<word>();
        words2.add(new word("description", "name","11" ,"ETGGRG"));
        words2.add(new word("description", "name","11" ,"ETGGRG"));
        words2.add(new word("description", "name","11" ,"ETGGRG"));
        words2.add(new word("description", "name","11" ,"ETGGRG"));
        words2.add(new word("description", "name","11" ,"ETGGRG"));
        words2.add(new word("description", "name","11" ,"ETGGRG"));







        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(this, words, R.color.colorPrimary);
        WordAdapter adapter2 = new WordAdapter(this, words2, R.color.colorPrimary);
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);
        ListView listView2 = (ListView) findViewById(R.id.list2);
        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word Word = words.get(position);

            }
        });

    }
    // Not allowing back button
    @Override
    public void onBackPressed() {

    }

    public void goToMyServicesPage(View view) {
        startActivity(new Intent(this,MyServicesActivity.class));
    }
}