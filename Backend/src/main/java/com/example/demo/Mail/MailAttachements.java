package com.example.demo.Mail;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;

public class MailAttachements {
    private ArrayList<String>attachements;
    @JsonSetter("attachements")
    public void setAttachements(ArrayList<String> attachements) {
        this.attachements = attachements;
    }

    public ArrayList<String> getAttachements() {
        return this.attachements;
    }
    public void addAttachment(String attachment){
        this.attachements.add(attachment);
    }
}
