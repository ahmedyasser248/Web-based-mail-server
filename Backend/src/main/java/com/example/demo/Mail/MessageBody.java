package com.example.demo.Mail;

import com.fasterxml.jackson.annotation.JsonSetter;

public class MessageBody {
    private String Body;

    @JsonSetter("Body")
    public void setBody(String body) {
        this.Body = body;
    }

    public String getBody() {
        return this.Body;
    }
}
