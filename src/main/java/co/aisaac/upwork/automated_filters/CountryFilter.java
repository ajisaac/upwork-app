package co.aisaac.upwork.automated_filters;

import java.util.ArrayList;
import java.util.List;

public class CountryFilter {
    private static final List<String> COUNTRIES = new ArrayList<>();

    static {
        COUNTRIES.add("USA");
        COUNTRIES.add("United States");
        COUNTRIES.add("");
    }

    public boolean contains(String country) {
        for (String s : COUNTRIES) {
            if (country.trim().equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
}
