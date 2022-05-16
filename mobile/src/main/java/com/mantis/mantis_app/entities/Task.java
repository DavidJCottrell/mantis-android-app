package com.mantis.mantis_app.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Task {

    public String title;
    public String id;
    public String type;
    public String taskKey;
    public String description;
    public String status;
    public String resolution;
    public String dateCreated;
    public String dateDue;
    public List<Comment> comments;
    public ArrayList<HashMap<String, String>> taskAssignees;
    public HashMap<String, String> reporter;

    public static List<Task> parseTasks(JSONArray taskData){
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < taskData.length(); i++) {
            try {
                JSONObject task = taskData.getJSONObject(i);
                tasks.add(new Task(task));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }

    private ArrayList<HashMap<String, String>> parseAssignees(JSONArray assigneeData){

        ArrayList<HashMap<String, String>> assignees = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < assigneeData.length(); i++) {
            try {
                JSONObject assigneeObject = assigneeData.getJSONObject(i);

                HashMap<String, String> assignee = new HashMap<>();

                assignee.put("userId", assigneeObject.getString("userId"));
                assignee.put("name", assigneeObject.getString("name"));

                assignees.add(assignee);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return assignees;
    }

    public Comment getCommentByID(String id){
        for (Comment comment: comments) if(comment.id.equals(id)) return comment;
        return null;
    }

    public JSONArray commentsToJSON(){
        JSONArray newComments = new JSONArray();

        // Convert Comment objects to formatted JSON objects
        for (Comment comment: comments) {
            JSONObject commentJson = new JSONObject();
            try {
                commentJson.put("authorName", comment.authorName);
                commentJson.put("authorId", comment.authorID);
                commentJson.put("content", comment.content);
                newComments.put(commentJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newComments;
    }

    public Task(JSONObject task){
        try {
            title = task.getString("title");
            id = task.getString("_id");
            type = task.getString("type");
            taskKey = task.getString("taskKey");
            description = task.getString("description");
            status = task.getString("status");
            resolution = task.getString("resolution");
            dateCreated = task.getString("dateCreated");
            dateDue = task.getString("dateDue");
            comments = Comment.parseComments(task.getJSONArray("comments"));
            taskAssignees = parseAssignees(task.getJSONArray("assignees"));

            JSONObject reporterObject = task.getJSONObject("reporter");
            reporter = new HashMap<>();
            reporter.put("userId", reporterObject.getString("userId"));
            reporter.put("name", reporterObject.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
