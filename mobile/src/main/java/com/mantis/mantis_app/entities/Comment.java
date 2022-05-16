package com.mantis.mantis_app.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Comment {

    public String authorName;
    public String authorID;
    public String content;
    public String id;

    public static List<Comment> parseComments(JSONArray commentsData){
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < commentsData.length(); i++) {
            try {
                JSONObject comment = commentsData.getJSONObject(i);
                comments.add(new Comment(comment));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return comments;
    }

    public Comment(JSONObject comment){
        try {
            authorName = comment.getString("authorName");
            authorID = comment.getString("authorId");
            content = comment.getString("content");
            id = comment.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
