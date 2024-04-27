package vip.controller;

import vip.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/aws")
public class S3Controller {
    @Autowired
    private FileService fileService;

    @PostMapping("/s3/file/upload/{userId}")
    public Map<String, Object> upload(@RequestParam("file")MultipartFile file,@PathVariable String userId){
        return fileService.saveFile(file,userId);
    }
}
