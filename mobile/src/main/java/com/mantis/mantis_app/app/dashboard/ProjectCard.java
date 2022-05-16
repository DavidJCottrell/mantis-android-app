package com.mantis.mantis_app.app.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mantis.mantis_app.entities.Project;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.app.project.ProjectActivity;

public class ProjectCard extends Fragment {


    Project project;

    public ProjectCard() { }

    public static ProjectCard newInstance(String param1, String param2) {
        ProjectCard fragment = new ProjectCard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        project = Project.getProjectByID(getArguments().getString("projectID"));

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_project_card, container, false);

        // Get UI elements to add data to
        TextView projectTitleTV = (TextView) view.findViewById(R.id.project_title);
        TextView activeTasksTV = (TextView) view.findViewById(R.id.active_tasks);
        TextView completeTasksTV = (TextView) view.findViewById(R.id.complete_tasks);
        ProgressBar projectProgressPB = (ProgressBar) view.findViewById(R.id.project_progress_bar);
        TextView projectProgressTV = (TextView) view.findViewById(R.id.project_progress_text);
        TextView teamSizeTV = (TextView) view.findViewById(R.id.team_size);
        TextView openBtn = (TextView) view.findViewById(R.id.project_open_btn);

        // Add data
        projectTitleTV.setText(project.title);
        activeTasksTV.setText(String.valueOf("Active tasks: " + project.activeTasks));
        completeTasksTV.setText(String.valueOf("Complete tasks: " + project.completeTasks));
        teamSizeTV.setText(String.valueOf("Team size: " + project.projectMembers.size()));

        projectProgressPB.setProgress(project.completionPercentage);
        projectProgressTV.setText(String.valueOf(project.completionPercentage + "%"));

        openBtn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ProjectActivity.class);
            i.putExtra("projectID", project.id);
            startActivity(i);
        });

        return view;
    }
}