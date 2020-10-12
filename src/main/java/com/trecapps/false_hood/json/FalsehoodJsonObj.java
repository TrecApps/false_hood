package com.trecapps.false_hood.json;

import org.json.JSONException;
import org.json.JSONObject;

public interface FalsehoodJsonObj
{
    JSONObject toJsonObject();

    void initializeFromJson(JSONObject obj) throws JSONException;
}
