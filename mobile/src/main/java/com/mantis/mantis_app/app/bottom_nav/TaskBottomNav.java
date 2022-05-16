package com.mantis.mantis_app.app.bottom_nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.mantis.mantis_app.entities.User;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.app.task.TaskActivity;
import com.mantis.mantis_app.utils.Auth;
import com.mantis.mantis_app.utils.MantisAPI;

import org.json.JSONObject;

import java.util.HashMap;

public class TaskBottomNav extends Fragment {

    MantisAPI api;
    String token;
    Boolean taskIsFollowed = false;

    public TaskBottomNav() {}

    public static TaskBottomNav newInstance() {
        TaskBottomNav fragment = new TaskBottomNav();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_task_bottom_nav, container, false);

        HashMap<String, String> IDs = ((TaskActivity) getActivity()).getIDs();

        if(User.currentUser.taskIsFollowed(IDs.get("taskID"))) taskIsFollowed = true;

        ImageView followBtn = (ImageView) view.findViewById(R.id.followBtn);

        AppCompatActivity currentActivity = (AppCompatActivity) getActivity();

        token = Auth.getStoredToken(currentActivity);
        api = new MantisAPI(currentActivity);

        if(taskIsFollowed) followBtn.setImageDrawable(ContextCompat.getDrawable(currentActivity, R.drawable.followed_icon));

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If the task is currently followed
                if(taskIsFollowed){
                    // Unfollow the task when the button is clicked
                    api.objReq("/users/unfollowtask/" + IDs.get("projectID") + "/" + IDs.get("taskID"), Request.Method.DELETE, new JSONObject(), token,
                        result -> {
                            // Change icon back to the follow icon
                            followBtn.setImageDrawable(ContextCompat.getDrawable(currentActivity, R.drawable.follow_icon));
                            User.currentUser.unfollowTask(IDs.get("taskID"));
                            taskIsFollowed = false;

                            //TODO: Remove JSON entry

                        }
                    );

                }
                // If the task is not currently followed
                else{
                    // Follow the task when the button is clicked
                    api.objReq("/users/followtask/" + IDs.get("projectID") + "/" + IDs.get("taskID"), Request.Method.PATCH, new JSONObject(), token,
                            result -> {
                                // Change the icon to the followed icon
                                followBtn.setImageDrawable(ContextCompat.getDrawable(currentActivity, R.drawable.followed_icon));
                                User.currentUser.followTask(IDs.get("projectID"), IDs.get("taskID"));
                                taskIsFollowed = true;
                            }
                    );
                }
            }
        });
        return view;
    }

}