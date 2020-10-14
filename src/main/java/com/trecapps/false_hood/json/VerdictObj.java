package com.trecapps.false_hood.json;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

public class VerdictObj implements FalsehoodJsonObj
{
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    boolean approve;

    long userId;

    Date made;

    String explaination;

    String ipAddress;

    public VerdictObj() {
    }

    public VerdictObj(boolean approve, long userId, Date made, String explaination, String ipAddress) {
        this.approve = approve;
        this.userId = userId;
        this.made = made;
        this.explaination = explaination;
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(HttpServletRequest req)
    {
        if(req == null)
            return;

        ipAddress = "Basic Address: <" + req.getRemoteAddr() + ">;";

        for(String ipHeader: IP_HEADER_CANDIDATES)
        {
            String ip = req.getHeader(ipHeader);

            if(ip != null)
            {
                String adder = String.format(" %s: <%s>;", ipHeader, ip);
                ipAddress += adder;
            }
        }
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getMade() {
        return made;
    }

    public void setMade(Date made) {
        this.made = made;
    }

    public String getExplaination() {
        return explaination;
    }

    public void setExplaination(String explaination) {
        this.explaination = explaination;
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
