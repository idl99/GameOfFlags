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

/**
 * Class assigned single responsibility of maintaining a repository of Country.
 * Interacts with data file (countries.json) and reads data into memory.
 */
public class CountryRepository {

    private static CountryRepository instance; // Static field holding singleton instance
    private Context context; // Context used to gain access to resources
    private List<Country> countries;

    /**
     * Constructor
     * @param context - Context used to gain access to resources
     * @throws IOException
     * @throws JSONException
     */
    private CountryRepository(Context context) throws IOException, JSONException{
        this.context = context;
        this.countries = new ArrayList<>();
        load();
    }

    /**
     * Method which returns Singleton instance of CountryRepository
     * @param context
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static CountryRepository getInstance(Context context) throws IOException, JSONException{
        if(instance == null){
            instance = new CountryRepository(context);
        }
        return instance;
    }

    /**
     * Getter returning list of Countries
     * @return - List of Country objects
     */
    public List<Country> getCountries(){
        return countries;
    }

    /**
     * Getter returning list of all Countries' names
     * @return - List of String objects holding country names as values
     */
    public List<String> getAllCountryNames(){
        List<String> countryNames = new ArrayList<>();
        for(Country country: countries){
            countryNames.add(country.getName());
        }
        Collections.sort(countryNames);
        return countryNames;
    }

    /**
     * Getter returning randomly selected country
     * @return - returns randomly selected country
     */
    public Country getRandomCountry(){
        int randVal = new Random().nextInt(256);
        return countries.get(randVal);
    }

    /**
     * Getter returning image of Flag (Drawable resource) for a given Country
     * @param country - Country for which  the flag is needed
     * @return drawable object containing image of country obtained as parameter
     */
    public Drawable getFlagForCountry(Country country){
        int flagResourceId = context.getResources().getIdentifier(country.getShortCode().toLowerCase(),
                "drawable",context.getPackageName());
        return context.getResources().getDrawable(flagResourceId);
    }

    /**
     * Private method handling loading data from .json file onto in-application memory
     * @throws IOException
     * @throws JSONException
     */
    private void load() throws IOException, JSONException{

        // Getting json data onto input buffer
        InputStream is = context.getAssets().open("countries.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        // Processing input buffer to extract json
        String rawText = new String(buffer, "UTF-8");
        JSONObject json = new JSONObject(rawText);
        Iterator<String> keys = json.keys();
        while(keys.hasNext()){
            String key = keys.next();
            countries.add(new Country(key, json.getString(key)));
        }
    }

}
