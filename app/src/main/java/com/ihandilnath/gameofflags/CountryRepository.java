package com.ihandilnath.gameofflags;

import android.content.Context;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CountryRepository {

    private static CountryRepository instance;
    private Context context;
    private List<Country> countries;

    private CountryRepository(Context context) throws IOException, JSONException{
        this.context = context;
        this.countries = new ArrayList<>();
        load();
    }

    private void load() throws IOException, JSONException{
        // Getting json data onto input buffer
        InputStream is = context.getAssets().open("countries.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        // Processing input buffer to extract json data
        String rawText = new String(buffer, "UTF-8");
        JSONObject json = new JSONObject(rawText);
        Iterator<String> keys = json.keys();
        while(keys.hasNext()){
            String key = keys.next();
            countries.add(new Country(key, json.getString(key)));
        }
    }

    public static CountryRepository getInstance(Context context) throws IOException, JSONException{
        if(instance == null){
            instance = new CountryRepository(context);
        }
        return instance;
    }

    public List<Country> getCountries(){
        return countries;
    }

    public List<String> getAllCountryNames(){
        List<String> countryNames = new ArrayList<>();
        for(Country country: countries){
            countryNames.add(country.getName());
        }
        Collections.sort(countryNames);
        return countryNames;
    }

    public Country getRandomCountry(){
        int randVal = new Random().nextInt(256);
        return countries.get(randVal);

    }

    public Drawable getFlagForCountry(Country country){
        int flagResourceId = context.getResources().getIdentifier(country.getShortCode().toLowerCase(),
                "drawable",context.getPackageName());
        return context.getResources().getDrawable(flagResourceId);
    }


}
