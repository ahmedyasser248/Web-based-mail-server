package com.example.demo.Mail;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class Mail {
    private MailHeader mailHeader;
    private MessageBody messageBody;
    private MailAttachements mailAttachements;
    private MailID mailID;
    @JsonSetter("mailAttachements")
    public void setMailAttachements(MailAttachements mailAttachements) {
        this.mailAttachements = mailAttachements;
    }
    @JsonSetter("mailHeader")
    public void setMailHeader(MailHeader mailHeader) {
        this.mailHeader = mailHeader;
    }
    @JsonSetter("messageBody")
    public void setMessageBody(MessageBody messageBody) {
        this.messageBody = messageBody;
    }
    @JsonSetter("mailID")
    public void setMailID(MailID mailID) {
        this.mailID = mailID;
    }

    public MailAttachements getMailAttachements() {
        return  this.mailAttachements;
    }

    public MailHeader getMailHeader() {
        return this.mailHeader;
    }

    public MessageBody getMessageBody() {
        return this.messageBody;
    }
    public MailID getMailID(){
        return this.mailID;
    }
    public static void main(String [] args) throws JsonProcessingException {
        Mail obj = new Mail();
        MailHeader obj2 = new MailHeader();
        MailAttachements obj3 = new MailAttachements();
        MessageBody obj4 =new MessageBody();
        ArrayList<String> re= new ArrayList<>();
        re.add("marwan");
        re.add("gazzar");
        re.add("jimmy");
        obj2.setDate("16/12/2020");
        obj2.setPriority("2");
        obj2.setReceivers(re);
        obj2.setSender("ahmedyasser882000@gmail.com");
        obj2.setSubject("testing");
        ObjectMapper org = new ObjectMapper();
        System.out.println(org.writeValueAsString(obj2));
        obj3.setAttachements(re);
        obj4.setBody("is htsht3'l isa");
        obj.setMailAttachements(obj3);
        obj.setMailHeader(obj2);
        obj.setMessageBody(obj4);
        System.out.println(org.writeValueAsString(obj));
    }
}