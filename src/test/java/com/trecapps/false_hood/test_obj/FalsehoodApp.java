package com.trecapps.false_hood.test_obj;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.trecapps.false_hood.appeals.FalsehoodAppealService;
import com.trecapps.false_hood.appeals.FalsehoodAppealSignatureRepo;
import com.trecapps.false_hood.commonLie.CommonLieService;
import com.trecapps.false_hood.controllers.AuthFalsehoodController;
import com.trecapps.false_hood.controllers.AuthPublicFalsehoodController;
import com.trecapps.false_hood.controllers.CommonLieController;
import com.trecapps.false_hood.controllers.FalsehoodAppealController;
import com.trecapps.false_hood.controllers.FalsehoodController;
import com.trecapps.false_hood.controllers.PublicFalsehoodController;
import com.trecapps.false_hood.controllers.PublicFigureController;
import com.trecapps.false_hood.falsehoods.FalsehoodRepo;
import com.trecapps.false_hood.falsehoods.FalsehoodService;
import com.trecapps.false_hood.falsehoods.MediaOutletService;
import com.trecapps.false_hood.keywords.KeywordService;
import com.trecapps.false_hood.miscellanous.FalsehoodEmailService;
import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.publicFalsehoods.PublicAttributeService;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodService;
import com.trecapps.false_hood.publicFigure.PublicFigureService;
import com.trecapps.false_hood.repos.CommonLieRepository;
import com.trecapps.false_hood.repos.FalsehoodAppealRepository;
import com.trecapps.false_hood.repos.FalsehoodAppealSignatureRepository;
import com.trecapps.false_hood.repos.FalsehoodRepository;
import com.trecapps.false_hood.repos.FalsehoodUserRepository;
import com.trecapps.false_hood.repos.InstitutionRepository;
import com.trecapps.false_hood.repos.KeywordRepository;
import com.trecapps.false_hood.repos.MediaOutletRepository;
import com.trecapps.false_hood.repos.PublicFalsehoodRepository;
import com.trecapps.false_hood.repos.PublicFigureRepository;
import com.trecapps.false_hood.repos.PublicKeywordsRepository;
import com.trecapps.false_hood.repos.RegionRepository;
import com.trecapps.false_hood.users.FalsehoodUserRepo;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class FalsehoodApp {
	
	static FalsehoodStorageHolder storageHolder;
	
	FalsehoodUserService userService;
	
	PublicFigureService publicFigureService;
	
	PublicAttributeService attService;
	
	PublicFalsehoodService publicFalsehoodService;
	
	KeywordService keyService;
	
	MediaOutletService outletService;
	
	FalsehoodService falsehoodService;
	
	CommonLieService cLieService;
	
	FalsehoodAppealService appealService;
	
	PublicFigureController pFigureController;
	
	PublicFalsehoodController pFalsehoodController;

	FalsehoodController falsehoodController;
	
	FalsehoodAppealController appealController;
	
	CommonLieController cLieController;
	
	AuthPublicFalsehoodController authPFalsehoodController;
	
	AuthFalsehoodController authFalsehoodController;
	
	FalsehoodEmailService emailService;
	
	FalsehoodAppealSignatureRepo signatureRepo;
	
	Map<String, String> inbox;
	
	public FalsehoodApp()
	{
		inbox = new TreeMap<>();
		
		if(storageHolder == null)
			storageHolder = new FalsehoodStorageHolder(System.getProperty("user.dir") + File.separator + "build" + File.separator + "storage",
					null,
					null,
					null);
		
		FakeMailSender sender = new FakeMailSender(this);
		
		emailService = new FalsehoodEmailService(sender);
		
		FalsehoodUserRepo userRepo = new FalsehoodUserRepository();
		PublicFalsehoodRepo pfRepo = new PublicFalsehoodRepository();
		userService = new FalsehoodUserService(UserTokens.testKey, userRepo, true);
		publicFigureService = new PublicFigureService(userService, new PublicFigureRepository(), storageHolder);
		attService = new PublicAttributeService(storageHolder, new RegionRepository(), new InstitutionRepository(), userService);
		publicFalsehoodService = new PublicFalsehoodService(storageHolder, pfRepo, userService);
		keyService = new KeywordService(new PublicKeywordsRepository(), new KeywordRepository());
		outletService = new MediaOutletService(new MediaOutletRepository(), storageHolder, userService);
		signatureRepo = new FalsehoodAppealSignatureRepository();
		
		FalsehoodRepo fRepo = new FalsehoodRepository();
		
		falsehoodService = new FalsehoodService(fRepo, storageHolder, userService);
		cLieService = new CommonLieService(new CommonLieRepository(), fRepo, pfRepo, storageHolder);
		appealService = new FalsehoodAppealService(new FalsehoodAppealRepository(),
					signatureRepo,
					fRepo,
					pfRepo,
					userRepo,
					emailService,
					storageHolder);
		
		// Initialize the Controllers now that the Services are initialized
		pFigureController = new PublicFigureController(userService, publicFigureService);
		pFalsehoodController = new PublicFalsehoodController(keyService, publicFalsehoodService, attService);
		falsehoodController = new FalsehoodController(falsehoodService, keyService, outletService);
		appealController = new FalsehoodAppealController(userService, appealService);
		cLieController = new CommonLieController(userService, cLieService);
		authPFalsehoodController = new AuthPublicFalsehoodController(userService, publicFalsehoodService, keyService, attService);
		authFalsehoodController = new AuthFalsehoodController(userService, falsehoodService, outletService, keyService);
	}
	
	void sendEmail(SimpleMailMessage message)
	{
		String[] rec = message.getTo();
		String mainMessage = message.getText();
		
		inbox.put(rec[0], mainMessage.substring(mainMessage.indexOf("<h2>") + 3, mainMessage.indexOf("</h2>")));
	}
	
	public String getMessage(String key)
	{
		for(Map.Entry<String, String> entry: inbox.entrySet())
		{
			System.out.println("Inbox Key '" + entry.getKey() + "' with value '" + entry.getValue() + "'");
		}
		return inbox.get(key);
	}
	
	void sendEmail(MimeMessage message)
	{
		Address[] rec;
		
		try {
			rec = message.getRecipients(Message.RecipientType.TO);
			String mainMessage;
			mainMessage = message.getContent().toString();
			inbox.put(rec[0].toString(), mainMessage.substring(mainMessage.indexOf("<h2>") + 3, mainMessage.indexOf("</h2>")));
		} catch (IOException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	/**
	 * @return the userService
	 */
	public FalsehoodUserService getUserService() {
		return userService;
	}

	/**
	 * @return the publicFigureService
	 */
	public PublicFigureService getPublicFigureService() {
		return publicFigureService;
	}

	/**
	 * @return the attService
	 */
	public PublicAttributeService getAttService() {
		return attService;
	}

	/**
	 * @return the publicFalsehoodService
	 */
	public PublicFalsehoodService getPublicFalsehoodService() {
		return publicFalsehoodService;
	}

	/**
	 * @return the keyService
	 */
	public KeywordService getKeyService() {
		return keyService;
	}

	/**
	 * @return the outletService
	 */
	public MediaOutletService getOutletService() {
		return outletService;
	}

	/**
	 * @return the falsehoodService
	 */
	public FalsehoodService getFalsehoodService() {
		return falsehoodService;
	}

	/**
	 * @return the cLieService
	 */
	public CommonLieService getcLieService() {
		return cLieService;
	}

	/**
	 * @return the appealService
	 */
	public FalsehoodAppealService getAppealService() {
		return appealService;
	}

	/**
	 * @return the pFigureController
	 */
	public PublicFigureController getpFigureController() {
		return pFigureController;
	}

	/**
	 * @return the pFalsehoodController
	 */
	public PublicFalsehoodController getpFalsehoodController() {
		return pFalsehoodController;
	}

	/**
	 * @return the falsehoodController
	 */
	public FalsehoodController getFalsehoodController() {
		return falsehoodController;
	}

	/**
	 * @return the appealController
	 */
	public FalsehoodAppealController getAppealController() {
		return appealController;
	}

	/**
	 * @return the cLieController
	 */
	public CommonLieController getcLieController() {
		return cLieController;
	}

	/**
	 * @return the authPFalsehoodController
	 */
	public AuthPublicFalsehoodController getAuthPFalsehoodController() {
		return authPFalsehoodController;
	}

	/**
	 * @return the authFalsehoodController
	 */
	public AuthFalsehoodController getAuthFalsehoodController() {
		return authFalsehoodController;
	}
	
	
}
