package com.trecapps.false_hood.miscellanous;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Scanner;

import org.json.JSONObject;

public class FalsehoodStorageLocal {
	
	File directory;
	
	public FalsehoodStorageLocal(String directoryStr)
	{
		directory = new File(directoryStr);
		
		if(!directory.exists() || !directory.isDirectory())
			throw new RuntimeException("Needed a real Directory to operate in! Not: " + directoryStr);
	}
	
	public String addJsonFile(String key, JSONObject obj)
	{
		if(!key.endsWith(".json"))
			key = key + ".json";
		
		String file = directory.getAbsolutePath() + File.separator + key;
		
		File objectFile = new File(file);
		
		OutputStream os = null;
        try {
            os = new FileOutputStream(objectFile);

            byte[] contentBytes = obj.toString().getBytes();
			
            os.write(contentBytes, 0, contentBytes.length);
        } catch (IOException e) {
        	return e.getMessage();
        }finally{
            try {
                os.close();
            } catch (IOException e) {
                return e.getMessage();
            }
        }
        
        return "Success";
	}
	
	public JSONObject getJSONObj(String key) throws IOException {
		if(!key.endsWith(".json"))
			key = key + ".json";

		return new JSONObject(retrieveContents(key));
	}
	
	public String addNewFile(String key, String content)
	{
		String file = directory.getAbsolutePath() + File.separator + key;
		
		File objectFile = new File(file);
		
		OutputStream os = null;
        try {
            os = new FileOutputStream(objectFile);
            byte[] contentBytes = content.getBytes();
			
            os.write(contentBytes, 0, contentBytes.length);
        } catch (IOException e) {
        	return e.getMessage();
        }finally{
            try {
                os.close();
            } catch (IOException e) {
                return e.getMessage();
            }
        }
        
        return "Success";
	}
	
	public String retrieveContents(String key) throws IOException
	{
		String file = directory.getAbsolutePath() + File.separator + key;
		
		File objectFile = new File(file);
	
		Scanner is = null;
		
		String ret = "";
		
		try 
		{
			is = new Scanner (new FileReader(objectFile));
		
			while(is.hasNextLine())
			{
				ret += is.nextLine();
				if(is.hasNextLine())
					ret += "\n";
			}
		}
		finally
		{
			if(is != null)
				is.close();
		}
		return ret;
	}
	
	public String appendFile(String key, String content)
	{		
		OutputStream os = null;
		String file = directory.getAbsolutePath() + File.separator + key;
		
		File objectFile = new File(file);
		try
		{
			String currentContents = retrieveContents(key);
			
			currentContents += content;
			os = new FileOutputStream(objectFile);
			
			byte[] contentBytes = currentContents.getBytes();
			
            os.write(contentBytes, 0, contentBytes.length);
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
		finally
		{
			if(os != null)
				try {
					os.close();
				} catch (IOException e) {
					return e.getMessage();
				}
		}
		
		return "Success";
	}
	
	
	public String removeFile(String key)
	{
		String file = directory.getAbsolutePath() + File.separator + key;
		
		if(new File(file).delete())
			return "Success";
		return "Failed to Delete 'file'";
	}
}
