package com.example.demo.FileFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
/**
 * used to be implemented by the classes needs to create files by different file types
 * */
public interface FileFactoryMethod {
    public File createFile(String path) throws IOException;
}
