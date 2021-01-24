package com.trecapps.false_hood.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class EventObj extends JsonMarker implements FalsehoodJsonObj
{
    public EventObj() {
    }

    public EventObj(boolean approve, long userId, Date made, String explaination, String ipAddress) {
        super(approve, userId, made, explaination, ipAddress);
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject ret = new JSONObject();

        ret.accumulate("EventType", (approve) ? "Created": "Updated");
        ret.accumulate("User", (Long)userId);
        ret.accumulate("IpAddress", ipAddress);
        ret.accumulate("Date", (Long)made.getTime());
        ret.accumulate("Explaination", explaination);

        return ret;
    }

    @Override
    public void initializeFromJson(JSONObject obj) throws JSONException {
        Object o = obj.get("EventType");

        if(o instanceof String)
        {
            approve = "Created".equalsIgnoreCase((String)o);
        }
        else if(o instanceof Boolean)
        {
            approve = (Boolean)o;
        }
        else
            throw new JSONException("'Verdict' field needed to be a String or a boolean value");

        o = obj.get("User");

        if(o instanceof Long)
        {
            userId = (Long)o;
        }
        else if(o instanceof Integer)
        {
        	Integer oi = (Integer)o;
        	userId = oi.longValue();
        }
        else
            throw new JSONException("'User' field needed to be a number castable to 'Long', got type " + o.getClass());

        o = obj.get("IpAddress");

        if(o instanceof String)
        {
            ipAddress = (String)o;
        }
        else throw new JSONException("'IpAddress' field needed to be a String");

        o = obj.get("Date");

        if(o instanceof Number)
        {
            made = new Date(((Number) o).longValue());
        }
        else throw new JSONException("'Date' field needed to be a number castable to 'Long'");

        try {
        	o = obj.get("Explaination");
        }catch(JSONException e)
        {
        	o = null;
        }
        if(o instanceof String)
        {
            explaination = o.toString();
        }

    }
}
