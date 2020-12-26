package com.example.demo.Mail;

import com.fasterxml.jackson.annotation.JsonSetter;

public class MailID {
    private String userID;
    private String sourceID;
    private String dateAsID;

    public String getDateAsId() {
        return this.dateAsID;
    }

    public String getSourceID() {
        return this.sourceID;
    }
    public String getUserID() {
        return this.userID;
    }
    @JsonSetter("dateAsID")
    public void setDateAsId(String dateAsId) {
        this.dateAsID = dateAsId;
    }
    @JsonSetter("sourceID")
    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }
    @JsonSetter("userID")
    public void setUserID(String userID) {
        this.userID = userID;
    }
}