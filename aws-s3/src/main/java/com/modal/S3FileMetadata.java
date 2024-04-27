package com.modal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Setter
@Getter
@ToString
@Table(name = "s3_file_metadata")
@Entity
public class S3FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "bucket_name")
    private String bucketName;

    private String fileName;

    private String originalFileName;

    private String path;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "last_modified")
    @LastModifiedDate
    private Date lastModified;

    @Column(name = "created_Date")
    @CreatedDate
    private Date createdDate;

    private String etag;

    private String userId;


    public S3FileMetadata() {
    }
}
