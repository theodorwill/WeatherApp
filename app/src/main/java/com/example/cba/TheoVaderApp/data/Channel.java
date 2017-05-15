package com.example.cba.TheoVaderApp.data;

import org.json.JSONObject;

/**
 * Created by cba on 2017-05-08.
 */

public class Channel implements JSONPopulator {

    private Item item;
    private Units units;

    public Units getUnits() {
        return units;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void populate(JSONObject data) {

        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

    }
}
