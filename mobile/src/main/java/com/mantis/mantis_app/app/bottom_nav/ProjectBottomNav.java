package com.mantis.mantis_app.app.bottom_nav;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.mantis.mantis_app.entities.Project;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.app.dashboard.MainActivity;
import com.mantis.mantis_app.app.project.ProjectActivity;
import com.mantis.mantis_app.utils.Auth;
import com.mantis.mantis_app.utils.MantisAPI;

import org.json.JSONObject;


public class ProjectBottomNav extends Fragment {

    MantisAPI api;
    String token;

    public ProjectBottomNav() {}

    public static ProjectBottomNav newInstance() {
        ProjectBottomNav fragment = new ProjectBottomNav();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showConfirmation(){
        String projectID = ((ProjectActivity) getActivity()).getProjectID();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Are you sure you want to leave this project?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        api.objReq("/projects/leave/" + projectID, Request.Method.PATCH, new JSONObject(), token,
                                result -> {
                                    getContext().startActivity(new Intent(getContext(), MainActivity.class));
                                    Project.removeProjectWithId(projectID);
                                }
                        );
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_project_bottom_nav, container, false);

        AppCompatActivity currentActivity = (AppCompatActivity) getActivity();

        ImageView leaveBtn = (ImageView) view.findViewById(R.id.leaveBtn);

        token = Auth.getStoredToken(currentActivity);
        api = new MantisAPI(currentActivity);

        leaveBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 showConfirmation();
             }
        });

        // Inflate the layout for this fragment
        return view;
    }
}