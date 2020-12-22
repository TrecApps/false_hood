package com.trecapps.false_hood.falsehoods_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.trecapps.false_hood.appeals.FalsehoodAppeal;
import com.trecapps.false_hood.appeals.FalsehoodAppealEntry;
import com.trecapps.false_hood.appeals.FalsehoodAppealSignature;
import com.trecapps.false_hood.controllers.FalsehoodAppealController;
import com.trecapps.false_hood.controllers.FalsehoodController;
import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.SearchFalsehood;
import com.trecapps.false_hood.resource_tests.InstitutionTest;
import com.trecapps.false_hood.resource_tests.MediaOutletTest;
import com.trecapps.false_hood.resource_tests.PublicFigureTest;
import com.trecapps.false_hood.resource_tests.RegionTest;
import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

@TestMethodOrder(OrderAnnotation.class)
public class AppealsTest {

	static FalsehoodApp sharedApp;
	
	@BeforeAll
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
	
		FalsehoodUserService userService = sharedApp.getUserService();
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken1);
		FalsehoodUser user3 = userService.getUserFromToken(UserTokens.userToken3);
		
		userService.adjustCredibility(user3, 15);
		
		// This should get the user 1 enough credibility to do what needs to be done
		userService.adjustCredibility(user, 70);
		
		RegionTest.initializePublicFigures(sharedApp);
		InstitutionTest.initializePublicFigures(sharedApp);
		PublicFigureTest.initializePublicFigures(sharedApp);
		MediaOutletTest.initializeOutlets(sharedApp);
		
		FalsehoodTest.initializeOutlets(sharedApp);
		PublicFalsehoodTest.initializeFalsehoods(sharedApp);
		
		FalsehoodTest fTest = new FalsehoodTest();
		fTest.succeedApprove(sharedApp);
		fTest.succeedReject(sharedApp);
		

	}
	
	@Test
	@Order(1)
	public void addFirstAppeal() throws URISyntaxException
	{
		FalsehoodAppealController appCont = sharedApp.getAppealController();
		
		
		
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		List<Falsehood> localFalsehoods = fController.searchFalsehoodByParams(new SearchFalsehood(null,null, null, null, null, 0, 20, null,null,null, null));
		
		FalsehoodAppeal appeal = new FalsehoodAppeal(null, localFalsehoods.get(0), null, "Some Reason", sharedApp.getUserService().getUserFromToken(UserTokens.userToken3));
		
		FalsehoodAppealEntry appealEntry = new FalsehoodAppealEntry("Testing", appeal);
		
		appCont.addAppeal(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(appealEntry));
		
		
		assertEquals(1, appCont.getAppeals().getBody().size());
	}
	
	@Test
	@Order(2)
	public void petitionFirstAppeal() throws URISyntaxException
	{
		FalsehoodAppealController appCont = sharedApp.getAppealController();
		
		ResponseEntity<FalsehoodAppealEntry> appeal = appCont.getAppeal(BigInteger.ZERO);
		
		FalsehoodAppealSignature sign = new FalsehoodAppealSignature();
		
		sign.setAppeal(appeal.getBody().getAppeal());
		sign.setUser(sharedApp.getUserService().getUserFromToken(UserTokens.userToken3));
		
		
		ResponseEntity<String> signResp = appCont.signPetition(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(sign));
		
		assertTrue(signResp.getStatusCode().is2xxSuccessful());
		
		// tormontrec@gmail.com // Email for user 3
		
		assertNotNull(sharedApp.getMessage("tormontrec@gmail.com"));
		
	}
}
