package com.example.demo.Mail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;

public class MailHeader {
    private String Sender;
    private ArrayList<String> Receivers;
    private String Subject;
    private String Date;
    private String Priority;
   @JsonSetter("Sender")
    public void setSender(String sender){
        this.Sender=sender;
    }
    @JsonSetter("Receivers")
    public void setReceivers(ArrayList<String> receivers){
        this.Receivers = receivers;
    }
    @JsonSetter("Subject")
    public void setSubject(String subject){
        this.Subject=subject;

    }
    @JsonSetter("date")
    public void setDate(String date){
        this.Date=date;
    }
    @JsonSetter("Priority")
    public void setPriority(String priority){
        this.Priority=priority;
    }
    public String getSender(){
        return    this.Sender;
    }
    public ArrayList<String> getReceivers(){
        return this.Receivers;
    }
    public String getSubject(){
        return this.Subject;
    }
    public String getDate(){
        return this.Date;
    }
    public String getPriority(){
        return this.Priority;
    }




}
