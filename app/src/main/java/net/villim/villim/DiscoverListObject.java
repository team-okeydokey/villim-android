package net.villim.villim;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by seongmin on 5/26/17.
 */

public class DiscoverListObject extends Object {

    public HashMap<String, String> data;

    public DiscoverListObject(JSONObject jsonObject) {

        data = new HashMap<>();

        Iterator<String> iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String value = jsonObject.getString(key);
                data.put(key, value);
            } catch (JSONException e) {
                // Something went wrong!
            }
        }
    }

}
