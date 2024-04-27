package vip.repository;

import org.springframework.data.jpa.repository.Query;
import vip.modal.S3FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface S3FileMetadataRepository extends JpaRepository<S3FileMetadata,Long> {

    S3FileMetadata findByOriginalFileName(String originalFilename);

    S3FileMetadata findByOriginalFileNameAndUserIdAndIsDeletedIsFalse(String originalFilename, String userId);

    S3FileMetadata findByUserId(String userId);

    List<S3FileMetadata> findByUserIdAndIsDeletedIsFalse(String userId);

    @Query(value = "SELECT original_file_name FROM aws.s3_file_metadata where user_id=?1 and is_deleted=false",nativeQuery = true)
    List<String> findOriginalFileNameByUserIdIgnoreCaseAndIsDeletedFalse(String userId);

    @Query(value = "SELECT * FROM aws.s3_file_metadata where user_id=?1 and is_deleted=false",nativeQuery = true)
    List<S3FileMetadata> findFileNameByUserIdIgnoreCaseAndIsDeletedFalse(String userId);
}
