package com.mantis.mantis_app.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    public String firstName;
    public String lastName;
    public String id;
    public String email;
    public String username;
    public List<Invitation> invitations;
    public List<HashMap<String, String>> followedTasks;

    // Singleton
    public static User currentUser;

    public static void parseUserData(JSONObject user){

        try {
            user = user.getJSONObject("user");
            // For each followed task
            JSONArray followedTasksJson = user.getJSONArray("followedTasks");
            List<HashMap<String, String>> tempFollowedTasks = new ArrayList<>();
            for (int i = 0; i < followedTasksJson.length(); i++) {
                JSONObject followedTask = followedTasksJson.getJSONObject(i);
                HashMap<String, String> task = new HashMap<>();
                task.put("taskID", followedTask.getString("taskId"));
                task.put("projectID", followedTask.getString("projectId"));
                tempFollowedTasks.add(task);
            }

            currentUser = new User(user, null, tempFollowedTasks);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private User(JSONObject user, List<Invitation> invitations, List<HashMap<String, String>> followedTasks){
        try {
            this.firstName = user.getString("firstName");
            this.lastName = user.getString("lastName");
            this.id = user.getString("_id");
            this.email = user.getString("email");
            this.username = user.getString("username");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.invitations = invitations;
        this.followedTasks = followedTasks;
    }

    public void unfollowTask(String taskID){
        for(int i = 0; i < currentUser.followedTasks.size(); i++){
            HashMap<String, String> currentTask = currentUser.followedTasks.get(i);
            if(currentTask.get("taskID").equals(taskID)){
                currentUser.followedTasks.remove(i);
                break;
            }
        }
    }

    public void followTask(String projectID, String taskID){
        HashMap<String, String> newTask = new HashMap<>();
        newTask.put("taskID", taskID);
        newTask.put("projectID", projectID);
        currentUser.followedTasks.add(newTask);
    }

    public boolean taskIsFollowed(String taskID){
        for (HashMap<String, String> task: currentUser.followedTasks)
            if(task.get("taskID").equals(taskID)) return true;
        return false;
    }

    public Invitation getInvitationByID(String ID){
        for (Invitation invitation: currentUser.invitations) if(invitation.id.equals(ID)) return invitation;
        return null;
    }

    public void setInvitations(List<Invitation> invitations) {
        currentUser.invitations = invitations;
    }
}
