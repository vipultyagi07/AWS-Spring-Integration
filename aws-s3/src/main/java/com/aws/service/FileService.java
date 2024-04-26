package com.aws.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    String saveFile(MultipartFile file);
    byte[] downloadFile(String fileName);

    List<String> listAllFiles();
    String deleteFile(String fileName);
}
