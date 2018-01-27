package com.myherobots.sharedagenda;

/**
 * Created by ale on 27/01/2018.
 */

public class Users {

    private String mCurrent;
    private String mPartenerUser;


    public Users(String mCurrent, String mPartenerUser) {
        this.mCurrent = mCurrent;
        this.mPartenerUser = mPartenerUser;
    }

    public Users() {
    }

    public String getCurrentUser() {
        return mCurrent;
    }

    public void setCurrentUser(String mCurrent) {

        this.mCurrent = mCurrent;
    }

    public String getPartenerUser() {

        return mPartenerUser;
    }

    public void setPartenerUser(String mPartener) {

        this.mPartenerUser = mPartenerUser;
    }


}
