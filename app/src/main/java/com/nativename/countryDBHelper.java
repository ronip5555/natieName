package com.nativename;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class countryDBHelper extends SQLiteOpenHelper {
    private String author,url,description,title;
    private static final String COUNTRY_TABLE_NAME = "news";
    private static final String COUNTRY_NAME = "countryname";
    private static final String COUNTRY_NATIVE_NAME = "countrynativename";
    private static final String COUNTRY_AREA = "countryarea";

    public countryDBHelper(Context context) {
        super(context, "MyCountryList.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s ( %s TEXT, %s TEXT, %s TEXT )",COUNTRY_TABLE_NAME,COUNTRY_NAME
                ,COUNTRY_NATIVE_NAME,COUNTRY_AREA);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertCountry(country countryItem){
        ContentValues values=new ContentValues();
        values.put(COUNTRY_NAME,countryItem.getCountryName());
        values.put(COUNTRY_NATIVE_NAME,countryItem.getCountryNativeName());
        values.put(COUNTRY_AREA,countryItem.getCountrySize());
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.insert(COUNTRY_TABLE_NAME,null,values);
        // second param is the name of the column in the table to insert a null value and not a real value (stringColumnHack)
        db.close();
    }

    public void insertCountries ( List<country> countriesList){
        SQLiteDatabase db=getWritableDatabase();
        for (int i = 0; i <countriesList.size() ; i++) {
            ContentValues values=new ContentValues();
            values.put(COUNTRY_NAME,countriesList.get(i).getCountryName());
            values.put(COUNTRY_NATIVE_NAME,countriesList.get(i).getCountryNativeName());
            values.put(COUNTRY_AREA,countriesList.get(i).getCountrySize());

            db.insert(COUNTRY_TABLE_NAME,null,values);
        }
        db.close();
    }

    public ArrayList<country> getAllCountriesAscending(){
        ArrayList <country> countriesList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(COUNTRY_TABLE_NAME, null, null, null, null, null, COUNTRY_NAME +" ASC");//the order of the list is names from a-z
        while(c.moveToNext()){
            String countryName = c.getString( c.getColumnIndex(COUNTRY_NAME));
            String countryNative = c.getString( c.getColumnIndex(COUNTRY_NATIVE_NAME));
            String countrySize = c.getString( c.getColumnIndex(COUNTRY_AREA));

            countriesList.add(new country(countryName,countryNative,countrySize));
        }
        db.close();
        return countriesList;
    }

    public ArrayList<country> getAllCountries(){
        ArrayList <country> countriesList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(COUNTRY_TABLE_NAME, null, null, null, null, null, COUNTRY_NAME +" DESC");//the order of the list is names from a-z
        while(c.moveToNext()){
            String countryName = c.getString( c.getColumnIndex(COUNTRY_NAME));
            String countryNative = c.getString( c.getColumnIndex(COUNTRY_NATIVE_NAME));
            String countrySize = c.getString( c.getColumnIndex(COUNTRY_AREA));

            countriesList.add(new country(countryName,countryNative,countrySize));
        }
        db.close();
        return countriesList;
    }

    public ArrayList<country> getAllCountriesByAreaDesc(){
        ArrayList <country> countriesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(COUNTRY_TABLE_NAME, null, null, null, null, null, COUNTRY_AREA +" DESC");//the order of the list is names from a-z
        while(c.moveToNext()){
            String countryName = c.getString( c.getColumnIndex(COUNTRY_NAME));
            String countryNative = c.getString( c.getColumnIndex(COUNTRY_NATIVE_NAME));
            String countrySize = c.getString( c.getColumnIndex(COUNTRY_AREA));
            countriesList.add(new country(countryName,countryNative,countrySize));
        }
        db.close();
        return countriesList;
    }

    public ArrayList<country> getAllCountriesByAreaAsc(){
        ArrayList <country> countriesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(COUNTRY_TABLE_NAME, null, null, null, null, null, COUNTRY_AREA +" ASC");//the order of the list is names from a-z
        while(c.moveToNext()){
            String countryName = c.getString( c.getColumnIndex(COUNTRY_NAME));
            String countryNative = c.getString( c.getColumnIndex(COUNTRY_NATIVE_NAME));
            String countrySize = c.getString( c.getColumnIndex(COUNTRY_AREA));

            countriesList.add(new country(countryName,countryNative,countrySize));
        }
        db.close();
        return countriesList;
    }

    public void deleteAllCountries(){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(COUNTRY_TABLE_NAME,null,null);
        db.close();
    }
}
