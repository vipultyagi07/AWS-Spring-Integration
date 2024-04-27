package com.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileService {

    Map<String,Object> saveFile(MultipartFile file, String userId);

    byte[] downloadFile(String fileName);

    List<String> listAllFiles();

    String deleteFile(String fileName);
}
