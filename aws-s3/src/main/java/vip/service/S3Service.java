package vip.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import jakarta.persistence.EntityNotFoundException;
import vip.modal.ProjectDetail;
import vip.modal.S3FileMetadata;
import vip.repository.S3FileMetadataRepository;
import vip.utility.FileNameUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class S3Service implements FileService {

    @Value("${bucketname}")
    private String bucketName;

    private final AmazonS3 s3;

    @Autowired
    private S3FileMetadataRepository s3FileMetadataRepository;

    @Autowired
    private ProjectDetailService projectDetailService;

    public S3Service(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public Map<String, Object> saveFile(MultipartFile file, String userId, ProjectDetail projectDetail) {

        Map<String, Object> result = new HashMap<>();

        /**
         *  it will save directly to the bucket, but if you want to save it in any folder
         *  which is created under that bucket than we have to give folder name in the filename
         *  @Example
         *  images/file.png
         */
//        String filename = file.getOriginalFilename();
        String originalFilename = file.getOriginalFilename();
        String filename = FileNameUtility.getFileNameAccordingToTheFileType(originalFilename);
        try {

            projectDetailService.saveProjectDetailsWithUser(projectDetail,userId);

            S3FileMetadata existingFile = s3FileMetadataRepository.findByOriginalFileNameAndUserIdAndIsDeletedIsFalse(originalFilename,userId);
            if (Objects.nonNull(existingFile)) {
                result.put("Status", "File with same name :'" + existingFile.getOriginalFileName() + "' already exist in the system");
                return result;
            }
            //here we upload our data to s3

            PutObjectResult putObjectResult = uploadDataToS3(file, filename);

            // Retrieve additional metadata about the uploaded object
            ObjectMetadata metadata = putObjectResult.getMetadata();
            String etag = putObjectResult.getETag();
            Date lastModified = metadata.getLastModified();
            long fileSize = metadata.getContentLength();

            // Create a map to hold the information
            result.put("md5Hash", putObjectResult.getContentMd5());
            result.put("etag", etag);
            result.put("lastModified", lastModified);
            result.put("fileSize", fileSize);
            result.put("fileType", FileNameUtility.getFileExtension(originalFilename));
            result.put("filePath", "/" + filename);
            result.put("fileName", filename);
            result.put("Status", "File Uploaded Successfully");
            saveMetaData(result, userId, originalFilename);

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PutObjectResult uploadDataToS3(MultipartFile file, String filename) throws IOException {

        File convertedFile = convertMultiPartToFile(file);
        PutObjectResult putObjectResult = s3.putObject(bucketName, filename, convertedFile);
        return putObjectResult;

    }

    private void saveMetaData(Map<String, Object> result, String userId, String originalFileName) {

        S3FileMetadata s3FileMetadata = new S3FileMetadata();
        if (result != null) {
            s3FileMetadata.setEtag((String) result.get("etag"));
            s3FileMetadata.setLastModified((Date) result.get("lastModified"));
            s3FileMetadata.setCreatedDate(new Date());
            s3FileMetadata.setFileSize((Long) result.get("fileSize"));
            s3FileMetadata.setBucketName(bucketName);
            s3FileMetadata.setOriginalFileName(originalFileName);
            s3FileMetadata.setFileName((String) result.get("fileName")); // Assuming 'md5Hash' corresponds to fileKey
            s3FileMetadata.setPath((String) result.get("filePath"));
            s3FileMetadata.setContentType((String) result.get("fileType")); // Assuming 'fileType' corresponds to contentType
            s3FileMetadata.setUserId(userId);
            s3FileMetadataRepository.save(s3FileMetadata);
        }

    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream((convFile));
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    @Override
    public Map<String,Object> downloadFile(String userId,String originalFileName) {
        S3FileMetadata existingUserMetaData=s3FileMetadataRepository.findByOriginalFileNameAndUserIdAndIsDeletedIsFalse(originalFileName,userId);
        if(Objects.isNull(existingUserMetaData)){
            throw new EntityNotFoundException("no data is present for this user");
        }
        Map<String,Object> result=new HashMap<>();
        result.put("fileName",existingUserMetaData.getOriginalFileName());
        String fileName = existingUserMetaData.getFileName();
        S3Object object = s3.getObject(bucketName, fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            result.put("data",IOUtils.toByteArray(objectContent));
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listAllFiles(String userId) {

        return s3FileMetadataRepository.findOriginalFileNameByUserIdIgnoreCaseAndIsDeletedFalse(userId);
    }

    @Override
    public Map<String, Object>  deleteFile(String userId,String originalFileName) {
        Map<String, Object>  result= new HashMap<>();

        S3FileMetadata existingUserMetaData=s3FileMetadataRepository.findByOriginalFileNameAndUserIdAndIsDeletedIsFalse(originalFileName,userId);
        if(Objects.isNull(existingUserMetaData)){
            result.put("status","No such data is present ");
            return result;
        }
        String fileName = existingUserMetaData.getFileName();
        s3.deleteObject(bucketName, fileName);
        existingUserMetaData.setDeleted(true);
        S3FileMetadata save = s3FileMetadataRepository.save(existingUserMetaData);
        result.put("status","File deleted successfully");
        result.put("fileName",originalFileName);
        result.put("data",save);
        return result;
    }

    @Override
    public Map<String, Object> deleteAllDataOfaUser(String userId) {

        Map<String, Object>  result= new HashMap<>();
        List<S3FileMetadata> fileMetadataList = s3FileMetadataRepository.findFileNameByUserIdIgnoreCaseAndIsDeletedFalse(userId);

        if(Objects.isNull(fileMetadataList)){
            result.put("status","No data is present ");
            return result;
        }
        int count=1;
        for(S3FileMetadata file:fileMetadataList){

            s3.deleteObject(bucketName, file.getFileName());

            file.setDeleted(true);
            s3FileMetadataRepository.save(file);
            result.put(count+" Successfully deleted",file.getOriginalFileName());
            count++;

        }
        result.put("status",count+" files are deleted successfully");

        return result;

    }
}
