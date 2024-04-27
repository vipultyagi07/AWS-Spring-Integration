package vip.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.secrets.Crypto;
import vip.utility.Utils;

@Configuration
public class S3Configuration {
    @Value("${accessKey}")
    private String accessKey;

    @Value("${SecretKey}")
    private String secretKey;

    @Value("${bucketname}")
    private String bucketname;

    @Value("${region}")
    private String region;

    @Bean
    public AmazonS3 s3() throws Exception {
        // I have encrypted the accessKey and secretKey.
        AWSCredentials awsCredentials= new BasicAWSCredentials(Crypto.transform(accessKey, Utils.SECRET_KEY),Crypto.transform(secretKey, Utils.SECRET_KEY));

        return AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(
                new AWSStaticCredentialsProvider(awsCredentials)).build();
    }
}
