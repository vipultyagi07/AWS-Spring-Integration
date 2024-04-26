package com.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.aws.utility.FileNameUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service implements FileService{

    @Value("${bucketname}")
    private String bucketName;

    private final AmazonS3 s3;

    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String saveFile(MultipartFile file) {
        /**
         *  it will save directly to the bucket, but if you want to save it in any folder
         *  which is created under that bucket than we have to give folder name in the filename
         *  @Example
         *  images/file.png
         */
//        String filename = file.getOriginalFilename();
        String filename = FileNameUtility.getFileNameAccordingToTheFileType(file.getOriginalFilename());
        try{
            File convertedFile = convertMultiPartToFile(file);
            PutObjectResult putObjectResult = s3.putObject(bucketName, filename, convertedFile);
            return putObjectResult.getContentMd5();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile=new File(file.getOriginalFilename());
        FileOutputStream fos=new FileOutputStream((convFile));
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    @Override
    public byte[] downloadFile(String fileName) {
        S3Object object = s3.getObject(bucketName, fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
           return  IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listAllFiles() {
        ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
        return listObjectsV2Result.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());

    }

    @Override
    public String deleteFile(String fileName) {
        s3.deleteObject(bucketName,fileName);
        return "file deleted";
    }

}
