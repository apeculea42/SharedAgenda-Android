package com.myherobots.sharedagenda;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SampleFragment extends Fragment {



    ImageView profilePicture;
    TextView name;
    String facebookUserId;
    ListView listTasks;
    ArrayList<ChatMessage> listItems;

    FirebaseUser user;
    private FirebaseListAdapter<ChatMessage> adapter;

    Button addItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container,
                false);

        facebookUserId = "";
        user = FirebaseAuth.getInstance().getCurrentUser();
        profilePicture = rootView.findViewById(R.id.profile_image_user);
        name = rootView.findViewById(R.id.textView1);
        listTasks = rootView.findViewById(R.id.list_of_tasks);
        addItem = rootView.findViewById(R.id.add_button);



        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();
                }
            }
        }


        String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

        Context c = getActivity().getApplicationContext();
        Picasso.with(c).load(photoUrl).into(profilePicture);

        if (user != null) {
            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            displayChatMessage();
        }

        listItems = new ArrayList<>();

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_view, null);

                final EditText edtTaskTitle = dialogView.findViewById(R.id.edt_new_task);
                final EditText edtStartTime = dialogView.findViewById(R.id.edt_start_time);
                final EditText edtEndTime = dialogView.findViewById(R.id.edt_end_time);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Enter your task")
                        .setView(dialogView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                                  /*       start = start + task.getDurLong();
                                                         String display = avatarDuration(start);
                                                         durationAlex.setText(display); */

                                ChatMessage chat = new ChatMessage(edtTaskTitle.getText().toString(), edtStartTime.getText().toString(), edtEndTime.getText().toString(), user.getEmail());
                                FirebaseDatabase.getInstance().getReference().push().setValue(chat);
                                listItems.add(chat);
                            }
                        })
                        .show();
            }


        });

        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ViewDialog alert = new ViewDialog();
                alert.showDialog(getActivity(), listItems.get(position));
            }
        });

        return rootView;
    }

    private void displayChatMessage() {
        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class, R.layout.list_element, FirebaseDatabase.getInstance().getReference()) {
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
        };
        listTasks.setAdapter(adapter);

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
/*
            final CheckBox checkBox = dialog.findViewById(R.id.checkbox_listelem);
            checkBox.setChecked(task.getCheck());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBox.isChecked()){

                        task.setCheck(true);

                        Long durBefore = task.getDurLong();

                        Long text = start - durBefore;
                        Long textDone = doneStart + durBefore;
                        start = text;
                        doneStart = textDone;
                        String display = avatarDuration(text);
                        String displayDone = avatarDuration(textDone);
                        durationAlex.setText(display);
                        durationAlexDone.setText(displayDone);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        task.setCheck(false);

                        task.setTitle(task_name.getText().toString());
                        task.setStartTime(start_time.getText().toString());
                        task.setEndTime(end_time.getText().toString());

                        Long durAfter = task.getDurLong();
                        Long text = start + durAfter;
                        Long textDone = doneStart - durAfter;
                        start = text;
                        doneStart = textDone;
                        String display = avatarDuration(text);
                        String displayDone = avatarDuration(textDone);
                        durationAlex.setText(display);
                        durationAlexDone.setText(displayDone);
                        adapter.notifyDataSetChanged();
                    }
                }
            }); */



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
               //     Long durBefore = task.getDurLong();
                    listItems.remove(task);
            //        Long text = start - durBefore;
           //         start = text;
           //         String display = avatarDuration(text);
              //      durationAlex.setText(display);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });



            Button saveEditButton = dialog.findViewById(R.id.btn_save_edit);
            saveEditButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
              //      Long durBefore = task.getDurLong();

                    task.setTitle(task_name.getText().toString());
                    task.setStartTime(start_time.getText().toString());
                    task.setEndTime(end_time.getText().toString());

             //       Long durAfter = task.getDurLong();
             //       Long text = start - durBefore + durAfter;
                    //String durationString = avatarDuration(text);
                    //durationAlex.setText(durationString);
             //       start = text;
             //       String display = avatarDuration(text);
             //       durationAlex.setText(display);

                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

}