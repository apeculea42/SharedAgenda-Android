package com.myherobots.sharedagenda;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.myherobots.MyApplication;
import com.squareup.picasso.Picasso;

/**
 * Created by ale on 18/01/2018.
 */


@NonReusable
@Layout(R.layout.drawer_header)
public class DrawerHeader {

    @View(R.id.profileImageView)
    private ImageView profileImage;

    @View(R.id.nameTxt)
    private TextView nameTxt;

    @View(R.id.emailTxt)
    private TextView emailTxt;


    @Resolve
    private void onResolved() {

            nameTxt.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            emailTxt.setText("work in progress");

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String facebookUserId;

            for (UserInfo profile : user.getProviderData()) {
                // check if the provider id matches "facebook.com"
                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                    facebookUserId = profile.getUid();

                    String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

                    Context c = MyApplication.getAppContext();
                    Picasso.with(c).load(photoUrl).into(profileImage);
                }
            }




    }
}