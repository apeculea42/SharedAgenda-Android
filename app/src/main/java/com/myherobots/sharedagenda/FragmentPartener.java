package com.myherobots.sharedagenda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by ale on 03/02/2018.
 */


public class FragmentPartener extends Fragment {


    private FirebaseDatabase database;
    private DatabaseReference myDatabaseReference;
    private FirebaseListAdapter<ChatMessage> adapterTasks;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ListView usersList;
    LinearLayout partenerLayout;
    Button change;

    ListView listOfMessages;

    TextView displayName, test, usersListText;
    ImageView profileImage;

    String partenerName, partenerUrl, partenerUId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container, false);

        database = FirebaseDatabase.getInstance();
        myDatabaseReference = database.getReference();

        usersList = rootView.findViewById(R.id.users_list);
        partenerLayout = rootView.findViewById(R.id.partener_layout);
        displayName = rootView.findViewById(R.id.textView1);
        profileImage = rootView.findViewById(R.id.profile_image_user);
        test = rootView.findViewById(R.id.test);
        usersListText = rootView.findViewById(R.id.users_list_text);

        listOfMessages = rootView.findViewById(R.id.list_of_tasks);

        pref = ((MainActivity)getActivity()).sendPref();
        editor = pref.edit();

        if (Objects.equals(pref.getString("hasPartener", null), "yes")) {
            displayPartener();
            usersList.setVisibility(View.GONE);
            usersListText.setVisibility(View.GONE);
            partenerLayout.setVisibility(View.VISIBLE);

        } else {
            usersList.setVisibility(View.VISIBLE);
            usersListText.setVisibility(View.VISIBLE);
            partenerLayout.setVisibility(View.GONE);
        }

        change = rootView.findViewById(R.id.add_button);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("hasPartener", "no");
                editor.apply();
                usersList.setVisibility(View.VISIBLE);
                usersListText.setVisibility(View.VISIBLE);
                partenerLayout.setVisibility(View.GONE);
            }
        });



        // Attach a listener to read the data at our posts reference
        myDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.getKey().equals("Users")) {

                        if (1 == 1) {
                            final HashMap<String, FirebaseUser> users = (HashMap<String, FirebaseUser>) dataSnapshot1.getValue();
                            final ArrayList<String> userIds = new ArrayList<>(users.keySet());
                            final ArrayList<Users> usersToChoose = new ArrayList<>();


                            int size = userIds.size();
                            int i = 0;

                            while (i < size) {

                                Users userInformation = new Users(userIds.get(i), pref.getString(userIds.get(i), null), pref.getString(userIds.get(i).concat("photo"), null));
                                usersToChoose.add(i, userInformation);
                                i++;
                            }
                            CustomAdapter adapter = new CustomAdapter(getContext(), usersToChoose);
                            usersList.setAdapter(adapter);

                            usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    FragmentPartener.ViewDialog alert = new FragmentPartener.ViewDialog();
                                    alert.showDialog(getActivity(), usersToChoose.get(i));
                                }
                            });

                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return rootView;
    }

    private void displayPartener() {


        partenerName = pref.getString("partenerName", null);
        partenerUrl = pref.getString("partenerURL", null);
        partenerUId = pref.getString("partener", null);

        Context c = getActivity();
        Picasso.with(c).load(partenerUrl).into(profileImage);
        displayName.setText(partenerName);

        adapterTasks = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.list_element, myDatabaseReference.child("Users").child(partenerUId)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.txt_name);
                TextView startTime = (TextView)v.findViewById(R.id.start_time);
                TextView endTime = (TextView)v.findViewById(R.id.end_time);

                // Set their text
                messageText.setText(model.getTitle());
                startTime.setText(model.getStartTime());

                // Format the date before showing it
                endTime.setText(model.getEndTime());


            }
        };

        listOfMessages.setAdapter(adapterTasks);
        test.setText(pref.getString("partener", null));

    }

    public class ViewDialog {
        public void showDialog(Activity activity, final Users partener){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_new_partener);

            TextView question = dialog.findViewById(R.id.ask_text);
            question.setText(String.format("Do you want %s to be your partener?", partener.getName()));

            Button yesButton = dialog.findViewById(R.id.yes_btn);
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editor.putString("partener", partener.getId());
                    editor.putString("partenerName", partener.getName());
                    editor.putString("partenerURL", partener.getUrl());
                    editor.putString("currentUser", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.putString("hasPartener", "yes");
                    editor.apply();
                    displayPartener();
                    dialog.dismiss();
                    usersList.setVisibility(View.GONE);
                    usersListText.setVisibility(View.GONE);
                    partenerLayout.setVisibility(View.VISIBLE);
                }
            });

            Button noButton = dialog.findViewById(R.id.no_btn);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }

}