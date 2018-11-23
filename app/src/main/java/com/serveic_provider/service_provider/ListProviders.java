package com.serveic_provider.service_provider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ListProviders extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_providers);







        // Create a list of words
        final ArrayList<Provider> providers = new ArrayList<Provider>();
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall",R.drawable.ic_launcher_background));
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall",R.drawable.ic_launcher_background));
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall",R.drawable.ic_launcher_background));
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall"));
        providers.add(new Provider("Hussain", "Cooker","11" ,"company name","DHAHRAN","KFUPM mall",R.drawable.ic_launcher_background));

















        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        ProviderAdapter adapter = new ProviderAdapter(this, providers, R.color.colorPrimary);
        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // activity_numbers.xml layout file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Provider p = providers.get(position);

            }
        });
        onButtonClickListener();

    }

    private static Button btn;


    public void onButtonClickListener() {
        btn = (Button)findViewById(R.id.slect_button);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(ListProviders.this);
                        a_builder.setMessage("intent");

                        AlertDialog alert = a_builder.create();
                        alert.setTitle("intent here");
                        alert.show();
                    }
                }
        );
    }




}