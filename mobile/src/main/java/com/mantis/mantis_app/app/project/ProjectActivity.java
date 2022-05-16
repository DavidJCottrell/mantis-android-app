package com.mantis.mantis_app.app.project;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.mantis.mantis_app.entities.Project;
import com.mantis.mantis_app.entities.Task;
import com.mantis.mantis_app.R;

public class ProjectActivity extends AppCompatActivity {

    Project project;

    public String getProjectID(){ return project.id; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Get the project data for the chosen project
            project = Project.getProjectByID(extras.getString("projectID"));

            // Add a project card per project returned from API
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);

            // Add task cards to project activity
            if(project.tasks.size() > 0){

                // Hide no tasks notice if the project has tasks
                TextView noTasksTV = (TextView) findViewById(R.id.no_tasks_text);
                noTasksTV.setVisibility(View.INVISIBLE);

                for (Task task : project.tasks) {
                    TaskCard taskCard = new TaskCard();

                    Bundle bundle = new Bundle();
                    bundle.putString("taskID", task.id);
                    bundle.putString("projectID", project.id);
                    taskCard.setArguments(bundle);

                    ft.add(R.id.task_cards_fragment_container_view, taskCard, null);
                }
            }
            // Add cards to activity
            ft.commit();
        }
    }
}