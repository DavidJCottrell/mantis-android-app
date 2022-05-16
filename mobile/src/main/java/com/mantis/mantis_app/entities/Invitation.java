package com.mantis.mantis_app.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Invitation {

    public HashMap<String, String> invitee = new HashMap<>();
    public HashMap<String, String> inviter = new HashMap<>();
    public HashMap<String, String> project = new HashMap<>();
    public String role;
    public String id;

    public static List<Invitation> parseInvitationsData(JSONObject invitationData){
        JSONArray invitations = null;
        List<Invitation> allInvitations = new ArrayList<>();
        try {
            invitations = invitationData.getJSONArray("invitations");
            for (int i = 0; i < invitations.length(); i++) {
                JSONObject invitation = invitations.getJSONObject(i);
                allInvitations.add(new Invitation(invitation));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allInvitations;
    }

    public Invitation(JSONObject invitation){
        try {
            JSONObject inviteeObject = invitation.getJSONObject("invitee");
            JSONObject inviterObject = invitation.getJSONObject("inviter");
            JSONObject projectObject = invitation.getJSONObject("project");

            invitee.put("userId", inviteeObject.getString("userId"));
            invitee.put("name", inviteeObject.getString("name"));
            invitee.put("username", inviteeObject.getString("username"));

            inviter.put("userId", inviterObject.getString("userId"));
            inviter.put("name", inviterObject.getString("name"));

            project.put("title", projectObject.getString("title"));
            project.put("projectId", projectObject.getString("projectId"));

            role = invitation.getString("role");
            id = invitation.getString("_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
