package com.mantis.mantis_app.app.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.mantis.mantis_app.R;
import com.mantis.mantis_app.utils.MantisAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create an instance of the API class
        MantisAPI api = new MantisAPI(this);

        // Get the form elements
        Button loginBtn = findViewById(R.id.loginBtn);
        Button signUpBtn = findViewById(R.id.signUpBtn);
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);

        // Create an instance of the sign up dialog
        DialogFragment signUpDialog = new SignUpDialog();

        // When the login button is clicked
        loginBtn.setOnClickListener(v -> {

            // Get the values from the email and password inputs
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Prevent an unnecessary API call
            if(!email.equals("") && !password.equals("")){
                // Construct the request body with user's credentials
                JSONObject postData = new JSONObject();
                try {
                    postData.put("email", email);
                    postData.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Call the API login endpoint
                api.login(postData);
            }
        });

        // Show the sign up dialog when the sign up button is clicked
        signUpBtn.setOnClickListener(v -> signUpDialog.show(getSupportFragmentManager(), "dialog"));
    }

    @Override
    public void onBackPressed() {
        // Prevent user from going back to the last page after logging out
        // Does nothing without super.onBackPressed();
    }
}