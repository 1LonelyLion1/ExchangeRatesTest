package com.example.exchangerates;

import android.content.Context;
//import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String NAME = "Name";
    private static final String VALUE = "Value";
    private static final String URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    public ArrayList<HashMap<String, String>> rateList;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.listView);
        try {
            downloadAndShowJson();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            

           // HashMap<String, String> itemHashMap =
             //       (HashMap<String, String>) parent.getItemAtPosition(position);
            //     String descriptionItem = itemHashMap.get(VALUE);
            //        String NameItem = itemHashMap.get(NAME);
           // Intent startConverterIntent = new Intent(MainActivity.this, Converter.class);
            //startConverterIntent.putExtra(VALUE,descriptionItem);
            //startConverterIntent.putExtra(NAME,NameItem);
            //startActivity(startConverterIntent);

        }
    };


    private boolean checkInternetConnection() {
        // Get Connectivity Manager
        ConnectivityManager connManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Details about the currently active default data network
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            Toast.makeText(this, "No default network is currently active", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isConnected()) {
            Toast.makeText(this, "Network is not connected", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!networkInfo.isAvailable()) {
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(this, "Network OK", Toast.LENGTH_LONG).show();
        return true;
    }


    public void downloadAndShowJson() throws ExecutionException, InterruptedException {
        boolean networkOK = this.checkInternetConnection();
        if (!networkOK) {
            return;
        }


        DownloadJsonTask task = new DownloadJsonTask();

        task.execute(URL);

        rateList = task.get();
        SimpleAdapter adapter = new SimpleAdapter(this, rateList,
                R.layout.list_rates, new String[]{NAME, VALUE},
                    new int[]{R.id.textview_description, R.id.textview_title});

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(itemClickListener);
    }

}