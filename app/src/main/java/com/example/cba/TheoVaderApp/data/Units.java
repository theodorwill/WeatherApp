package com.example.cba.TheoVaderApp.data;


import org.json.JSONObject;

/**
 * Created by cba on 2017-05-08.
 */

public class Units implements JSONPopulator {

    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
