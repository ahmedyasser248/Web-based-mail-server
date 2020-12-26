package com.example.demo;


import com.example.demo.FileFactory.FileFactoryMethod;
import com.example.demo.Mail.Mail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Filter.CriteriaDate;
import Filter.CriteriaPriority;
import Filter.CriteriaReceivers;
import Filter.CriteriaSender;
import Filter.CriteriaSubject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * System class is responsiple to save new users
 * check if a user is already registered
 * Signleton design pattern is used with this class
 * It is only  used for sign up adn sign in
 */
public class SystemAdmin implements FileFactoryMethod {
    private static SystemAdmin system ;
    private SystemAdmin(){
    
    }
    String fileSeparator =System.getProperty("file.separator");
    /**
     * getting same instancewhen this method is called
     */
    public static SystemAdmin getSystemInstance(){
        if(SystemAdmin.system==null){
            SystemAdmin.system=new SystemAdmin();
        }

        return SystemAdmin.system;
    }
    /**
     * This method is used when system is used for first time to create
     * system directory and also creates the
     */
    public boolean IntializeSystem()  {
        
        File directory = new File("System");
        //no problem if directory is already created
        boolean bool=directory.mkdir();
        User ahmed = new User();
        ahmed.setEmail("ahmedyasser882000@gmail.com");

        ahmed.setFirstName("Ahmed");
        ahmed.setLastName("Yasser");
        ahmed.setPassword("4444");
        User elgazzar =new User();
        elgazzar.setEmail("elgazzar@yahoo.com");
        elgazzar.setPassword("33333");
        elgazzar.setFirstName("Mohammed");
        elgazzar.setLastName("elgazzarf");
        try{
        	createUser(ahmed);
        	createUser(elgazzar);
        }catch (Exception e){
			e.printStackTrace();
		}
        return true;
    }
    public boolean createUser(User user) throws IOException {
      
        File file = new File("System"+fileSeparator+user.getEmail()+"-"+user.getPassword());
        if(checkUsedUsername(user.getEmail())){
            return false;
        }else{
            file.mkdir();
            File informationFile = createFile(file.getPath());
            writeUserAsJsonInFile(informationFile,user);
            createNeededDirectories(file);

            return true;
        }

    }/**
     *method is used to write user Information as json in json file called information
     */
    public void writeUserAsJsonInFile(File file,User user) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file,user);
    }
    public void createNeededDirectories(File file){
      
        String path=file.getPath()+fileSeparator;
        File inboxDirectory= new File(path+"inbox");
        inboxDirectory.mkdir();
        File trashDirectory = new File(path+"trash");
        trashDirectory.mkdir();
        File ContactsFolder = new File(path+"contacts");
        ContactsFolder.mkdir();
        File sentFolder = new File(path+"sent");
        sentFolder.mkdir();
        File draftFolder = new File(path+"draft");
        draftFolder.mkdir();

    }
    /**
     *
     * @param user
     * @return a boolean depending on if user is in system already or not.
     */
    public boolean checkSignIn(User user){
        File system = new File("System");
        String [] files= system.list();
        //no users are created
        if(files.length==0){
            return  false;
        }
        for ( String file : files){
            String []userNameAndPassWord=file.split("-",2);
            if(userNameAndPassWord[0].equals(user.getEmail())&& userNameAndPassWord[1].equals(user.getPassword())){
                return true;
            }
        }

        return false;
    }
    /**
     *
     * @param userName
     * @return a boolean to check if username is repeated during sign up
     */
    public boolean checkUsedUsername(String userName){
        File system = new File("System");
        String [] files= system.list();
        //no users are created
        if(files.length==0){
            return  false;
        }
        for ( String file : files){
            String []userNameAndPassWord=file.split("-",2);
            if(userNameAndPassWord[0].equals(userName)){
                return true;
            }
        }
        return false;
    }
    
    
    //-------------------CONTACTS METHODS---------------------------------------------------------------------------------
    
    public boolean isSubstring(
            String s1, String s2)
        {
            int M = s1.length();
            int N = s2.length();         
            for (int i = 0; i <= N - M; i++) {
                int j;
     

                for (j = 0; j < M; j++)
                    if (s2.charAt(i + j)
                        != s1.charAt(j))
                        break;
     
                if (j == M)
                    return true;
            }
     
            return false;
        }
    
    public ArrayList<Contacts> SearchContact(String user,String conToSearch ,int pageNum) throws IOException, ParseException{
		
    	ArrayList<Contacts> Con=new ArrayList<Contacts>();
    	ArrayList<Contacts> Res=new ArrayList<Contacts>();
    	Con=this.loadContact(user, -1);	
    		for(Contacts contact: Con) {
    			
    			if(this.isSubstring(conToSearch.toLowerCase(),contact.getContact().toLowerCase())) {
    				Res.add(contact);
    			}
    		}
    		return this.SetPropCon(Res, pageNum);
    			
    	}
    	
    
    
    
    public ArrayList<Contacts> SortContact(String user , String sortType , int pageNum ,boolean reversed) throws IOException, ParseException{
		
   	 ArrayList<Contacts> Con=new ArrayList<Contacts>();
   	 Con=this.loadContact(user,-1);
   	 int size=Con.size();
   	 for(int i = 0; i<size; i++)   
   	 {  
   		 for (int j = i+1; j<size; j++)   
   		 {  
   			 
   			 
   			 if(this.SortHelperCon(Con.get(i), sortType).compareTo(this.SortHelperCon(Con.get(j), sortType))>0)   
   			 {  
   			
   			 Contacts temp = Con.get(i);  
   			 Con.set(i, Con.get(j)) ; 
   			 Con.set(j, temp) ;   
   			 }  
   		 }  
   	 }  
   	 if(reversed) this.ReverseCon(Con);
   	 return this.SetPropCon(Con, pageNum);
    	
    }
    
    public ArrayList<Contacts> ReverseCon(ArrayList<Contacts> Con){
    	
   	 int start=0;
   	 int end=Con.size()-1;
   	 while(start<end) {
   		 Contacts temp = Con.get(start);
   		 Con.set(start, Con.get(end));
   		 Con.set(end, temp);
   		 start++;
   		 end--;
   		 
   	 }
   	 
   	 return Con;
   	 
    }
    public String SortHelperCon(Contacts contact,String sortType) {
		
    	switch(sortType) {
    	case "Date":
    		return contact.getDate();
		default :
	 		String sub=contact.getContact();
	 		return sub.toLowerCase();
			
			
    	}
    	
    }
    
    
    public boolean checkNames(String user,String name,ArrayList<String> names) throws IOException, ParseException {
   	
    	boolean finished = false;
    	if(names.size()==0) {
    		finished=true;
    	}
    	
    	while(!finished) {
    	for(String name1:names) {
    		if(name1.equals(name)) {
    			return false;
    		}else {
    			finished =true;
    		}
    		
    	}
    	}
    	return true;
    	    	
    }
    
    public String checkContact(String user,String contact) throws IOException, ParseException {
    	File folder = new File("System"+fileSeparator+user+fileSeparator+"contacts");
    	File[] listOfFiles = folder.listFiles();
    	String mainContact=contact;

    	
    	boolean finished = false;
    	if(listOfFiles.length==0) {
    		finished=true;
    		
    	}
    	int counter =0;
    	while(!finished) {
    	for(File name1:listOfFiles) {
    		if(name1.getName().equals(contact+".json")) {
    			if(counter==0) {
    				contact=contact+" "+String.valueOf(counter);
    			}else {
    				contact=mainContact+" "+String.valueOf(counter);
    			}
    			finished=false;
    			counter++;
    			break;
    		}else {
    			finished=true;
    		}
    	}
    	}
    	return contact;
    	
    	
    }
    
    public  ArrayList<Contacts> loadContact(String user,int pageNum) throws IOException, ParseException {
    	File folder = new File("System"+fileSeparator+user+fileSeparator+"contacts");
    	File[] listOfFiles = folder.listFiles();
    	if(listOfFiles.length==0)return null;
    	ArrayList<Contacts> contacts=new ArrayList<Contacts>();
    	for (int i = 0; i < listOfFiles.length; i++) {
    		  if (listOfFiles[i].isFile()) { 
    		    contacts.add(this.readNames(user,listOfFiles[i].getName().replace(".json","")));
    		  }
    	}
    	if(pageNum==-1) return contacts;
    	return this.SetPropCon(contacts, pageNum);
    	}
    
    public  ArrayList<String> loadContactNames(String user) {
    	File folder = new File("System"+fileSeparator+user+fileSeparator+"contacts");
    	File[] listOfFiles = folder.listFiles();
    	if(listOfFiles.length==0)return null;
    	ArrayList<String> contacts=new ArrayList<String>();
    	for (int i = 0; i < listOfFiles.length; i++) {
    		  if (listOfFiles[i].isFile()) {
    		    contacts.add(listOfFiles[i].getName().replace(".json",""));
    		  }
    	}
    	return contacts;
    	}
    
    public  Boolean delContact(String user,String contact) {
    	ArrayList<String> contacts=new ArrayList<String>();
    	contacts=this.loadContactNames(user);
    	if(contacts.size()==0)return false;
    	if(!contacts.contains(contact))return false;
    	File file= new File("System"+fileSeparator+user+fileSeparator+"contacts"+fileSeparator+contact+".json");  
    	file.delete();
		return true;
    	}
    
    
    public String editContact(String user ,int pageNum, String contact , String newContact) throws ParseException, IOException {
		if(contact.equals(this.checkContact(user, contact))) return null;
    	ArrayList<Contacts> contacts = new ArrayList<Contacts>();
    	try {
			contacts=this.loadContact(user, pageNum);
		 
    	Contacts newCon= new Contacts();
    	for (int i=0 ; i < contacts.size() ; i++) {
    		if(contacts.get(i).getContact().equals(contact)) {
    			
    			newCon=contacts.get(i);
    			break;
    		}
    	}
    	this.delContact(user, contact);
    	
		newContact=this.checkContact(user, newContact);
    	File file = new File("System"+fileSeparator+user+fileSeparator+"contacts"+fileSeparator+newContact+".json");
    	file.createNewFile();
    	FileWriter myWriter = new FileWriter(file);
    	
    	
    	newCon.setContact(newContact);
    	newCon.setDate(String.valueOf(System.currentTimeMillis()));
    	ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(newCon);
    	 myWriter.write(jsonString);
         myWriter.close();
    	}
         catch (IOException e) {
        	 return null;	
 		}
    return newContact;	
    }
    
   
    public String addContact(String user,String contact) throws  ParseException {
    	try {
			contact=this.checkContact(user, contact);
	    	File file = new File("System"+fileSeparator+user+fileSeparator+"contacts"+fileSeparator+contact+".json");
	    	file.createNewFile();
		} catch (IOException e) {
			return null;
		} 
		return contact;

    }
    
    public boolean addName(String user,String contact,String name) throws  ParseException {
		ArrayList<String> names;
		
		
    	File file = new File("System"+fileSeparator+user+fileSeparator+"contacts"+fileSeparator+contact+".json");

		try {
			
			names=this.readNames(user,contact).getNames();
			if(names==null)names=new ArrayList<String>();
			if(this.checkNames(user, name, names)) {
				names.add(name);
			}else {
				return false;
			}
	    	FileWriter myWriter = new FileWriter(file);
	    	
	    	Contacts Names=new Contacts();
	    	Names.setNames(names);
	    	Names.setContact(contact);
	    	Names.setDate(String.valueOf(System.currentTimeMillis()));
	    	ObjectMapper mapper = new ObjectMapper();
	        String jsonString = mapper.writeValueAsString(Names);
	    	 myWriter.write(jsonString);
	         myWriter.close();
		} catch (IOException e) {
			return false;
		}

    	return true;
    	
    }
    
    public boolean delName(String user,String contact,String name) throws  ParseException {
		ArrayList<String> names;//=new ArrayList<String>();

		
		File file = new File("System"+fileSeparator+user+fileSeparator+"contacts"+fileSeparator+contact+".json");
    	try {
    		Contacts temp=this.readNames(user,contact);
    		names=temp.getNames();
    		if(names==null)names=new ArrayList<String>();
    		if(!names.contains(name)) return false;
    		names.remove(name);
        	FileWriter myWriter = new FileWriter(file);
        	
        	Contacts Names=new Contacts();
        	Names.setNames(names);
        	Names.setContact(temp.getContact());
        	Names.setDate(temp.getDate());
        	ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(Names);
        	 myWriter.write(jsonString);
             myWriter.close();
        	return true;
		} catch (IOException e) {
			return false;
		}

    	
    }
    
    public Contacts readNames(String user,String contact) throws IOException, ParseException {
		Contacts Names=new Contacts();
		Names.setContact(contact);
		ObjectMapper mapper = new ObjectMapper();
			
 	      File myObj = new File("System"+fileSeparator+user+fileSeparator+"contacts"+fileSeparator+contact+".json");
 	      FileReader reader =new FileReader(myObj);
 	     
 	      
 	     JSONParser jsonP= new JSONParser();
 	     String names = null;
		try {
			names = jsonP.parse(reader).toString();
			Names=mapper.readValue(names, Contacts.class);
		} catch (IOException e) {
			
			
		} catch (ParseException e) {
		}
 	     
 	     reader.close();
 	     System.out.println(Names.getNames());

	return Names;
	
}
  //-------------------------------------------------------------------------------------------------------------------------------------
 
 public ArrayList<Mail> SetPropNum(ArrayList<Mail> mails, int pageNum){
	
	 ArrayList<Mail> returned=new ArrayList<Mail>();
	 int end;
	 if(pageNum*10 > mails.size()) {
		 end=mails.size();
	 }else {
		 end=pageNum*10;
	 }
	 for(int i=(pageNum*10)-10 ; i < end ; i++) {
		 returned.add(mails.get(i));
	 }
	 
	 return returned;
	 
 }
 public ArrayList<Contacts> SetPropCon(ArrayList<Contacts> Con, int pageNum){
		
	 ArrayList<Contacts> returned=new ArrayList<Contacts>();
	 int end;
	 if(pageNum*10 > Con.size()) {
		 end=Con.size();
	 }else {
		 end=pageNum*10;
	 }
	 for(int i=(pageNum*10)-10 ; i < end ; i++) {
		 returned.add(Con.get(i));
	 }
	 
	 return returned;
	 
 }
    
 public ArrayList<Mail> loadMails(String user,String folder,int pageNum) throws IOException, ParseException{
	
	 ArrayList<Mail> allMails=new ArrayList<Mail>();
	 ObjectMapper mapper = new ObjectMapper();
     JSONParser jsonP= new JSONParser();
     

 	File Folder = new File("System"+fileSeparator+user+fileSeparator+folder);
 	File[] listOfFiles = Folder.listFiles();
 	for (int i = 0; i < listOfFiles.length; i++) {
 		File file = new File("System"+fileSeparator+user+fileSeparator+folder+fileSeparator+listOfFiles[i].getName()+fileSeparator+"Mail.json");
 		 try {
			FileReader reader =new FileReader(file);
			
			String mails = jsonP.parse(reader).toString();
			allMails.add(mapper.readValue(mails, Mail.class));
			reader.close();
		} catch (FileNotFoundException e) {
			return null;
		}
 	}
 	
 	if(pageNum!=-1) allMails=this.SetPropNum(allMails, pageNum);
	 
	 
	 return allMails;
		
	}
 
 public String SortHelper(Mail mail ,String sortType) {
	
	 switch(sortType) {
	 	case "Date":
	 		return mail.getMailHeader().getDate();
	 	case "Subject":
	 		String sub=mail.getMailHeader().getSubject();
	 		return sub.toLowerCase();
	 	case "Priority":
	 		switch(mail.getMailHeader().getPriority()) {
	 		case "high":
	 			return "3";
	 		case "medium" :
				 return "2";
			case "Superhigh" :
	 			 return "4";	 
	 		default :
	 			return "1";
	 		}
	 	default :
	 		return mail.getMailHeader().getSender();
	
	 }
 }
 
 public ArrayList<Mail> Reverse(ArrayList<Mail> mails){
	
	 int start=0;
	 int end=mails.size()-1;
	 while(start<end) {
		 Mail temp = mails.get(start);
		 mails.set(start, mails.get(end));
		 mails.set(end, temp);
		 start++;
		 end--;
		 
	 }
	 
	 return mails;
	 
 }
 
 public ArrayList<Mail> Sort(String user , String folder , String sortType,int pageNum , boolean reversed) throws IOException, ParseException{
	 ArrayList<Mail> mails=new ArrayList<Mail>();
	 mails=this.loadMails(user, folder ,-1);
	 int size=mails.size();
	 for(int i = 0; i<size; i++)   
	 {  
		 for (int j = i+1; j<size; j++)   
		 {  
			 
			 
			 if(this.SortHelper(mails.get(i), sortType).compareTo(this.SortHelper(mails.get(j), sortType))>0)   
			 {  
			
			 Mail temp = mails.get(i);  
			 mails.set(i, mails.get(j)) ; 
			 mails.set(j, temp) ;   
			 }  
		 }  
	 }  
	 if(reversed) this.Reverse(mails);
	 return this.SetPropNum(mails, pageNum);
	 
 }
 
 
 //------------------------------------------------FILTER METHODS-----------------------------

 public ArrayList<Mail> filterPriority(String user,String folder,String filter,int pageNum) throws IOException, ParseException{
	 ArrayList<Mail> mails=new ArrayList<Mail>();
	 mails=this.loadMails(user, folder ,-1); 
	 CriteriaPriority filPrio= new CriteriaPriority();
	 mails=filPrio.meetCriteria(mails, filter);
	
	 return this.SetPropNum(mails, pageNum);
 	
 }
 
 public ArrayList<Mail> filterDate(String user,String folder,String filter,int pageNum) throws IOException, ParseException{
	 ArrayList<Mail> mails=new ArrayList<Mail>();
	 mails=this.loadMails(user, folder ,-1); 
	 CriteriaDate filDate= new CriteriaDate();
	 mails=filDate.meetCriteria(mails, filter);
	
	 return this.SetPropNum(mails, pageNum);
 	
 }
 
 public ArrayList<Mail> filterReciver(String user,String folder,String filter,int pageNum) throws IOException, ParseException{
	 ArrayList<Mail> mails=new ArrayList<Mail>();
	 mails=this.loadMails(user, folder ,-1); 
	 CriteriaReceivers filRec= new CriteriaReceivers();
	 mails=filRec.meetCriteria(mails, filter);
	
	 return this.SetPropNum(mails, pageNum);
 	
 }
 
 public ArrayList<Mail> filterSubject(String user,String folder,String filter,int pageNum) throws IOException, ParseException{
	 ArrayList<Mail> mails=new ArrayList<Mail>();
	 mails=this.loadMails(user, folder ,-1); 
	 CriteriaSubject filSub= new CriteriaSubject();
	 mails=filSub.meetCriteria(mails, filter);
	
	 return this.SetPropNum(mails, pageNum);
 	
 	
 }
 
 public ArrayList<Mail> filterSender(String user,String folder,String filter,int pageNum) throws IOException, ParseException{
	 ArrayList<Mail> mails=new ArrayList<Mail>();
	 mails=this.loadMails(user, folder ,-1); 
	 CriteriaSender filSub= new CriteriaSender();
	 mails=filSub.meetCriteria(mails, filter);
	
	 return this.SetPropNum(mails, pageNum);
 	 	
 }
 
 public ArrayList<Mail> filterSendAndSub(String user,String folder , String filter1,String filter2,int pageNum ) throws IOException, ParseException{
	 ArrayList<Mail> mails=new ArrayList<Mail>();
	 mails=this.loadMails(user, folder,-1); 
	 CriteriaSubject filSub= new CriteriaSubject();
	 CriteriaSender filSen= new CriteriaSender();
	 mails=filSub.meetCriteria(mails, filter1);
	 mails=filSen.meetCriteria(mails, filter2);
	
	 return this.SetPropNum(mails, pageNum);
 	 	
 }
 //------------------------------------------------------------------------------------------------
 
    @Override
    public File createFile(String path) throws IOException {
        
        path+=fileSeparator+"Information.json";
        System.out.println(path);
        File  informationFile = new File(path);
        informationFile.createNewFile();
        return informationFile ;
    }

    public static void  main(String[] args) throws IOException {
        SystemAdmin obj = SystemAdmin.getSystemInstance();
        obj.IntializeSystem();
    }


}
