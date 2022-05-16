package com.mantis.mantis_app.app.task;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.mantis.mantis_app.entities.Comment;
import com.mantis.mantis_app.entities.Project;
import com.mantis.mantis_app.entities.Task;
import com.mantis.mantis_app.entities.User;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.utils.Auth;
import com.mantis.mantis_app.utils.MantisAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    Task task;
    Project project;
    MantisAPI api;
//    HashMap<String, String> userDetails;
    List<CommentCard> commentCards = new ArrayList<>();
    String token;
    String projectID;
    String taskID;

    public HashMap<String, String> getIDs(){
        HashMap<String, String> IDs = new HashMap<>();
        IDs.put("projectID", projectID);
        IDs.put("taskID", taskID);
        return IDs;
    }

    void setStatusBar(String status){
        ImageView inDevIcon = (ImageView) findViewById(R.id.in_dev_icon);
        ImageView testingIcon = (ImageView) findViewById(R.id.testing_icon);
        ImageView inReview = (ImageView) findViewById(R.id.in_review_icon);
        ImageView readyToMergeIcon = (ImageView) findViewById(R.id.ready_to_merge_icon);
        ImageView resolvedIcon = (ImageView) findViewById(R.id.resolved_icon);

        switch (status){
            case "In Development":
                inDevIcon.setBackgroundColor(Color.GREEN);
                break;
            case "Testing":
                testingIcon.setBackgroundColor(Color.GREEN);
                break;
            case "In Review":
                inReview.setBackgroundColor(Color.GREEN);
                break;
            case "Ready to Merge":
                readyToMergeIcon.setBackgroundColor(Color.GREEN);
                break;
            case "Resolved":
                resolvedIcon.setBackgroundColor(Color.GREEN);
                break;
        }
    }

    void setTaskInfo(Task task){
        String assigneesHtmlList = new String("<html><ul>");
        for (HashMap<String, String> assignee: task.taskAssignees)
            assigneesHtmlList = assigneesHtmlList.concat("<li> " + assignee.get("name") + "</li>");

        assigneesHtmlList = assigneesHtmlList.concat("</ul></html>");

        ((TextView)findViewById(R.id.task_assignees)).setText(Html.fromHtml(assigneesHtmlList));

        String reporterHtmlText = new String("<html><ul><li> " + task.reporter.get("name") + "</li><ul></html>");
        ((TextView)findViewById(R.id.task_reporter)).setText(Html.fromHtml(reporterHtmlText));
    }

    void renderComments(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);

        // Loop through each comment for the current task
        for (Comment comment : task.comments) {

            // Create a new CommentCard fragment
            CommentCard commentCard = new CommentCard();

            // Send the required ID's for the fragment to find the objects
            Bundle bundle = new Bundle();
            bundle.putString("projectID", project.id);
            bundle.putString("taskID", task.id);
            bundle.putString("commentID", comment.id);
            commentCard.setArguments(bundle);

            // Add fragment to transaction
            ft.add(R.id.comment_card_fragment_container_view, commentCard, null);
            commentCards.add(commentCard);
        }
        // Add comment fragments to view
        ft.commit();
    }

    void onAddCommentBtnClick(EditText newCommentText){

        if(newCommentText.getText().toString().equals("")) return;

        // API requires list of comment json objects
        JSONArray commentsJson = task.commentsToJSON();

        JSONObject newCommentJson = new JSONObject();
        try {
            newCommentJson.put("authorName", User.currentUser.firstName + " " + User.currentUser.lastName);
            newCommentJson.put("authorId", User.currentUser.id);
            newCommentJson.put("content", newCommentText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        commentsJson.put(newCommentJson);

        JSONObject body = new JSONObject();
        try {
            body.put("comments", commentsJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String APIRoute = "/projects/tasks/comments/updatecomments/" + project.id + "/" + task.id;

        api.arrayReq(APIRoute, Request.Method.PATCH, commentsJson, token,
                result -> {
                    try {
                        JSONObject newComment = result.getJSONObject(result.length() - 1);
                        task.comments.add(new Comment(newComment));

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
                        for (Fragment fragment : commentCards){
                            ft.remove(fragment);
                        }
                        ft.commit();
                        renderComments();
                        newCommentText.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //Get user's stored token
        token = Auth.getStoredToken(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            projectID = extras.getString("projectID");
            taskID = extras.getString("taskID");

            project = Project.getProjectByID(projectID);
            task = Project.getProjectTaskByID(project, taskID);

            ((TextView)findViewById(R.id.task_activity_title)).setText(Html.fromHtml("Task " + task.taskKey + " - " + task.title));

            setStatusBar(task.status);
            setTaskInfo(task);

            renderComments();

            EditText newCommentText = (EditText) findViewById(R.id.new_comment_text);

            api = new MantisAPI(this);

            Button addCommentBtn = (Button) findViewById(R.id.add_comment_btn);
            addCommentBtn.setOnClickListener(view -> onAddCommentBtnClick(newCommentText));
        }

    }
}