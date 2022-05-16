package com.mantis.mantis_app.app.top_nav;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.mantis.mantis_app.R;

public class TopNav extends Fragment {

    public TopNav() { }

    public static TopNav newInstance() {
        TopNav fragment = new TopNav();
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
        View view = lf.inflate(R.layout.fragment_top_nav, container, false);

        // Get parent activity
        Activity activity = getActivity();

        // Create dialog object
        DialogFragment invitationDialog = new InvitationDialog();
        DialogFragment accountDialog = new AccountDialog();

        // Open invitation dialog window on icon click
        ImageView invitationBtn = (ImageView) view.findViewById(R.id.invitationBtn);
        invitationBtn.setOnClickListener(v -> {
            if(activity != null) invitationDialog.show(getChildFragmentManager(), "dialog");
        });

        // Open account dialog window on icon click
        ImageView accountBtn = (ImageView) view.findViewById(R.id.accountBtn);
        accountBtn.setOnClickListener(v -> {
            if(activity != null) accountDialog.show(getChildFragmentManager(), "dialog");
        });

        return view;
    }
}