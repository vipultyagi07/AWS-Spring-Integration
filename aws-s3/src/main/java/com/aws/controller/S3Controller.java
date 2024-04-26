package com.aws.controller;

import com.aws.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/aws")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @PostMapping("/file/upload")
    public String upload(@RequestParam("file")MultipartFile file){
        return s3Service.saveFile(file);
    }
}
