package com.example.demo;

import com.example.demo.Mail.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    MailID mailID = new MailID();
    @Test
    void mailSent() throws IOException {
        Service test = new Service();
        Mail  mail  = new Mail();
        MailHeader mailHeader=new MailHeader();
        MessageBody  messageBody = new MessageBody();
        MailAttachements mailAttachements = new MailAttachements();

        mailHeader.setPriority("high");
        mailHeader.setSubject("test");
        mailHeader.setSender("ahmedyasser882000@mail.com");
        ArrayList<String> receivers = new ArrayList<>();
        receivers.add("elgazzar@mail.com");
        mailHeader.setReceivers(receivers);
        messageBody.setBody("lorem lorem lorem lorem");
        mail.setMailID(mailID);
        mail.setMailAttachements(mailAttachements);
        mail.setMailHeader(mailHeader);
        mail.setMessageBody(messageBody);
        MultipartFile []files = null;
        assertTrue(test.mailSent(mail,files));// the sender and receiver both found on system
        // if  a user in receivers is not found in system it will return false
        mail.getMailHeader().getReceivers().add("notfoundinsystem");
        assertFalse(test.mailSent(mail,files));



    }


    @Test
    void saveDraftAndDelete() throws IOException {
        Service test = new Service();
        Mail  mail  = new Mail();
        MailHeader mailHeader=new MailHeader();
        MessageBody  messageBody = new MessageBody();
        MailAttachements mailAttachements = new MailAttachements();

        mailHeader.setPriority("high");
        mailHeader.setSubject("test");
        mailHeader.setSender("ahmedyasser882000@gmail.com");
        ArrayList<String> receivers = new ArrayList<>();
        receivers.add("elgazzar@yahoo.com");
        mailHeader.setReceivers(receivers);
        messageBody.setBody("lorem lorem lorem lorem");
        mail.setMailID(mailID);
        mail.setMailAttachements(mailAttachements);
        mail.setMailHeader(mailHeader);
        mail.setMessageBody(messageBody);
        MultipartFile []files = null;
        // this line is responsible to set date in MailId as the same name of created file in Mail
        String date = System.currentTimeMillis()+"";
        mailID.setDateAsId(date);
        System.out.println(mailID.getDateAsId());
        // returns true if mail is saved in draft
        assertTrue(test.saveDraft(mail,files));
        mailID.setUserID("ahmedyasser882000@gmail.com");
        mailID.setSourceID("draft");
        // the created folder in draft is deleted immediately using this method
        assertTrue(test.deleteMail(mailID));
    }
    @Test
    void SaveDraft() throws IOException {
        Service test = new Service();
        Mail  mail  = new Mail();
        MailHeader mailHeader=new MailHeader();
        MessageBody  messageBody = new MessageBody();
        MailAttachements mailAttachements = new MailAttachements();

        mailHeader.setPriority("high");
        mailHeader.setSubject("test");
        mailHeader.setSender("ahmedyasser882000@gmail.com");
        ArrayList<String> receivers = new ArrayList<>();
        receivers.add("elgazzar@yahoo.com");
        mailHeader.setReceivers(receivers);
        messageBody.setBody("lorem lorem lorem lorem");
        mail.setMailID(mailID);
        mail.setMailAttachements(mailAttachements);
        mail.setMailHeader(mailHeader);
        mail.setMessageBody(messageBody);
        MultipartFile []files = null;
        String date = System.currentTimeMillis()+"";
        mailID.setDateAsId(date);
        System.out.println(mailID.getDateAsId());
        // returns true if mail is saved in draft
        assertTrue(test.saveDraft(mail,files));

    }

}