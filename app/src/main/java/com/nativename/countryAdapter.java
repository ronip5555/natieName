package com.nativename;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class countryAdapter extends RecyclerView.Adapter<countryAdapter.countryHolder> {
    private countryDBHelper helper;
    Context context;
    ArrayList<country> data = new ArrayList<>();
    private countryAdapter adapter;

    public countryAdapter(Context context, ArrayList<country> data) {
        this.context = context;
        if (data != null)
            this.data = data;
        helper = new countryDBHelper(context);
    }

    public countryAdapter(Context context) {
        this.context = context;
        helper = new countryDBHelper(context);
    }

    @Override
    public countryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customlistcountry, parent, false);

        return new countryHolder(v);
    }

    public void AddAll(ArrayList<country> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }


    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(countryHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class countryHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvcntName,tvcntNative;
        country Item;

        public countryHolder(View itemView) {
            super(itemView);
            tvcntName = (TextView) itemView.findViewById(R.id.tv_country_name);
            tvcntNative = (TextView) itemView.findViewById(R.id.tv_native_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            new getnearbyTask().execute(getPosition());
        }

        @Override
        public boolean onLongClick(View v) {

            return true;
        }

        public void bind(country Item) {
            this.Item = Item;
            tvcntName.setText(this.Item.getCountryName());
            tvcntNative.setText(this.Item.getCountryNativeName());
        }
    }

    public class getnearbyTask extends AsyncTask<Integer, Void, String[]> {

        @Override
        protected String[] doInBackground(Integer... params) {
            final String BASE_URL = "https://restcountries.eu/rest/v2/all";
            String cntrylist="";
            int pos = params[0];
            HttpURLConnection connection;
            BufferedReader reader;
            StringBuilder builder = new StringBuilder();
            adapter = new countryAdapter(context, data);
            JSONArray borderarr=null;
            try {
                URL url = new URL(BASE_URL);
                connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                reader.close();
                JSONArray jarr = new JSONArray(builder.toString());
                JSONObject jobj=jarr.getJSONObject(pos);
                borderarr=jobj.getJSONArray("borders");
                String[] totalresults=new String[borderarr.length()];
                for (int j = 0; j <borderarr.length() ; j++) {
                    totalresults[j]=borderarr.getString(j);
                    cntrylist+=totalresults[j]+"\n";
                }
                return totalresults;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] str) {
            super.onPostExecute(str);
            Intent in = new Intent(context,bordersActivity.class);
            in.putExtra("borders",str);
            context.startActivity(in);
        }
    }
}
