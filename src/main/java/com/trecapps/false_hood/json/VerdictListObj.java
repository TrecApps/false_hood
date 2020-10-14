package com.trecapps.false_hood.json;

import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

public class VerdictListObj implements FalsehoodJsonObj
{
    List<VerdictObj> verdicts;

    List<EventObj> events;

    long approversAvailable;

    public VerdictListObj(@NotNull List<VerdictObj> verdicts, @NotNull List<EventObj> events,long approversAvailable) {
        this.verdicts = verdicts;
        this.approversAvailable = approversAvailable;
        this.events = events;
    }

    public boolean isApproved()
    {
        if(verdicts.size() < Math.min(5, approversAvailable))
            return false;

        int totalCount = 0;
        int approveCount = 0;

        for(VerdictObj verdict: verdicts)
        {
            totalCount++;
            if(verdict.isApprove())
                approveCount++;
        }

        // Avoid divide by 0
        if(totalCount == 0)
            return false;

        return (float)approveCount / (float)totalCount > 0.66f;
    }

    public boolean isRejected()
    {
        if(verdicts.size() < Math.min(5, approversAvailable))
            return false;

        int totalCount = 0;
        int approveCount = 0;

        for(VerdictObj verdict: verdicts)
        {
            totalCount++;
            if(!verdict.isApprove())
                approveCount++;
        }

        // Avoid divide by 0
        if(totalCount == 0)
            return false;

        return (float)approveCount / (float)totalCount > 0.66f;
    }

    public List<EventObj> getEvents() {
        return events;
    }

    public void setEvents(List<EventObj> events) {
        this.events = events;
    }

    public List<VerdictObj> getVerdicts() {
        return verdicts;
    }

    public void setVerdicts(List<VerdictObj> verdicts) {
        this.verdicts = verdicts;
    }

    public long getApproversAvailable() {
        return approversAvailable;
    }

    public void setApproversAvailable(long approversAvailable) {
        this.approversAvailable = approversAvailable;
    }

    public VerdictListObj()
    {
        verdicts = new LinkedList<>();
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject ret = new JSONObject();

        ret.put("Approvers-Available", (Long)approversAvailable);

        JSONArray jList = new JSONArray();

        for(VerdictObj verdict: verdicts)
        {
            jList.put(verdict.toJsonObject());
        }

        ret.put("Verdicts", jList);

        jList = new JSONArray();

        for(EventObj verdict: events)
        {
            jList.put(verdict.toJsonObject());
        }

        ret.put("Events", jList);

        return ret;
    }

    @Override
    public void initializeFromJson(JSONObject obj) throws JSONException
    {
        if(verdicts == null)
            verdicts = new LinkedList<>();
        else
            verdicts.clear();


        Object o = obj.get("Approvers-Available");

        if(o instanceof Number)
        {
            approversAvailable = ((Number) o).longValue();
        }
        else throw new JSONException("'Approvers-Available' field needed to be a number castable to 'Long'");

        o = obj.get("Verdicts");

        if(o instanceof JSONArray)
        {
            JSONArray arr = (JSONArray)o;

            for(Object verdictObj: arr)
            {
                if(verdictObj instanceof JSONObject)
                {
                    VerdictObj newVerdict = new VerdictObj();
                    newVerdict.initializeFromJson((JSONObject)verdictObj);

                    verdicts.add(newVerdict);
                }
                else throw new JSONException("Needed JSON object in Verdicts array element");
            }
        }
        else throw new JSONException("Needed JSON Array in Verdicts field");

        o = obj.get("Events");

        if(o instanceof JSONArray)
        {
            JSONArray arr = (JSONArray)o;

            for(Object eventObj: arr)
            {
                if(eventObj instanceof JSONObject)
                {
                    EventObj newEvent = new EventObj();
                    newEvent.initializeFromJson((JSONObject)eventObj);

                    events.add(newEvent);
                }
                else throw new JSONException("Needed JSON object in Verdicts array element");
            }
        }
        else throw new JSONException("Needed JSON Array in Verdicts field");
    }
}
