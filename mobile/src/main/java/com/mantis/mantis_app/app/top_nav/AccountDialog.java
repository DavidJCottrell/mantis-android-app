package com.mantis.mantis_app.app.top_nav;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.mantis.mantis_app.entities.User;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.utils.Auth;

public class AccountDialog extends DialogFragment {

    public AccountDialog() {}

    public static AccountDialog newInstance() {
        AccountDialog fragment = new AccountDialog();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_account_dialog, container, false);

        Button logoutBtn = (Button) view.findViewById(R.id.logout_btn);

        TextView fullnameText = (TextView) view.findViewById(R.id.fullname_text);
        TextView usernameText = (TextView) view.findViewById(R.id.username_text);
        TextView emailText = (TextView) view.findViewById(R.id.email_text);

        fullnameText.setText("Name - " + User.currentUser.firstName + " " + User.currentUser.lastName);
        usernameText.setText("Username - " + User.currentUser.username);
        emailText.setText("Email - " + User.currentUser.email);

        logoutBtn.setOnClickListener(v -> Auth.logout((AppCompatActivity) getActivity()));

        return view;
    }
}