package com.ihandilnath.gameofflags;

import java.io.Serializable;

public class Country implements Serializable {

    private String shortCode;
    private String name;

    public Country(String shortCode, String name) {
        this.shortCode = shortCode;
        this.name = name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || this.name.equalsIgnoreCase(((Country)obj).getName());
    }



}