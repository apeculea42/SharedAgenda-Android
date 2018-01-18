package com.myherobots.sharedagenda;

/**
 * Created by ale on 12/01/2018.
 */

public class ChatMessage {

    private String mTitle;
    private String mStartTime;
    private String mEndTime;
    private String mEmail;

    public ChatMessage(String mTitle, String mStartTime, String mEndTime, String mEmail) {



        this.mTitle = mTitle;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mEmail = mEmail;
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

    public String getEmail() { return  mEmail; }
}
