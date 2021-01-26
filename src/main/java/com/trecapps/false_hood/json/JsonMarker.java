package com.trecapps.false_hood.json;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

public class JsonMarker
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

    int approve;

    long userId;

    Date made;

    String explaination;

    String ipAddress;

    public JsonMarker() {
    }

    public JsonMarker(int approve, long userId, Date made, String explaination, String ipAddress) {
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

    public int isApprove() {
        return approve;
    }

    public void setApprove(int approve) {
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
}
