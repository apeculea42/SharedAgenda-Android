package com.myherobots.sharedagenda;

/**
 * Created by ale on 27/01/2018.
 */

public class Users {

    private String mId;
    private String mName;
    private String mUrl;


    public Users(String mId, String mName, String mUrl) {
        this.mId = mId;
        this.mName = mName;
        this.mUrl = mUrl;
    }

    public Users() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {

        this.mName = mName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {

        this.mId = mId;
    }

    public String getUrl() {

        return mUrl;
    }

    public void seturl(String mUrl) {

        this.mUrl = mUrl;
    }


}
