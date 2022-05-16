package com.mantis.mantis_app.app.login;

import android.os.Bundle;
import android.util.Log;
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

        MantisAPI api = new MantisAPI(this);

        Button loginBtn = findViewById(R.id.loginBtn);
        Button signUpBtn = findViewById(R.id.signUpBtn);
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);

        DialogFragment signUpDialog = new SignUpDialog();

        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(!email.equals("") && !password.equals("")){
                    JSONObject postData = new JSONObject();
                    try {
                        postData.put("email", email);
                        postData.put("password", password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    api.login(postData);
                }

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                signUpDialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent user from going back to the last page after logging out
        // Does nothing without super.onBackPressed();
    }
}