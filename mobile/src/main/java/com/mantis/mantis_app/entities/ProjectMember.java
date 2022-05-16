package com.mantis.mantis_app.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectMember {
    String name;
    String userId;
    String username;
    String role;

    public static List<ProjectMember> parseProjectMember(JSONArray projectMemberData){
        List<ProjectMember> projectMembers = new ArrayList<>();
        for(int i = 0; i < projectMemberData.length(); i++){
            JSONObject user = null;
            try {
                user = (JSONObject) projectMemberData.get(i);
                projectMembers.add(new ProjectMember(
                        user.getString("name"),
                        user.getString("userId"),
                        user.getString("username"),
                        user.getString("role"))
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return projectMembers;
    }

    public ProjectMember(String name, String userId, String username, String role){
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}
