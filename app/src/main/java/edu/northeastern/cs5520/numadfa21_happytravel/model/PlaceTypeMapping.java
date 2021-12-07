package edu.northeastern.cs5520.numadfa21_happytravel.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * We want to show user friendly content to our customer for place types, however we cannot store
 * space as key in database. So we can use this class as a converter.
 */
public class PlaceTypeMapping {
    private static Map<String, String> nameToIdMap =
            new HashMap<String, String>() {
                {
                    put("Eat and Drink", "eat");
                    put("Humanities and Arts", "art");
                    put("Nature", "nature");
                    put("Sports", "sport");
                    put("Other", "other");
                }
            };
    private static Map<String, String> idToNameMap =
            new HashMap<String, String>() {
                {
                    put("eat", "Eat and Drink");
                    put("art", "Humanities and Arts");
                    put("nature", "Nature");
                    put("sport", "Sports");
                    put("other", "Other");
                }
            };

    public static Set<String> getAllNames() {
        return nameToIdMap.keySet();
    }

    public static Set<String> getAllIds() {
        return idToNameMap.keySet();
    }

    public static String getIdByName(String name) {
        return nameToIdMap.get(name);
    }

    public static String getNameById(String id) {
        return idToNameMap.get(id);
    }
}
