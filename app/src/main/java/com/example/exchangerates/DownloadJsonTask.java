package com.example.exchangerates;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import javax.net.ssl.HttpsURLConnection;

public class DownloadJsonTask extends AsyncTask<String, Void, ArrayList> {


    private static final String NAME = "Name";
    private static final String VALUE = "Value";


    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(String... params) {

        String textUrl = params[0];

        InputStream in = null;
        BufferedReader br = null;

        try {
            URL url = new URL(textUrl);
            HttpsURLConnection httpConn = (HttpsURLConnection) url.openConnection();

            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            int resCode = httpConn.getResponseCode();

            if (resCode == HttpsURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                br = new BufferedReader(new InputStreamReader(in));

                StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }

                ArrayList<HashMap<String, String>> rateList = new ArrayList<>();
                HashMap<String,String> hashMap;

                Rates result = new Gson().fromJson(sb.toString(), Rates.class);

                for(Map.Entry<String,Map<String,String>> entry : result.Valute.entrySet()){

                    hashMap = new HashMap<>();
                    hashMap.put(NAME, entry.getValue().get("Value"));
                    hashMap.put(VALUE, entry.getValue().get("Name"));
                    rateList.add(hashMap);
                }

                return rateList;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(br);
        }
        return null;
    }


    @Override
    protected void onPostExecute(ArrayList result ) {
        if(result != null){

        }
        else{
            Log.e("MyMessage", "Failed to fetch data!");
        }
    }
}
