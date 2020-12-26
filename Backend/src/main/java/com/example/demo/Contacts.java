package com.example.demo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Contacts {
	@JsonProperty
	private String contact;
	@JsonProperty
	private ArrayList<String> names;
	
	private String Date;
	public String getDate() {
		return Date;
	}
	@JsonSetter
	public void setDate(String date) {
		Date = date;
	}
	public String getContact() {
		return contact;
	}
	@JsonSetter
	public void setContact(String contact) {
		this.contact = contact;
	}
	

	public ArrayList<String> getNames() {
		return names;
	}
	@JsonSetter("Email_Adresses")
	public void setNames(ArrayList<String> names) {
		this.names = names;
	}

}
