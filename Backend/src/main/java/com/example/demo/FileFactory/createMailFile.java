package com.example.demo.FileFactory;

import com.example.demo.Mail.Mail;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class createMailFile implements FileFactoryMethod{
    final String fileSeparator=System.getProperty("file.separator");
   /*
   * create a mail by receiving path to desired folder
   * */
    @Override
    public File createFile(String path) throws IOException {
        String pathStr=path+fileSeparator+"Mail.json";
        File file = new File(pathStr);
        file.createNewFile();
        return file;
    }
    public boolean writeMAilInJsonFile(Mail mail,String path) throws IOException {
        File thisMailFolder = new File(path);
        thisMailFolder.mkdir();
        File file= createFile(path);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file,mail);
        return  true;
    }
}
