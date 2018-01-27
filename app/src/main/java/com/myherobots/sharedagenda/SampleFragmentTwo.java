package com.myherobots.sharedagenda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SampleFragmentTwo extends Fragment {


    TextView name;
    ListView listTasks;

    FirebaseUser user;
    private FirebaseListAdapter<ChatMessage> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container,
                false);


        name = rootView.findViewById(R.id.tester);

        if (user != null) {
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        }


        return rootView;

    }


}