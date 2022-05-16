package com.mantis.mantis_app.app.top_nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.mantis.mantis_app.entities.Invitation;
import com.mantis.mantis_app.entities.User;
import com.mantis.mantis_app.R;
import com.mantis.mantis_app.app.dashboard.MainActivity;
import com.mantis.mantis_app.utils.Auth;
import com.mantis.mantis_app.utils.MantisAPI;

import org.json.JSONObject;

public class InvitationCard extends Fragment {

    private Invitation invitation;
    private MantisAPI api;

    public InvitationCard() {}

    public static InvitationCard newInstance() {
        InvitationCard fragment = new InvitationCard();
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
        View view = lf.inflate(R.layout.fragment_invitation_card, container, false);

        invitation = User.currentUser.getInvitationByID(getArguments().getString("invitationID"));

        api = new MantisAPI((AppCompatActivity) getActivity());

        String token = Auth.getStoredToken((AppCompatActivity) getActivity());

        TextView titleTV = (TextView) view.findViewById(R.id.invitation_task_title);
        TextView inviterNameTV = (TextView) view.findViewById(R.id.inviter_name);
        TextView invitationAcceptBtn = (TextView) view.findViewById(R.id.invitation_accept_btn);
        TextView invitationDeclineBtn = (TextView) view.findViewById(R.id.invitation_decline_btn);

        titleTV.setText(invitation.project.get("title"));
        inviterNameTV.setText(invitation.inviter.get("name"));

        invitationAcceptBtn.setOnClickListener(v -> {
            api.objReq("/invitations/accept/" + invitation.id, Request.Method.POST, new JSONObject(), token,
                response -> {
                    ((MainActivity) getActivity()).refreshProjects();
                }
            );
        });

        invitationDeclineBtn.setOnClickListener(v -> {
            api.objReq("/invitations/delete/" + invitation.id, Request.Method.DELETE, new JSONObject(), token,
                response -> {
                    ((MainActivity) getActivity()).refreshProjects();
                }
            );
        });


        return view;
    }
}