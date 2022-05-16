package com.mantis.mantis_app.app.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.mantis.mantis_app.entities.Invitation;
import com.mantis.mantis_app.entities.Project;
import com.mantis.mantis_app.entities.User;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.utils.Auth;
import com.mantis.mantis_app.utils.BackgroundService;
import com.mantis.mantis_app.utils.MantisAPI;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String token;
    MantisAPI api;
    HashMap<String, String> userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // assigning ID of the toolbar to a variable
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        TextView loggedUserTxt = (TextView) findViewById(R.id.dashboard_user_name);

        //Get user's stored token
        token = Auth.getStoredToken(this);

        // Create instance of API class to make API calls
        api = new MantisAPI(this);

        if(Auth.isLoggedIn){

            Intent intent = new Intent(this, BackgroundService.class);
            intent.putExtra("token", token);
            startService(intent);

            // Get the list of the users
            api.objReq("/users/projects/", Request.Method.GET, new JSONObject(), token,
                    // Once the data has been fetched
                    result -> {
                        // Construct project classes based on fetched project data
                        Project.parseProjectData(result);

                        // Add a project card per project returned from API
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
                        for (Project project : Project.getAllProjects()) {
                            ProjectCard projectCard = new ProjectCard();

                            Bundle bundle = new Bundle();
                            bundle.putString("projectID", project.id);
                            projectCard.setArguments(bundle);

                            ft.add(R.id.project_cards_fragment_container_view, projectCard, null);
                        }
                        // Add cards to activity
                        ft.commit();
                    }
            );

            api.objReq("/users/getuser/", Request.Method.GET, new JSONObject(), token, userData -> {

                User.parseUserData(userData);
                loggedUserTxt.setText(User.currentUser.firstName + " " + User.currentUser.lastName);

                api.objReq("/users/invitations/", Request.Method.GET, new JSONObject(), token,
                        invitationData -> {
                            User.currentUser.setInvitations(Invitation.parseInvitationsData(invitationData));
                        }
                );

            });
        }
    }

    public void refreshProjects(){
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Auth.isLoggedIn) Auth.logout(MainActivity.this);
    }
}