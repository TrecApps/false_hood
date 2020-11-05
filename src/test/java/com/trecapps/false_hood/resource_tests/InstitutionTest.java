package com.trecapps.false_hood.resource_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.trecapps.false_hood.controllers.AuthPublicFalsehoodController;
import com.trecapps.false_hood.publicFalsehoods.PublicAttributeService;
import com.trecapps.false_hood.publicFalsehoods.Institution;
import com.trecapps.false_hood.publicFalsehoods.InstitutionEntry;
import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class InstitutionTest {
	static FalsehoodApp sharedApp;
	
	static InstitutionEntry intEntries[] = new InstitutionEntry[3];
	
	static String INIT_CORU = "Planet that serves as the Capital of the Old Republic and later the Galactic Empire.";
	static String ADD_CORU = "It was replaced by the Hosnian Prime System for the New Republic.";
	
	@BeforeClass
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
		
		FalsehoodUserService userService = sharedApp.getUserService();
		
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken1);
		
		// This should get the user 1 enough credibility to do what needs to be done
		userService.adjustCredibility(user, 70);
		
		initializePublicFigures(sharedApp);
	}
	
	public static void initializePublicFigures(FalsehoodApp sharedApp) throws URISyntaxException
	{
		if(intEntries[0] == null)
			intEntries[0] = new InstitutionEntry(new Institution(null, "Coruscant"), 
					INIT_CORU);
		if(intEntries[1] == null)
			intEntries[1] = new InstitutionEntry(new Institution(null, "District 12"), 
					"Institution of Panem, located in the Appalachian region and known for coal production");
		if(intEntries[2] == null)
			intEntries[2] = new InstitutionEntry(new Institution(null, "Morag"), 
					"Old planet, known for intense flooding, once held the Power Stone");
		
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		
		apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[0]));
		apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[1]));
		apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[2]));
	}
	
	@Test
	public void getInstitution()
	{
		PublicAttributeService attService = sharedApp.getAttService();
		
		InstitutionEntry re = attService.getInstitution(0);
		
		assertNotNull(re);
		assertNotNull(re.getContents());
		assertNotNull(re.getInstitution());
		assertEquals(0L, re.getInstitution().getId().longValue());
	}
	
	@Test
	public void failAddInvalid()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken2).body(intEntries[0]));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void failAddNotCredible()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(intEntries[0]));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void succeedAdd()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[0]));
		
		assertTrue(resp.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void update()
	{
		PublicAttributeService attService = sharedApp.getAttService();
		
		InstitutionEntry re = attService.getInstitution(0L);
		String cont = re.getContents();
		
		assertEquals(INIT_CORU, cont);
		
		re.setContents("\n\n"+ADD_CORU);
		
		String resp = attService.UpdateAttribute(re);
		
		assertEquals("", resp);
		re = attService.getInstitution(0L);
		assertEquals(INIT_CORU + "\n\n" + ADD_CORU, re.getContents());
	}
}
