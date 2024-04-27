package vip.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileService {

    Map<String,Object> saveFile(MultipartFile file, String userId);

    Map<String,Object> downloadFile(String userId,String originalFileName);


    List<String> listAllFiles(String userId);

    Map<String, Object>  deleteFile(String userId, String fileName);

    Map<String, Object> deleteAllDataOfaUser(String userId);
}
