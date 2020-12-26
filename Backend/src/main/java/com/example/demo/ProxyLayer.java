package com.example.demo;

import java.io.File;
import java.util.ArrayList;

public class ProxyLayer {

	public boolean CheckUserAccesability(String user) {
		
		File directory =new File("System");
	        String []files=directory.list();
	        System.out.println(files.length);
	        for(String file :files){
	            String [] temp=file.split("-",2);
	            if(temp[0].equals(user)){
	                return true;
	            }
	        }		
		return false;
		
	}
}