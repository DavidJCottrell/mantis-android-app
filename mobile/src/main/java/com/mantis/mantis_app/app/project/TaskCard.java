package com.mantis.mantis_app.app.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mantis.mantis_app.entities.Project;
import com.mantis.mantis_app.entities.Task;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.app.task.TaskActivity;

public class TaskCard extends Fragment {

    Task task;

    public TaskCard() {}

    public static TaskCard newInstance(String param1, String param2) {
        TaskCard fragment = new TaskCard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String projectID = getArguments().getString("projectID");
        String taskID = getArguments().getString("taskID");

        task = Project.getProjectTaskByID(Project.getProjectByID(projectID), taskID);

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_task_card, container, false);


        // Get parent activity
        Activity activity = getActivity();

        // Get UI elements to add data to
        TextView taskTitleTV = (TextView) view.findViewById(R.id.task_title);
        TextView taskDescTV = (TextView) view.findViewById(R.id.task_description);
        TextView taskTypeTV = (TextView) view.findViewById(R.id.task_type);
        TextView taskKeyTV = (TextView) view.findViewById(R.id.task_key);
        TextView taskStatusTV = (TextView) view.findViewById(R.id.task_status);
        TextView taskResTV = (TextView) view.findViewById(R.id.task_resolution);
        TextView taskDueDateTV = (TextView) view.findViewById(R.id.task_due_date);
        TextView openBtn = (TextView) view.findViewById(R.id.task_open_btn);

        taskTitleTV.setText(task.title);
        taskDescTV.setText(task.description);
        taskTypeTV.setText(String.valueOf("Type: " + task.type));
        taskKeyTV.setText(String.valueOf("Key: " + task.taskKey));
        taskStatusTV.setText(String.valueOf("Status: " + task.status));
        taskResTV.setText(String.valueOf("Status: " + task.resolution));
        taskDueDateTV.setText(String.valueOf("Due: " + task.dateDue));

        openBtn.setOnClickListener(v -> {
            Intent i = new Intent(activity, TaskActivity.class);
            i.putExtra("projectID", projectID);
            i.putExtra("taskID", taskID);
            startActivity(i);
        });

        return view;
    }
}