package com.myherobots.sharedagenda;

/**
 * Created by ale on 12/01/2018.
 */

public class ChatMessage {

    private String mTitle;
    private String mStartTime;
    private String mEndTime;
    private String mKey;
    private String mUid;

    public ChatMessage(String mTitle, String mStartTime, String mEndTime, String mKey, String mUid) {



        this.mTitle = mTitle;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mKey = mKey;
        this.mUid = mUid;
    }

    public ChatMessage() {
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String mStartTime) {
        this.mStartTime = mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String mUid) {
        this.mUid = mUid;
    }

}
