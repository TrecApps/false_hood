package com.trecapps.false_hood.resource_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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

@TestMethodOrder(OrderAnnotation.class)
public class InstitutionTest {
	static FalsehoodApp sharedApp;
	
	static InstitutionEntry intEntries[] = new InstitutionEntry[3];
	
	static String INIT_CORU = "Planet that serves as the Capital of the Old Republic and later the Galactic Empire.";
	static String ADD_CORU = "It was replaced by the Hosnian Prime System for the New Republic.";
	
	@BeforeAll
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
		
		FalsehoodUserService userService = sharedApp.getUserService();
		
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken1);
		
		// This should get the user 1 enough credibility to do what needs to be done
		userService.adjustCredibility(user, 70);
		
		initializePublicFigures(sharedApp);
	}
	
	@Test
	@Order(2)
	public void upgradeUser3()
	{
		FalsehoodUserService userService = sharedApp.getUserService();
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken3);
		userService.adjustCredibility(user, 200);
		
		user = userService.getUserFromToken(UserTokens.userToken1);
		userService.adjustCredibility(user, 130);
		
		assertEquals(2, userService.getUserCountAboveCredibility(200));
	}
	
	@Test
	@Order(3)
	public void failApproveReject() throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		
		ResponseEntity<String> resp = apfController.approveInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(0L));
		assertTrue(resp.getStatusCode().is4xxClientError());
		
		resp = apfController.rejectInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(Long.getLong("0")));
		assertTrue(resp.getStatusCode().is4xxClientError());
	}
	
	@Test
	@Order(3)
	public void succeedApproveReject() throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		
		ResponseEntity<String> resp = apfController.approveInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(1L));
		System.out.println("SucceedApproveReject: " + resp.getBody());
		assertTrue(resp.getStatusCode().is2xxSuccessful());
		
		resp = apfController.rejectInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(2L));
		System.out.println("SucceedApproveReject: " + resp.getBody());
		assertTrue(resp.getStatusCode().is2xxSuccessful());
	}
	
	public static void initializePublicFigures(FalsehoodApp app) throws URISyntaxException
	{
		if(intEntries[0] == null)
			intEntries[0] = new InstitutionEntry(new Institution(null, "Coruscant", (byte)0, null), 
					INIT_CORU);
		if(intEntries[1] == null)
			intEntries[1] = new InstitutionEntry(new Institution(null, "District 12", (byte)0, null), 
					"Institution of Panem, located in the Appalachian region and known for coal production");
		if(intEntries[2] == null)
			intEntries[2] = new InstitutionEntry(new Institution(null, "Morag", (byte)0, null), 
					"Old planet, known for intense flooding, once held the Power Stone");
		
		AuthPublicFalsehoodController apfController = app.getAuthPFalsehoodController();
		
		apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[0]));
		apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[1]));
		apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[2]));
	}
	
	@Test
	@Order(1)
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
	@Order(1)
	public void failAddInvalid()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken2).body(intEntries[0]));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
	}
	
	@Test
	@Order(1)
	public void failAddNotCredible()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(intEntries[0]));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
	}
	
	@Test
	@Order(1)
	public void succeedAdd()throws URISyntaxException
	{
		AuthPublicFalsehoodController apfController = sharedApp.getAuthPFalsehoodController();
		ResponseEntity<String> resp = apfController.addInstitution(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(intEntries[0]));
		
		assertTrue(resp.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	@Order(1)
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
