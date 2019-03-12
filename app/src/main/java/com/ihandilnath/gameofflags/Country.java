package com.ihandilnath.gameofflags;

import java.io.Serializable;

/**
 * Class which models and represents the real world entity "Country".
 * Implement Serializable marker interface.
 * Implements Comparable interface to compare two Countries, used to sort list of countries by country name
 */
public class Country implements Serializable, Comparable<Country> {

    private String mShortCode;
    private String mName;

    /**
     * Constructor
     * @param shortCode
     * @param name
     */
    public Country(String shortCode, String name) {
        this.mShortCode = shortCode;
        this.mName = name;
    }

    /**
     * Method overriden from Object class to check if two Country objects are equal on the basis
     * of equality in their country names.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || this.mName.equalsIgnoreCase(((Country)obj).getName());
    }

    /**
     * Implementation of abstract method defined in the Comparable interface, compares two Country
     * objects on the basis of their country name in natural alphabetical order.
     * @param country
     * @return
     */
    @Override
    public int compareTo(Country country) {
        return this.getName().compareTo(country.getName());
    }


    /**
     * Static, utility method used to get string array of country names, when Country object are given as parameters
     * @param countries
     * @return
     */
    public static String[] getCountryNamesAsArray(Country[] countries){
        String[] countryNames = new String[countries.length];
        for(int i=0; i<countries.length; i++){
            countryNames[i] =  countries[i].getName();
        }
        return countryNames;
    }

    /**
     * Getter for country shortcode
     * @return
     */
    public String getShortCode() {
        return mShortCode;
    }

    /**
     * Setter for country shortcode
     * @param shortCode
     */
    public void setShortCode(String shortCode) {
        this.mShortCode = shortCode;
    }

    /**
     * Getter for country name
     * @return
     */
    public String getName() {
        return mName;
    }

    /**
     * Setter for country name
     * @param name
     */
    public void setName(String name) {
        this.mName = name;
    }

}
