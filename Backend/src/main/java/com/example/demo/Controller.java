package com.example.demo;

import com.example.demo.Mail.Mail;
import com.example.demo.Mail.MailID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.el.EvaluationListener;
import java.io.IOException;

@CrossOrigin
@RestController
public class Controller {
    // the singleton
    SystemAdmin obj = SystemAdmin.getSystemInstance();
    Service obj1 = new Service();
    ProxyLayer obj2= new ProxyLayer();
	private boolean mailSent;
    //when a mail send it is parsed by HTTP converters
    @PostMapping(value = "/sendMail",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public boolean sendMail (@RequestParam("mail") String mail , @RequestParam(required = false, name = "file")MultipartFile []file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        Mail mailObj =mapper.readValue(mail,Mail.class);
        Service service = new Service();
        
        return  service.mailSent(mailObj,file);
    }

    @PostMapping(value = "/downloadAttachment",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public boolean downloadAttachment(@RequestParam("mailId") String mailId,@RequestParam("fileName") String fileName){
        ObjectMapper mapper =new ObjectMapper();
        Service service = new Service();
        try{
        MailID mailIDObj =mapper.readValue(mailId,MailID.class);
        service.downloadAttachment(mailIDObj,fileName);
        }catch (Exception e){
            System.out.println("error happened in download attachments ");
            return false;
        }
        return  true;
    }
    @PostMapping(value = "/saveDraft",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public boolean saveDraft(@RequestParam("mail") String mail,@RequestParam(required = false, name = "file")MultipartFile []file){
        ObjectMapper mapper = new ObjectMapper();
        Service service = new Service();
        try {
            Mail mailObj =mapper.readValue(mail,Mail.class);
            service.saveDraft(mailObj,file);
        }catch (Exception e){
            return false;
        }
        return  true;
    }
    @PostMapping(value = "/createDefaultUsers")
        public boolean CreateDefaultUsers(){
            obj.IntializeSystem();
            return true;
    }

    @PostMapping(value = "/autoDelete",consumes = MediaType.APPLICATION_JSON_VALUE)
        public boolean autoDelete(@RequestBody User user){
            System.out.println("I entered broo");
            Service service = new Service();
            service.autoDelete(user);
        return true;
    }

    //Create a user when data is sent from frontend

    @PostMapping(value = "/SignUp",consumes = "application/json")
    public boolean SignUP(@RequestBody User user) throws IOException {
        
    	return obj.createUser(user);
    }
    @PostMapping(value="/signIn",consumes = "application/json")
    public boolean SignIn(@RequestBody User user) throws JsonProcessingException {

        ObjectMapper test = new ObjectMapper();
        System.out.println(test.writeValueAsString(user));
        return obj.checkSignIn(user);
    }
    
    @PostMapping(value = "/delete",consumes = "application/json")
    public boolean deleteMail(@RequestBody MailID mailID)  {
       try {
           Service test = new Service();

        test.deleteMail(mailID);
       } catch (Exception e){
           return false;
       }
        return true;
    }
    
    
  //-------------CONTACTS METHODS-------------------------------------------------------------------------------------------------  
    @GetMapping(value = "/addContacts")
    public String addContacts(@RequestParam String user,@RequestParam String contact ) throws IOException, org.json.simple.parser.ParseException{
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	return obj.addContact(user,contact);
    }
    
    @GetMapping(value = "/editContacts")//-----New Added-----------
    public String editContacts(@RequestParam String user,@RequestParam int pageNum,@RequestParam String contact,@RequestParam String newContact ) throws IOException, org.json.simple.parser.ParseException{
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	return obj.editContact(user, pageNum, contact, newContact);
    }
    
    @GetMapping(value = "/delContacts")
    public boolean DelContacts(@RequestParam String user,@RequestParam String contact ) throws IOException, org.json.simple.parser.ParseException{
    	if(!obj2.CheckUserAccesability(user)) return false;
    	user=obj1.getFolderName(user);
    	return obj.delContact(user,contact);
    }
    
    @GetMapping(value = "/loadContacts")
    public String LoadContacts(@RequestParam String user,@RequestParam int pageNum) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(obj.loadContact(user,pageNum));
    	return jsonString;
    }
    
    @GetMapping(value = "/SortCon")
    public String SortCon(@RequestParam String user,@RequestParam String sortType,@RequestParam int pageNum,@RequestParam boolean reversed) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonString = mapper.writeValueAsString(obj.SortContact(user, sortType, pageNum, reversed));
    	   
        System.out.println(jsonString);
        
    	return jsonString;
    }
    
    @GetMapping(value = "/SearchCon")
    public String SearchCon(@RequestParam String user,@RequestParam String conToSearch,@RequestParam int pageNum) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
        user=obj1.getFolderName(user);
        System.out.print(pageNum);
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonString = mapper.writeValueAsString(obj.SearchContact(user, conToSearch, pageNum));
    	   
        System.out.println(jsonString);
        
    	return jsonString;
    }
     
    @GetMapping(value = "/addNames")
    public boolean addNames(@RequestParam String user,@RequestParam String contact ,@RequestParam String name) throws IOException, org.json.simple.parser.ParseException{
    	if(!obj2.CheckUserAccesability(name)) return false;
    	if(!obj.checkUsedUsername(user)) return false ;
    	user=obj1.getFolderName(user);
    	return obj.addName(user,contact, name);
    }
    
    @GetMapping(value = "/delNames")
    public boolean delNames(@RequestParam String user,@RequestParam String contact ,@RequestParam String name) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return false;
    	user=obj1.getFolderName(user);
    	return  obj.delName(user,contact,name);
    }
    
    @GetMapping(value = "/loadNames")
    public String loadNames(@RequestParam String user,@RequestParam String contact) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(obj.readNames(user,contact));
    	return jsonString;
    }
 //------------------------------------------------------------------------------------------------------------------------------
    
    @GetMapping(value = "/loadMails")
    public String LoadMails(@RequestParam(name="user") String user,@RequestParam(name = "folder") String folder,@RequestParam(name = "pageNum") int pageNum ) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(obj.loadMails(user,folder,pageNum));
        System.out.println(jsonString);
        
    	return jsonString;
    }
   //---------------------------------------------------FILTER & Search METHODS-------------------------------------------------------- 

    
//    @GetMapping(value = "/filterSubject")
//    public String filterSubject(@RequestParam String user,@RequestParam String folder,@RequestParam String filter) throws IOException, org.json.simple.parser.ParseException {
//    	if(!obj2.CheckUserAccesability(user)) return null;
//    	user=obj1.getFolderName(user);
//    	ObjectMapper mapper = new ObjectMapper();
//        String jsonString = mapper.writeValueAsString(obj.filterSubject(user, folder, filter));
//        System.out.println(jsonString);
//        
//    	return jsonString;
//    }
//    
//    @GetMapping(value = "/filterSender")
//    public String filterSender(@RequestParam String user,@RequestParam String folder,@RequestParam String filter) throws IOException, org.json.simple.parser.ParseException {
//    	if(!obj2.CheckUserAccesability(user)) return null;
//    	user=obj1.getFolderName(user);
//    	ObjectMapper mapper = new ObjectMapper();
//        String jsonString = mapper.writeValueAsString(obj.filterSender(user, folder, filter));
//        System.out.println(jsonString);
//        
//    	return jsonString;
//    }
    
    @GetMapping(value = "/Search")
    public String Search(@RequestParam String user,@RequestParam String searchFor,@RequestParam String folder,@RequestParam int pageNum,@RequestParam String element) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonString;
    	switch(searchFor){
    		case "Subject":
    			 jsonString = mapper.writeValueAsString(obj.filterSubject(user, folder, element,pageNum));
    			break;
    		case "Sender":
    			 jsonString = mapper.writeValueAsString(obj.filterSender(user, folder, element,pageNum));
    			break;
    		case "Date" :
    			 jsonString = mapper.writeValueAsString(obj.filterDate(user, folder, element,pageNum));
    			break;
    		case "Priority" :
    			jsonString = mapper.writeValueAsString(obj.filterPriority(user, folder, element,pageNum));
    			break;
    		default :
    			jsonString = mapper.writeValueAsString(obj.filterReciver(user, folder, element,pageNum));
    	}
        
        System.out.println(jsonString);
        
    	return jsonString;
    }
    
    
    
    /*FirstFilter ->>> Subject
     * SecondFilter ->>>> Sender
     */
    @GetMapping(value = "/Filter")
    public String filter(@RequestParam String user,@RequestParam String filterType,@RequestParam String folder,@RequestParam int pageNum,@RequestParam String firstFilter,@RequestParam String secondFilter) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonString;
    	switch(filterType){
    		case "Subject":
    			 jsonString = mapper.writeValueAsString(obj.filterSubject(user, folder, firstFilter,pageNum));
    			break;
    		case "Sender":
    			 jsonString = mapper.writeValueAsString(obj.filterSender(user, folder, firstFilter,pageNum));
    			break;
    		default :
    			jsonString = mapper.writeValueAsString(obj.filterSendAndSub(user, folder, firstFilter, secondFilter,pageNum) );
    	}
        
    	return jsonString;
    }
   // ---------------------------------------------------FILTER METHODS--------------------------------------------------------
    
    @GetMapping(value = "/Sort")
    public String Sort(@RequestParam String user,@RequestParam String sortType,@RequestParam String folder,@RequestParam int pageNum,@RequestParam boolean reversed) throws IOException, org.json.simple.parser.ParseException {
    	if(!obj2.CheckUserAccesability(user)) return null;
    	user=obj1.getFolderName(user);
    	ObjectMapper mapper = new ObjectMapper();
    	String jsonString = mapper.writeValueAsString(obj.Sort(user, folder, sortType, pageNum, reversed));
    	   
        System.out.println(jsonString);
        
    	return jsonString;
    }
    
    
}
