package com.mantis.mantis_app.app.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.mantis.mantis_app.R;
import com.mantis.mantis_app.utils.MantisAPI;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpDialog extends DialogFragment {

    public SignUpDialog() {}

    MantisAPI api;

    public static SignUpDialog newInstance() {
        SignUpDialog fragment = new SignUpDialog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the layout inflater and view
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_sign_up_dialog, container, false);

        // Get the form elements
        EditText emailInput = (EditText) view.findViewById(R.id.new_email_input);
        EditText firstNameInput = (EditText) view.findViewById(R.id.first_name_input);
        EditText lastNameInput = (EditText) view.findViewById(R.id.last_name_input);
        EditText passwordInput = (EditText) view.findViewById(R.id.new_password_input);
        EditText verifyPasswordInput = (EditText) view.findViewById(R.id.verify_new_password_input);
        Button signUpButton = (Button) view.findViewById(R.id.submit_sign_up_btn);

        // Create a new instance of the API class
        api = new MantisAPI((AppCompatActivity) getActivity());

        // When the sign up button is clicked
        signUpButton.setOnClickListener(view1 -> {
            // Get the user's credentials ready to be sent to the API
            JSONObject signUpData = new JSONObject();
            try {
                signUpData.put("email", emailInput.getText().toString());
                signUpData.put("firstName", firstNameInput.getText().toString());
                signUpData.put("lastName", lastNameInput.getText().toString());
                signUpData.put("password", passwordInput.getText().toString());
                signUpData.put("vpassword", verifyPasswordInput.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Call the sign up API endpoint
            api.signUp(signUpData);
        });

        return view;
    }
}