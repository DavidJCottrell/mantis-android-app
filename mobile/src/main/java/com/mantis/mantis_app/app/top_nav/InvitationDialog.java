package com.mantis.mantis_app.app.top_nav;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.mantis.mantis_app.entities.Invitation;
import com.mantis.mantis_app.entities.User;
import com.mantis.mantis_app.R;

public class InvitationDialog extends DialogFragment {

    public InvitationDialog() {}


    public static InvitationDialog newInstance() {
        InvitationDialog fragment = new InvitationDialog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Allows dialog to be closed when there are no invitations
        if(User.currentUser.invitations.size() != 0){
            Dialog dialog = getDialog();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_invitation_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        for (Invitation invitation : User.currentUser.invitations) {

            InvitationCard invitationCard = new InvitationCard();

            Bundle bundle = new Bundle();
            bundle.putString("invitationID", invitation.id);
            invitationCard.setArguments(bundle);

            ft.add(R.id.invitation_card_container, invitationCard);
        }
        ft.commit();
    }
}