package com.example.cba.weatherapp.data;

import org.json.JSONObject;

/**
 * Created by cba on 2017-05-08.
 */

public class Item implements JSONPopulator {

    private Condition condition;

    public Condition getCondition(){
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
