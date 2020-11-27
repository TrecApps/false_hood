package com.trecapps.false_hood.test_obj;

import java.io.File;

import com.trecapps.false_hood.appeals.FalsehoodAppealService;
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
import com.trecapps.false_hood.miscellanous.FalsehoodStorageHolder;
import com.trecapps.false_hood.publicFalsehoods.PublicAttributeService;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodRepo;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehoodService;
import com.trecapps.false_hood.publicFigure.PublicFigureService;
import com.trecapps.false_hood.repos.CommonLieRepository;
import com.trecapps.false_hood.repos.FalsehoodAppealRepository;
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
	
	public FalsehoodApp()
	{
		if(storageHolder == null)
			storageHolder = new FalsehoodStorageHolder(System.getProperty("user.dir") + File.separator + "build" + File.separator + "storage",
					null,
					null,
					null);
		
		FalsehoodUserRepo userRepo = new FalsehoodUserRepository();
		PublicFalsehoodRepo pfRepo = new PublicFalsehoodRepository();
		userService = new FalsehoodUserService(UserTokens.testKey, userRepo, true);
		publicFigureService = new PublicFigureService(userService, new PublicFigureRepository(), storageHolder);
		attService = new PublicAttributeService(storageHolder, new RegionRepository(), new InstitutionRepository());
		publicFalsehoodService = new PublicFalsehoodService(storageHolder, pfRepo);
		keyService = new KeywordService(new PublicKeywordsRepository(), new KeywordRepository());
		outletService = new MediaOutletService(new MediaOutletRepository(), storageHolder, userService);
		
		FalsehoodRepo fRepo = new FalsehoodRepository();
		
		falsehoodService = new FalsehoodService(fRepo, storageHolder, userService);
		cLieService = new CommonLieService(new CommonLieRepository(), fRepo, pfRepo, storageHolder);
		appealService = new FalsehoodAppealService(new FalsehoodAppealRepository(),
					null,
					fRepo,
					pfRepo,
					userRepo,
					null,
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
