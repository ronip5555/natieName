package com.nativename;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class bordersActivity extends AppCompatActivity {
    private RecyclerView list;
    List<country> rememberCtries;
    private countryDBHelper helper = new countryDBHelper(this);
    private ArrayList<country> countryAdapter = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    String urltoread = "https://restcountries.eu/rest/v2/alpha/";
    private countryAdapter mAdapter;
    country cn;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borders);
        String[] borders=getIntent().getStringArrayExtra("borders");
        list = (RecyclerView) findViewById(R.id.borderList);
        list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        helper.deleteAllCountries();
        mAdapter = new countryAdapter(this,helper.getAllCountries());
        list.setAdapter(mAdapter);
        for (int i = 0; i <borders.length ; i++) {
            new borderTask().execute(new String[]{borders[i]});
        }
    }

    public class borderTask extends AsyncTask<String, Void, country> {

        @Override
        protected country doInBackground(String... strings) {
            BufferedReader reader;
            StringBuilder builder = new StringBuilder();
            String cntrName,cntryNative;
            String cntryArea ;
            ArrayList<country> data = new ArrayList<>();
            country cntr;
            try {
                URL url1 = new URL(urltoread+strings[0]);
                HttpURLConnection httpconn = (HttpURLConnection)url1.openConnection();

                if (httpconn.getResponseCode()!= HttpURLConnection.HTTP_OK)
                {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                reader.close();
                JSONObject jobj=new JSONObject(builder.toString());
                cntrName=jobj.getString("name");
                cntryNative=jobj.getString("nativeName");
                cntryArea= jobj.getString("area");
                if(!(cntryArea == null )){
                    cntr=new country(cntrName,cntryNative,cntryArea);
                }else{
                    cntr=new country(cntrName,cntryNative,"");
                }
                return cntr;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(country cntry) {
            super.onPostExecute(cntry);
            flag=1;
            if(cntry!=null){
                mAdapter.clear();
                helper.insertCountry(cntry);
                mAdapter.AddAll(helper.getAllCountries());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
