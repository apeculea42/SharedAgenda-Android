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



        if (user != null) {
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            displayChatMessage();
        }




        return rootView;

    }

    private void displayChatMessage() {

      /*  adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class, R.layout.list_element, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView text, start, end;

                if (!Objects.equals(model.getEmail(), FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                    text = v.findViewById(R.id.txt_name);
                    start = v.findViewById(R.id.start_time);
                    end = v.findViewById(R.id.end_time);


                    text.setText(model.getTitle());
                    start.setText(model.getStartTime());
                    end.setText(model.getEndTime());

                }

            }
        };
        listTasks.setAdapter(adapter); */

    }
}