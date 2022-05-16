package com.mantis.mantis_app.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Requirement {

    public String type;
    public String index;
    public String systemName;
    public String trigger;
    public String fullText;
    public String feature;
    public String unwantedTrigger;
    List<String> preconditions = new ArrayList<>();
    List<String> systemResponses = new ArrayList<>();
    List<String> order = new ArrayList<>();

    public static List<Requirement> parseRequirements(JSONArray requirementData){
        List<Requirement> requirements = new ArrayList<>();

        for (int i = 0; i < requirementData.length(); i++) {
            try {
                JSONObject requirement = requirementData.getJSONObject(i);
                requirements.add(new Requirement(requirement));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return requirements;
    }

    public Requirement(JSONObject requirement){
        try {
            index = requirement.getString("index");
            type = requirement.getString("type");
            systemName = requirement.getString("systemName");
            fullText = requirement.getString("fullText");

            // get system response (can assume it has at least 1)
            // get order (can assume it has this)
            // get system pre-conditions (may or may not have these)

            if(requirement.has("feature")) feature = requirement.getString("feature");
            if(requirement.has("unwantedTrigger")) unwantedTrigger = requirement.getString("unwantedTrigger");
            if(requirement.has("trigger")) trigger = requirement.getString("trigger");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
