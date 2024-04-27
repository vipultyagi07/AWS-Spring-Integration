package com.repository;

import com.modal.S3FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface S3FileMetadataRepository extends JpaRepository<S3FileMetadata,Long> {

    S3FileMetadata findByOriginalFileName(String originalFilename);
}
