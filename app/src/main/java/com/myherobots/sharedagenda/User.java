package com.myherobots.sharedagenda;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.internal.zzdym;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import java.util.List;

/**
 * Created by coman on 04/02/2018.
 */

public class User extends FirebaseUser {
    @NonNull
    @Override
    public String getUid() {
        return null;
    }

    @NonNull
    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Nullable
    @Override
    public List<String> getProviders() {
        return null;
    }

    @NonNull
    @Override
    public List<? extends UserInfo> getProviderData() {
        return null;
    }

    @NonNull
    @Override
    public FirebaseUser zzaq(@NonNull List<? extends UserInfo> list) {
        return null;
    }

    @Override
    public FirebaseUser zzcf(boolean b) {
        return null;
    }

    @NonNull
    @Override
    public FirebaseApp zzbre() {
        return null;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return null;
    }

    @Nullable
    @Override
    public String getEmail() {
        return null;
    }

    @Nullable
    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @NonNull
    @Override
    public zzdym zzbrf() {
        return null;
    }

    @Override
    public void zza(@NonNull zzdym zzdym) {

    }

    @NonNull
    @Override
    public String zzbrg() {
        return null;
    }

    @NonNull
    @Override
    public String zzbrh() {
        return null;
    }

    @Nullable
    @Override
    public FirebaseUserMetadata getMetadata() {
        return null;
    }
}
