package com.mantis.mantis_app.app.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mantis.mantis_app.entities.Comment;
import com.mantis.mantis_app.entities.Project;
import com.mantis.mantis_app.entities.Task;
import com.mantis.mantis_app.R;

public class CommentCard extends Fragment {

    public CommentCard() {}

    public static CommentCard newInstance(String param1, String param2) {
        CommentCard fragment = new CommentCard();
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
        View view = lf.inflate(R.layout.fragment_comment_card, container, false);

        String projectID = getArguments().getString("projectID");
        String taskID = getArguments().getString("taskID");
        String commentID = getArguments().getString("commentID");

        Project project = Project.getProjectByID(projectID);
        Task task = Project.getProjectTaskByID(project, taskID);
        Comment comment = task.getCommentByID(commentID);

        ((TextView)view.findViewById(R.id.comment_content)).setText(comment.content);
        ((TextView)view.findViewById(R.id.comment_author)).setText(String.valueOf("- " + comment.authorName));


        return view;
    }
}