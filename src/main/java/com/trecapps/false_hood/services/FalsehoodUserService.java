package com.trecapps.false_hood.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.trecapps.false_hood.model.FalsehoodUser;
import com.trecapps.false_hood.repos.FalsehoodUserRepo;

@Service
public class FalsehoodUserService {

	@Autowired
	FalsehoodUserRepo userRepo;
	
	@Value("${trec.key.public}")
	String publicKeyStr;
	
	RSAPublicKey publicKey;
	
	private boolean setKeys()
	{
		if(publicKey == null)
		{
			System.out.println("Key location is " + publicKeyStr);
			File publicFile = new File(publicKeyStr);

			Scanner keyfis;
			try {
				String encKey = "";
				
				keyfis = new Scanner(publicFile);
				
				while(keyfis.hasNext())
				{
					encKey += keyfis.next();
				}
				
				keyfis.close();
				
				System.out.println("Private Key is " + encKey);
				
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(encKey));
				
				publicKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
				
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}
		
		return publicKey != null;
	}
	
	public FalsehoodUser getUserFromToken(String token)
	{
		if(!setKeys())
		{
			System.out.println("Set Keys Failed!");
			return null;
		}
		DecodedJWT decodedJwt = null;
		try
		{			
			decodedJwt = JWT.require(Algorithm.RSA512(publicKey,null))
					.build()
					.verify(token);
		}
		catch(JWTVerificationException e)
		{
			e.printStackTrace();
			return null;
		}
			
		Claim idClaim = decodedJwt.getClaim("ID");
		
		Long idLong = idClaim.asLong();
		
		if(idLong == null)
		{
			System.out.println("User ID turned out to be null");
			return null;
		}
		FalsehoodUser user;
		
		if(userRepo.existsById(idLong))
		{
			user = userRepo.getOne(idLong);
		}
		else
		{
			user = userRepo.save(new FalsehoodUser(idLong, 5));
		}
		
		return user;
	}
	
	public void adjustCredibility(FalsehoodUser user, int points)
	{
		long id = user.getUserId();
		
		if(!userRepo.existsById(id))return;
		
		user.setCredit(user.getCredit() + points);
		
		userRepo.save(user);
	}
}
