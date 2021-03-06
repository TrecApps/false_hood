package com.trecapps.false_hood.miscellanous;

import java.io.IOException;

import org.json.JSONObject;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class FalsehoodStorageAws {

	AmazonS3 s3;
	
	String bucketName = "temp-falsehoods-bucket";
	
	public FalsehoodStorageAws(String keyId, String secret, String region)
	{
		AWSCredentialsProvider provide = new AWSStaticCredentialsProvider(new BasicAWSCredentials(keyId, secret));
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		builder.setCredentials(provide);
		
		builder.setRegion(region);
		s3 = builder.build();
	}

	public String addJsonFile(String key, JSONObject obj)
	{
		if(!key.endsWith(".json"))
			key = key + ".json";

		try {
			s3.putObject(bucketName, key, obj.toString());
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
		return "Success";
	}

	public JSONObject getJSONObj(String key) throws IOException {
		if(!key.endsWith(".json"))
			key = key + ".json";

		return new JSONObject(retrieveContents(key));
	}
	
	public boolean isValid()
	{
		return s3 != null;
	}
	
	public String retrieveContents(String key) throws IOException
	{
		String newContent;
		System.out.println("Retrieving S3 Contents with key: " + key);
		
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
