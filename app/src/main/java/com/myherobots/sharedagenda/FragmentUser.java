package com.myherobots.sharedagenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ale on 02/02/2018.
 */

public class FragmentUser extends Fragment {

    private FirebaseListAdapter<ChatMessage> adapter;
    ArrayList<ChatMessage> listItems;
    ListView listOfMessages;
    TextView name;
    ImageView profilePicture;
    Button addItem;

    FirebaseUser user;
    String facebookUserId;

    private FirebaseDatabase database;
    private DatabaseReference myDatabaseReference, myDatabaseRefUsers;

    TextView text2;
    String userId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);


        text2 = rootView.findViewById(R.id.test);


        addItem = rootView.findViewById(R.id.add_button);
        listOfMessages = rootView.findViewById(R.id.list_of_tasks);
        name = rootView.findViewById(R.id.textView1);
        profilePicture = rootView.findViewById(R.id.profile_image_user);

        listItems = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myDatabaseReference = database.getReference();
        myDatabaseRefUsers = myDatabaseReference.child("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();

        if (user != null) {

            for (UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }

            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

            String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

            Context c = getActivity().getApplicationContext();
            Picasso.with(c).load(photoUrl).into(profilePicture);
            myDatabaseRefUsers.child(userId).push();

            text2.setText(facebookUserId);

            ((MainActivity)getActivity()).receivePhotoUrl(photoUrl);

            displayChatMessages();

        }


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null);

                final EditText edtTaskTitle;
                edtTaskTitle = dialogView.findViewById(R.id.edt_new_task);
                final EditText edtStartTime;
                edtStartTime = dialogView.findViewById(R.id.edt_start_time);
                final EditText edtEndTime;
                edtEndTime = dialogView.findViewById(R.id.edt_end_time);


                edtStartTime.setInputType(0);
                edtStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar c = Calendar.getInstance();


                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        edtStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                    }
                                }, c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)
                                , false);
                        timePickerDialog.show();
                    }
                });

                edtEndTime.setInputType(0);
                edtEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Calendar c = Calendar.getInstance();


                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        edtEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                                    }
                                }, c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)
                                , false);
                        timePickerDialog.show();
                    }
                });

                new AlertDialog.Builder(getContext())
                        .setTitle("Task name")
                        .setView(dialogView)
                        .setPositiveButton("Save", new DialogInterface. OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String key = myDatabaseReference.push().getKey();
                                ChatMessage task = new ChatMessage(edtTaskTitle.getText().toString(), edtStartTime.getText().toString(), edtEndTime.getText().toString(), key, userId);



                                myDatabaseRefUsers.child(userId).child(key).setValue(task);


                            }
                        })
                        .show();


            }
        });

        listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewDialog alert = new ViewDialog();

                text2.setText(listItems.get(i).getTitle());
                alert.showDialog(getActivity(), listItems.get(i));
            }
        });



        return rootView;
    }



    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.list_element, myDatabaseRefUsers.child(user.getUid())) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                TextView messageText = v.findViewById(R.id.txt_name);
                TextView startTime = v.findViewById(R.id.start_time);
                TextView endTime = v.findViewById(R.id.end_time);

                messageText.setText(model.getTitle());
                startTime.setText(model.getStartTime());

                endTime.setText(model.getEndTime());
                listItems.add(position, model);

            }
        };

        listOfMessages.setAdapter(adapter);

    }


    public class ViewDialog {

        public void showDialog(Activity activity, final ChatMessage task) {

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog);


            final EditText task_name = dialog.findViewById(R.id.edt_title);
            task_name.setText(task.getTitle());

            final EditText start_time = dialog.findViewById(R.id.edt_start_time);
            start_time.setText(task.getStartTime());


            final EditText end_time = dialog.findViewById(R.id.edt_end_time);
            end_time.setText(task.getEndTime());


            Button dialogButtonDismiss = dialog.findViewById(R.id.btn_dismiss_dialog);
            dialogButtonDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button deleteButton = dialog.findViewById(R.id.btn_delete_task);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    myDatabaseRefUsers.child(user.getUid()).child(task.getKey()).removeValue();
                    listItems.remove(task);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            Button saveEditButton = dialog.findViewById(R.id.btn_save_edit);
            saveEditButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    task.setTitle(task_name.getText().toString());
                    task.setStartTime(start_time.getText().toString());
                    task.setEndTime(end_time.getText().toString());

                    myDatabaseRefUsers.child(user.getUid()).child(task.getKey()).setValue(task);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }
}
