package com.nativename;

public class country {
    String countryName,countryNativeName;
    String countrySize;

    public country(String countryName, String countryNativeName, String countrysize) {
        this.countryName = countryName;
        this.countryNativeName = countryNativeName;
        this.countrySize = countrysize;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNativeName() {
        return countryNativeName;
    }

    public void setCountryNativeName(String countryNativeName) {
        this.countryNativeName = countryNativeName;
    }

    public String getCountrySize() {
        return countrySize;
    }

    public void setCountrySize(String countrySize) {
        this.countrySize = countrySize;
    }
}
