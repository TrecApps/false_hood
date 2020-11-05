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

import com.trecapps.false_hood.controllers.AuthFalsehoodController;
import com.trecapps.false_hood.controllers.AuthPublicFalsehoodController;
import com.trecapps.false_hood.publicFalsehoods.PublicAttributeService;
import com.trecapps.false_hood.publicFalsehoods.Region;
import com.trecapps.false_hood.publicFalsehoods.RegionEntry;
import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class RegionTest {
	static FalsehoodApp sharedApp;
	
	static RegionEntry regEntries[] = new RegionEntry[3];
	
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
		if(regEntries[0] == null)
			regEntries[0] = new RegionEntry(new Region(null, "Coruscant"), 
					INIT_CORU);
		if(regEntries[1] == null)
			regEntries[1] = new RegionEntry(new Region(null, "District 12"), 
					"Region of Panem, located in the Appalachian region and known for coal production");
		if(regEntries[2] == null)
			regEntries[2] = new RegionEntry(new Region(null, "Morag"), 
					"Old planet, known for intense flooding, once held the Power Stone");
		
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		
		apfController.addRegion(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(regEntries[0]));
		apfController.addRegion(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(regEntries[1]));
		apfController.addRegion(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(regEntries[2]));
	}
	
	@Test
	public void getRegion()
	{
		PublicAttributeService attService = sharedApp.getAttService();
		
		RegionEntry re = attService.getRegion(0);
		
		assertNotNull(re);
		assertNotNull(re.getContents());
		assertNotNull(re.getRegion());
		assertEquals(0L, re.getRegion().getId().longValue());
	}
	
	@Test
	public void failAddInvalid()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addRegion(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken2).body(regEntries[0]));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void failAddNotCredible()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addRegion(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(regEntries[0]));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void succeedAdd()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addRegion(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(regEntries[0]));
		
		assertTrue(resp.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void update()
	{
		PublicAttributeService attService = sharedApp.getAttService();
		
		RegionEntry re = attService.getRegion(0);
		String cont = re.getContents();
		
		assertEquals(INIT_CORU, cont);
		
		re.setContents("\n\n"+ADD_CORU);
		
		String resp = attService.UpdateAttribute(re);
		
		assertEquals("", resp);
		re = attService.getRegion(0);
		assertEquals(INIT_CORU + "\n\n" + ADD_CORU, re.getContents());
	}
}
