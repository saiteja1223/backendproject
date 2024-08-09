package com.movie.cenima.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class FileService {
     public String uploadfile(String path, MultipartFile file) throws Exception{
         String fileName=file.getOriginalFilename();
         String filePath=path+ File.separator+fileName;
         File f=new File(path);
         if(!f.exists()){
             f.mkdir();

         }
         Files.copy(file.getInputStream(), Paths.get(filePath));
         return fileName;
     }
     public InputStream getResourceFile (String path,String fileName) throws FileNotFoundException {
         String filePath = path + File.separator + fileName;
         return new FileInputStream(filePath);

     }
}
