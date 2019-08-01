package com.nativename;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView list;
    private countryAdapter mAdapter;
    ArrayList<country> rememberCtries;
    private countryDBHelper helper = new countryDBHelper(this);
    private ArrayList<country> countryAdapter = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    Button btnEngDown,btnEngUp,btnAreaDown,btnAreaUp;
    String urltoread = "https://restcountries.eu/rest/v2/all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (RecyclerView) findViewById(R.id.my_recycler_view);
        btnEngDown=findViewById(R.id.englishascending);
        btnEngUp=findViewById(R.id.englishdescending);
        btnAreaDown=findViewById(R.id.areaascending);
        btnAreaUp=findViewById(R.id.areadescending);

        btnAreaUp.setOnClickListener(this);
        btnAreaDown.setOnClickListener(this);
        btnEngUp.setOnClickListener(this);
        btnEngDown.setOnClickListener(this);

        list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        mAdapter = new countryAdapter(this,helper.getAllCountries());
        list.setAdapter(mAdapter);
        new countryNameTask().execute(urltoread);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.areadescending:
                mAdapter.clear();
                helper.deleteAllCountries();
                helper.insertCountries(rememberCtries);
                mAdapter.AddAll(helper.getAllCountriesByAreaDesc());

                break;

            case R.id.areaascending:
                mAdapter.clear();
                helper.deleteAllCountries();
                helper.insertCountries(rememberCtries);
                mAdapter.AddAll(helper.getAllCountriesByAreaAsc());

                break;

            case R.id.englishdescending:
                mAdapter.clear();
                helper.deleteAllCountries();
                helper.insertCountries(rememberCtries);
                mAdapter.AddAll(helper.getAllCountries());

                break;

            case R.id.englishascending:
                mAdapter.clear();
                helper.deleteAllCountries();
                helper.insertCountries(rememberCtries);
                mAdapter.AddAll(helper.getAllCountriesAscending());

                break;
        }

    }

    class countryNameTask extends AsyncTask<String,Void, ArrayList<country>> {

        @Override
        protected ArrayList<country> doInBackground(String... strings) {
            BufferedReader reader;
            StringBuilder builder = new StringBuilder();
            String cntrName,cntryNative;
            String cntryArea ;
            ArrayList<country> data = new ArrayList<>();

            try {
                URL url1 = new URL(strings[0]);
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
                JSONArray jarr = new JSONArray(builder.toString());
                for (int i = 0; i <jarr.length() ; i++) {
                    JSONObject jobj=jarr.getJSONObject(i);
                    cntrName=jobj.getString("name");
                    cntryNative=jobj.getString("nativeName");
                    cntryArea= jobj.getString("area");
                    if(!(cntryArea == null )){
                        data.add(new country(cntrName,cntryNative,cntryArea));
                    }else{
                        data.add(new country(cntrName,cntryNative,""));
                    }
                }
                return data;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<country> countries) {
            super.onPostExecute(countries);
            if(countries!=null){
                mAdapter.clear();
                mAdapter.AddAll(countries);
                rememberCtries=countries;
            }
        }
    }
}
