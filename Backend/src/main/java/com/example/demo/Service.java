package com.example.demo;
import com.example.demo.FileFactory.createMailFile;
import com.example.demo.Mail.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * this class is used to save the mails and contacts
 * */
public class Service  {
    /**
     * @param mail the mail sent by user
     * @param multiPartFile[]  is array of contain the attachments;
     * */
    final String fileSeparator=System.getProperty("file.separator");

    public boolean mailSent(Mail mail, MultipartFile[] multipartFiles) throws IOException {
        //method is used to delete draft folder if mail sent from draft
        boolean MailIsDraft = false;
        String pathToDraft="";
        ArrayList<File> filesFromDraft = new ArrayList<>();
        if(isMailFromDraft(mail)){
            MailIsDraft=true;
            pathToDraft="System"+fileSeparator+getFolderName(mail.getMailHeader().getSender())+fileSeparator+"draft"+fileSeparator+mail.getMailHeader().getDate();
            filesFromDraft=getFilesFromDraft(pathToDraft,mail);
        }
        mail.getMailHeader().setDate(Long.toString(System.currentTimeMillis()));
        String userFolderName;
        String pathStr;
        ArrayList<String> attachments =new ArrayList<>();
        if(multipartFiles!=null) {
            for (MultipartFile multipartFile : multipartFiles) {
                attachments.add(multipartFile.getOriginalFilename());
                System.out.println(multipartFile.getOriginalFilename());
            }

            mail.getMailAttachements().setAttachements(attachments);
            if(filesFromDraft.size()!=0){
                for (File file : filesFromDraft){
                    mail.getMailAttachements().addAttachment(file.getName());
                }
            }
        }
        // obj handle writing and creating mail file that implements create file method
        createMailFile test =new createMailFile();
        for(String receiver : mail.getMailHeader().getReceivers()){
            String checkExistingReceiver = getFolderName(receiver);
            if(checkExistingReceiver.equals("Null")){
                return  false;
            }
        }
        // create the folder in inbox of all receivers
        for(String receiver:mail.getMailHeader().getReceivers()){
            userFolderName=getFolderName(receiver);
            // if user is not found in system  the method return false


            //setting mailId
            mail.getMailID().setUserID(receiver);
            mail.getMailID().setSourceID("inbox");
            mail.getMailID().setDateAsId(mail.getMailHeader().getDate());
            pathStr = "System" + fileSeparator + userFolderName + fileSeparator + "inbox" + fileSeparator + mail.getMailHeader().getDate();
            test.writeMAilInJsonFile(mail, pathStr);
            // transferring attachment files to mail folder
            if(multipartFiles!= null){
                for(MultipartFile file :multipartFiles){
                    MultiPartToFile(pathStr,file);
                }
            }
            if(filesFromDraft.size()!=0){
                for (File file :filesFromDraft){
                    File destinationFile = new File(pathStr+fileSeparator+file.getName());
                    destinationFile.createNewFile();
                    transferFile(file.getPath(),destinationFile.getPath());
                }
            }

        }
        mail.getMailID().setUserID(mail.getMailHeader().getSender());
        mail.getMailID().setSourceID("sent");
        mail.getMailID().setDateAsId(mail.getMailHeader().getDate());
        userFolderName=getFolderName(mail.getMailHeader().getSender());
        pathStr="System"+fileSeparator+userFolderName+fileSeparator+"sent"+fileSeparator+mail.getMailHeader().getDate();
        test.writeMAilInJsonFile(mail,pathStr);
        if(multipartFiles!=null) {
            for (MultipartFile file : multipartFiles) {
                MultiPartToFile(pathStr, file);
            }
            if(filesFromDraft.size()!=0){
                for (File file :filesFromDraft){
                    File destinationFile = new File(pathStr+fileSeparator+file.getName());
                    destinationFile.createNewFile();
                    transferFile(file.getPath(),destinationFile.getPath());
                }
            }
        }
        //if mail is from draft then clear folder in mail
        if(MailIsDraft){
            deleteDraftFolder(pathToDraft);
        }
        return  true;
    }
    public boolean saveDraft(Mail mail,MultipartFile []MultiPartFiles)throws IOException{
        ArrayList<String>attachment = new ArrayList<>();
        //this if condition to check if there is no attachments sent from front end or no .
        if (MultiPartFiles!=null) {
            for (MultipartFile multipartFile : MultiPartFiles) {
                attachment.add(multipartFile.getOriginalFilename());
            }
            mail.getMailAttachements().setAttachements(attachment);
        }
        // setting date as it is the name of folder
        mail.getMailHeader().setDate(Long.toString(System.currentTimeMillis()));
        //getting user folder
        String userFolderName=getFolderName(mail.getMailHeader().getSender());
        //updating the mailID object
        mail.getMailID().setUserID(mail.getMailHeader().getSender());
        mail.getMailID().setSourceID("draft");
        mail.getMailID().setDateAsId(mail.getMailHeader().getDate());
        //creating the path
        String pathStr="System"+fileSeparator+userFolderName+fileSeparator+"draft"+fileSeparator+mail.getMailHeader().getDate();
        createMailFile obj = new createMailFile();
        //write the Mail in Json
        obj.writeMAilInJsonFile(mail,pathStr);
        //transferring the attachments
        if (MultiPartFiles != null) {
            for (MultipartFile file : MultiPartFiles) {
                MultiPartToFile(pathStr, file);
            }
        }
        return true;
    }

    public boolean deleteMail(MailID mailID) throws IOException {
        String userFolderName=getFolderName(mailID.getUserID());
        //source directory
        System.out.println(mailID.getDateAsId());
        String pathStrSource="System"+fileSeparator+userFolderName+fileSeparator+mailID.getSourceID()+fileSeparator+mailID.getDateAsId();

        //destination directory need to be created
        String pathStrDest="System"+fileSeparator+userFolderName+fileSeparator+"trash"+fileSeparator+mailID.getDateAsId();
        File sourceDirectory=new File(pathStrSource);
        sourceDirectory.mkdir();
        String [] files =sourceDirectory.list();
        if(mailID.getSourceID().equals("trash")){
            for(String file:files){
                File currentFile =new File(sourceDirectory.getPath(),file);
                currentFile.delete();
            }
            System.out.println( sourceDirectory.delete());
            return true;
        }
        File destinationDirectory = new File(pathStrDest);
        destinationDirectory.mkdir();
        // directory in trash is created
        // now I read the original mail to update it's Id and then save it in trash
        ObjectMapper mapper = new ObjectMapper();
        File mailFile = new File(pathStrSource+fileSeparator+"Mail.json");

        Mail mailObj= mapper.readValue(mailFile,Mail.class);
        mailObj.getMailID().setSourceID("trash");
        System.out.println(mapper.writeValueAsString(mailObj));
        File newMailFile=new File(pathStrDest+fileSeparator+"Mail.json");
        System.out.println(newMailFile.createNewFile());
        mapper.writeValue(newMailFile,mailObj);

        // for loop to move the attachments using byte array to support all files
        for (String file :files){
            if(!file.equals("Mail.json")) {
                transferFile(pathStrSource + fileSeparator + file, pathStrDest + fileSeparator + file);
            }
        }
        // delete from source
        for(String file:files){
            File currentFile =new File(sourceDirectory.getPath(),file);
            System.out.println(currentFile.delete());
        }
        sourceDirectory.delete();
        return true;
    }
    /**
     * method to get folderName for creating files in it
     * */
    public String getFolderName(String userName){
        File directory =new File("System");
        String []files=directory.list();
        for(String file :files){
            String [] temp=file.split("-",2);
            if(temp[0].equals(userName)){
                return file;
            }
        }
        return  "Null";
    }
    /**
     * @param user is user who I check his folders for emails older than 30 days to be deleted
     * */
    public void autoDelete(User user){
        String userFolderName= getFolderName(user.getEmail());
        String pathToUserFolder="System"+fileSeparator+userFolderName+fileSeparator;
        // check trash
        checkForAutoDelete(pathToUserFolder,"trash");
    }
    public boolean checkForAutoDelete(String path,String folder){
        long currentTime=System.currentTimeMillis();
        long thirtyDaysInMilliSec= 30L *24*60*60*1000;
        String DirectoryPath = path+folder;
        File directoryToBeChecked= new File(DirectoryPath);
        String[] files =directoryToBeChecked.list();
        if(files.length==0){
            return false;
        }
        for (String file :files){
            File currentFile =new File(directoryToBeChecked.getPath(),file);
            long dateOFMail=Long.parseLong(file);
            if(currentTime-dateOFMail>thirtyDaysInMilliSec){
                currentFile.delete();
            }
        }
        return true;
    }
    /**
     * @param source path of source file
     * @param destination path of destination file
     * will be used to transfer attachements files
     * */
    public void transferFile(String source,String destination){
        File sourceFile = new File(source);
        File destinationFile = new File(destination);
        FileOutputStream outputStream= null;
        FileInputStream inputStream=null;
        try {
            outputStream = new FileOutputStream(destinationFile);
            inputStream=new FileInputStream(sourceFile);
            byte []buffer = new byte[4096];
            int bytesRead = -1;
            while((bytesRead=inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void downloadAttachment(MailID mailID , String fileName){
        String folderName=getFolderName(mailID.getUserID());
        String sourcePath="System"+fileSeparator+folderName+fileSeparator+mailID.getSourceID()+fileSeparator+mailID.getDateAsId()+fileSeparator+fileName;
        String destinationPath=System.getProperty("user.home")+fileSeparator+"Downloads"+fileSeparator+fileName;
        transferFile(sourcePath,destinationPath);
    }
    public boolean isMailFromDraft(Mail mail){
        try{
            if(mail.getMailID().getSourceID()==null){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return  true;
    }
    public void deleteDraftFolder(String path){
        File folder =  new File(path);
        String [] files  = folder.list();
        if(files.length==0){
            return;
        }else{
            for (String file : files){
                File currentFile = new File(path,file);
                currentFile.delete();
            }
            folder.delete();
        }
    }
    /**
     * new method that takes the multipart file and create it in required folders
     * */
    public boolean MultiPartToFile(String pathToFolder,MultipartFile multipartFile){
        File file = new File(pathToFolder+fileSeparator+multipartFile.getOriginalFilename());
        OutputStream os = null;
        try{ os = new FileOutputStream(file);
            os.write(multipartFile.getBytes());
            os.close();
        }catch (Exception e){
            System.out.println("an Exception is thrown in MultiPartToFile ");
        }
        return true;
    }
    public ArrayList<File> getFilesFromDraft(String pathToDraft , Mail mail ){
        System.out.println("hello "+mail.getMailAttachements().getAttachements());
        ArrayList<File> files =  new ArrayList<>();
        if(mail.getMailAttachements().getAttachements()!=null){
            System.out.println("xxxxxxxxxxxxxxx");
            //attachments contain names of files In sent mail from draft
            ArrayList<String> attachments = mail.getMailAttachements().getAttachements();
            File draftFolder = new File(pathToDraft);
            String []filesInDraft  =  draftFolder.list();
            List<String> filesInDraft2 = Arrays.asList(filesInDraft);
            //checking for intersection
            for (String file :filesInDraft2){
                if(attachments.contains(file)){
                    File newFile = new File(pathToDraft+fileSeparator+file);
                    files.add(newFile);
                }
            }
        }
        return  files;
    }
    public static void main(String [] args) throws IOException {


        Mail mail = new Mail();
        MailHeader mailHeader= new MailHeader();
        MailAttachements mailAttachements = new MailAttachements();
        MessageBody messageBody = new MessageBody();



    }
}
