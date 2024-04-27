package vip.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import vip.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @GetMapping("/s3/file/download/{userId}/{originalFileName}")
    public ResponseEntity<byte[]> download(@PathVariable String userId,@PathVariable String originalFileName) {
        Map<String,Object> fileContent = fileService.downloadFile(userId,originalFileName);

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", (String)fileContent.get("fileName"));

        return new ResponseEntity<>((byte[])fileContent.get("data"), headers, HttpStatus.OK);
    }

    @DeleteMapping("/s3/file/delete/{userId}/{originalFileName}")
    public Map<String, Object>  delete(@PathVariable String userId,@PathVariable String originalFileName) {
        return fileService.deleteFile(userId,originalFileName);
    }

    @GetMapping("/get/all/file/{userId}")
    public List<String> getAllFile(@PathVariable String userId) {

        return fileService.listAllFiles(userId);

    }
}
