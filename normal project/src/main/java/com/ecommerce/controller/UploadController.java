package com.ecommerce.controller;
import org.springframework.beans.factory.annotation.Value; import org.springframework.web.bind.annotation.*; import org.springframework.web.multipart.MultipartFile; import java.nio.file.*; import java.util.*;
@RestController @RequestMapping("/api/uploads")
public class UploadController {
 @Value("${app.upload-dir}") private String directory;
 @PostMapping(value="/images",consumes="multipart/form-data") Map<String,String> upload(@RequestParam MultipartFile file)throws Exception{String type=file.getContentType();if(file.isEmpty()||type==null||!Set.of("image/jpeg","image/png","image/webp","image/gif").contains(type))throw new IllegalArgumentException("Only JPG, PNG, WEBP or GIF images are allowed");String ext=switch(type){case "image/png"->".png";case "image/webp"->".webp";case "image/gif"->".gif";default->".jpg";};Path dir=Paths.get(directory).toAbsolutePath().normalize();Files.createDirectories(dir);String name=UUID.randomUUID()+ext;Files.copy(file.getInputStream(),dir.resolve(name),StandardCopyOption.REPLACE_EXISTING);return Map.of("url","/uploads/"+name);}
}
