package com.larditrans.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunny on 22.12.17
 */
public class Errors {
    Map<String, String> errors = new HashMap<>();

    public int size() {
        return errors.size();
    }

    public void put(String key, String value) {
        errors.put(key, value);
    }

    public Map<String,String> getErrors() {
        return errors;
    }
//    public String get(String key) {
//
//    }


    @Override
    public String toString() {
        return "Errors{" +
                "errors=" + errors +
                '}';
    }
}
