package com.myherobots.sharedagenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class SampleFragment extends Fragment {

    private FirebaseListAdapter<ChatMessage> adapter;
    ArrayList<ChatMessage> listItems;
    ListView listOfMessages;
    TextView name;
    ImageView profilePicture;
    Button addItem;

    final ArrayList<String> keyList = new ArrayList<>();
    final ArrayList<String> items = new ArrayList<>();

    FirebaseUser user;
    String facebookUserId;

    private FirebaseDatabase database;
    private DatabaseReference myDatabaseReference, myDatabaseRefUsers;

    TextView text2;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_one, container,
                false);


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
                                ChatMessage task = new ChatMessage(edtTaskTitle.getText().toString(), edtStartTime.getText().toString(), edtEndTime.getText().toString(), key);



                                myDatabaseRefUsers.child(userId).child(key).setValue(task);


                            }
                        })
                        .show();


            }
        });
/*
        myDatabaseReference.getRoot().child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot messages : dataSnapshot.getChildren()) {
                            keyList.add(messages.getKey());
                            items.add(messages.getValue(String.class));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });  */

        listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewDialog alert = new ViewDialog();


                text2.setText("a");
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
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.txt_name);
                TextView messageUser = (TextView)v.findViewById(R.id.start_time);
                TextView messageTime = (TextView)v.findViewById(R.id.end_time);

                // Set their text
                messageText.setText(model.getTitle());
                messageUser.setText(model.getStartTime());

                // Format the date before showing it
                messageTime.setText(model.getEndTime());
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


           Button dialogButtonDismiss= dialog.findViewById(R.id.btn_dismiss_dialog);
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
                    listItems.remove(task);

                    myDatabaseRefUsers.child(user.getUid()).child(task.getKey()).removeValue();
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























/*

    ImageView profilePicture;
    TextView name;
    String facebookUserId;

    TextView title, start, end;

    FirebaseUser user;
    ArrayAdapter<ChatMessage> adapter;
    ListView list;
    ArrayList<ChatMessage> listItems;

    Button addItem;
    private FirebaseDatabase database;
    private DatabaseReference myDatabaseReference;
    private String personId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_one, container,
                false);


        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        list = rootView.findViewById(R.id.list_of_tasks);
        listItems = new ArrayList<>();
        adapter = new CustomAdapter(getContext(), listItems);

        facebookUserId = "";
        user = FirebaseAuth.getInstance().getCurrentUser();
        profilePicture = rootView.findViewById(R.id.profile_image_user);
        name = rootView.findViewById(R.id.textView1);
        addItem = rootView.findViewById(R.id.add_button);



        database = FirebaseDatabase.getInstance();
        myDatabaseReference=database.getReference();
        personId= myDatabaseReference.push().getKey();

        if (user != null) {

            for (UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }

            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        }

        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

        Context c = getActivity().getApplicationContext();
        Picasso.with(c).load(photoUrl).into(profilePicture);

        list.setAdapter(adapter);
        /*adapter = new FirebaseListAdapter<ChatMessage>(getActivity(),ChatMessage.class,R.layout.list_element, myDatabaseReference) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView text, start, end;

                text = v.findViewById(R.id.txt_name);
                start = v.findViewById(R.id.start_time);
                end = v.findViewById(R.id.end_time);

                text.setText(model.getTitle());
                start.setText(model.getStartTime());
                end.setText(model.getEndTime());



            }
        }; */


/*
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view, null);

                final EditText edtTaskTitle = dialogView.findViewById(R.id.edt_new_task);
                final EditText edtStartTime = dialogView.findViewById(R.id.edt_start_time);
                final EditText edtEndTime = dialogView.findViewById(R.id.edt_end_time);
                edtStartTime.requestFocus();
                edtEndTime.requestFocusFromTouch();

                edtStartTime.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                edtStartTime.setText( String.format("%02d:%02d", selectedHour, selectedMinute));
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();

                    }
                });

                edtEndTime.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                edtEndTime.setText( String.format("%02d:%02d", selectedHour, selectedMinute));
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();

                    }
                });

                new AlertDialog.Builder(getContext())
                        .setTitle("Task name")
                        .setView(dialogView)
                        .setPositiveButton("Save", new DialogInterface. OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                addTask(edtTaskTitle.getText().toString(), edtStartTime.getText().toString(), edtEndTime.getText().toString());

                            }
                        })
                        .show();
            }
        });


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        myDatabaseReference.addValueEventListener(postListener);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ViewDialog alert = new ViewDialog();
                alert.showDialog(getActivity(), listItems.get(position));
            }
        });


        return rootView;
    }



    @Override
    public void onResume() {

        LinearLayout item = getView().findViewById(R.id.pager);
        View child = getLayoutInflater().inflate(R.layout.fragment_one, null);
     //   item.addView(child);


        TextView test = child.findViewById(R.id.test);
        test.setText("Teest");


        super.onResume();
    }

    private void displayChatMessage(List<ChatMessage> ls) {

            listItems.clear();
            listItems.addAll(ls);
            adapter.notifyDataSetChanged();

    }

    public void updateAdapter(){

        final List<ChatMessage> listUsers= new ArrayList<>();
        myDatabaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listUsers.add(dataSnapshot.getValue(ChatMessage.class));
                displayChatMessage(listUsers);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void addTask(String title, String startTime, String endTime) {
        String chatId = myDatabaseReference.push().getKey();
        ChatMessage chat = new ChatMessage(title, startTime, endTime);
        listItems.add(chat);
        myDatabaseReference.child("ChatMessage").child(chatId).setValue(chat);
        updateAdapter();
        adapter.notifyDataSetChanged();

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


            Button dialogButtonDismiss= dialog.findViewById(R.id.btn_dismiss_dialog);
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
                    listItems.remove(task);
                    myDatabaseReference.removeValue();
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


                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    } */

}