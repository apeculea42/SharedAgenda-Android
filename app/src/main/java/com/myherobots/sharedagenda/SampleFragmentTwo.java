package com.myherobots.sharedagenda;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SampleFragmentTwo extends Fragment implements SampleFragment.OnFragmentInteractionListener{


    private TextView n;
    TextView name;

    FirebaseUser user;

    public String uId;
    String facebookUserId;


    private FirebaseListAdapter<ChatMessage> adapter;

    ListView listOfMessages;
    private DatabaseReference myDatabaseRefUsers;
    ChatMessage chat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container,
                false);


        name = rootView.findViewById(R.id.textView1);
        n = rootView.findViewById(R.id.tester);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uId = user.getUid();


        if (user != null) {

            for (UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }

            myDatabaseRefUsers = FirebaseDatabase.getInstance().getReference().child("Users");
//            displayChatMessages();
        }
        chat = new ChatMessage();



        return rootView;

    }


    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.list_element, myDatabaseRefUsers.child("sijKXKwTqmhJQLSPgo6mplhcFm93")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                if (chat.getUid() != user.getUid()) {
                    // Get references to the views of message.xml
                    TextView messageText = (TextView) v.findViewById(R.id.txt_name);
                    TextView startTime = (TextView) v.findViewById(R.id.start_time);
                    TextView endTime = (TextView) v.findViewById(R.id.end_time);

                    // Set their text
                    messageText.setText(model.getTitle());
                    startTime.setText(model.getStartTime());

                    // Format the date before showing it
                    endTime.setText(model.getEndTime());
                    // listItems.add(position, model);

                }
            }
        };

        listOfMessages.setAdapter(adapter);

    }


    @Override
    public void onFragmentInteraction(String name, String desc) {

    }
}