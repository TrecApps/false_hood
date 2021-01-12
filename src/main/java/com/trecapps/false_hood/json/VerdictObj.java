package com.trecapps.false_hood.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class VerdictObj extends JsonMarker implements FalsehoodJsonObj
{
    public VerdictObj(boolean approve, long userId, Date made, String explaination, String ipAddress) {
        super(approve, userId, made, explaination, ipAddress);
    }

    public VerdictObj() {
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject ret = new JSONObject();

        ret.accumulate("Verdict", (approve) ? "approve": "reject");
        ret.accumulate("User", (Long)userId);
        ret.accumulate("IpAddress", ipAddress);
        ret.accumulate("Date", (Long)made.getTime());
        ret.accumulate("Explaination", explaination);

        return ret;
    }

    @Override
    public void initializeFromJson(JSONObject obj) throws JSONException {
        Object o = obj.get("Verdict");

        if(o instanceof String)
        {
            approve = "approve".equalsIgnoreCase((String)o);
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
        else
            throw new JSONException("'User' field needed to be a number castable to 'Long'");

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

        o = obj.get("Explaination");

        if(o instanceof String)
        {
            explaination = o.toString();
        }
        else throw new JSONException("'Explaination' field needed to be a String");
    }
}
