package com.trecapps.false_hood.repos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Component
public class FalsehoodStorageAws {

	AmazonS3 s3;
	
	String bucketName = "temp-falsehoods-bucket";
	
	@Autowired
	public FalsehoodStorageAws(@Value("${S3_ACCESS}")String keyId, @Value("${S3_SECRET}")String secret, @Value("${S3_REGION:us-east-1}")String region)
	{
		AWSCredentialsProvider provide = new AWSStaticCredentialsProvider(new BasicAWSCredentials(keyId, secret));
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		builder.setCredentials(provide);
		
		builder.setRegion(region);
		s3 = builder.build();
	}
	
	boolean isValid()
	{
		return s3 != null;
	}
	
	public String retrieveContents(String key) throws IOException
	{
		String newContent;
		
		try(S3Object obj = s3.getObject(bucketName, key);
				S3ObjectInputStream objContent = obj.getObjectContent();)
		{			
			byte[] bytes = objContent.readAllBytes();
		
			newContent = new String(bytes);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			
			throw e;
		}
		
		return newContent;
	}
	
	public String addNewFile(String key, String content)
	{
		try {
			s3.putObject(bucketName, key, content);
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
		return "Success";
	}
	
	public String appendFile(String key, String content)
	{		
		String newContent = "";
		
		
		try (S3Object obj = s3.getObject(bucketName, key);
		S3ObjectInputStream objContent = obj.getObjectContent();) 
		{
			byte[] bytes = objContent.readAllBytes();
			
			
			
			
			
			newContent = new String(bytes);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		
		newContent += content;
		
		
		
		try {
			s3.putObject(bucketName, key, newContent);
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
		
		
		return "Success";
	}
	
	public String removeFile(String key)
	{
		try
		{
			s3.deleteObject(bucketName, key);
		}catch(Exception e)
		{
			return e.getMessage();
		}
		
		return "Success";
	}
}
