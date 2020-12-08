package com.trecapps.false_hood.resource_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.trecapps.false_hood.controllers.PublicFigureController;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.publicFigure.PublicFigureEntry;
import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class PublicFigureTest {

	static FalsehoodApp sharedApp;
	
	static PublicFigureEntry pubEntries[] = new PublicFigureEntry[3];
	
	@BeforeClass
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
		
		FalsehoodUserService userService = sharedApp.getUserService();
		
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken1);
		
		// This should get the user 1 enough credibility to do what needs to be done
		userService.adjustCredibility(user, 260);
		
		initializePublicFigures(sharedApp);
	}
	
	public static void initializePublicFigures(FalsehoodApp sharedApp) throws URISyntaxException
	{
		if(pubEntries[0] == null)
		pubEntries[0] = new PublicFigureEntry(new PublicFigure(null, "Darth", null, "Vader"),
				"This man is second in command to the Galactic Empire");
		if(pubEntries[1] == null)
		pubEntries[1] = new PublicFigureEntry(new PublicFigure(null, "Tom", "Marvelo", "Riddle"), 
				"He prefers to be known as Lord Voldemort, but don't use his name!");
		if(pubEntries[2] == null)
		pubEntries[2] = new PublicFigureEntry(new PublicFigure(null, "General", "Thunderbolt", "Ross"),
				"This man is a General who was obsessed with capturing the Hulk but moved on to pursue other ambitions like the Sokovia Accords");
		
		PublicFigureController pfController = sharedApp.getpFigureController();
		
		pfController.addPublicFigure(RequestEntity.post(new URI("/Add")).header("Authorization", UserTokens.userToken1).body(pubEntries[0]));
		pfController.addPublicFigure(RequestEntity.post(new URI("/Add")).header("Authorization", UserTokens.userToken1).body(pubEntries[1]));
		pfController.addPublicFigure(RequestEntity.post(new URI("/Add")).header("Authorization", UserTokens.userToken1).body(pubEntries[2]));
	}
	
	@Test
	public void addWithInvalidUser() throws URISyntaxException
	{
		PublicFigureController pfController = sharedApp.getpFigureController();

		ResponseEntity<String> resp = pfController.addPublicFigure(RequestEntity.post(
				new URI("/Add")).header("Authorization", UserTokens.userToken2).body(pubEntries[0]));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
		
		assertEquals("Could Not Authenticate User", resp.getBody());
	}
	
	@Test
	public void failApproveVader() throws URISyntaxException
	{
		PublicFigureController pfController = sharedApp.getpFigureController();

		ResponseEntity<String> resp = pfController.approvePublicFigure(RequestEntity.post(
				new URI("/Add")).header("Authorization", 
						UserTokens.userToken3).body(pubEntries[0].getFigure().getId()));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
		
		assertEquals("Could Not Authenticate User", resp.getBody());
	}
	
	@Test
	public void failRejectVader() throws URISyntaxException
	{
		PublicFigureController pfController = sharedApp.getpFigureController();

		ResponseEntity<String> resp = pfController.rejectPublicFigure(RequestEntity.post(
				new URI("/Add")).header("Authorization",
						UserTokens.userToken3).body(pubEntries[0].getFigure().getId()));
		
		assertTrue(resp.getStatusCode().is4xxClientError());
		
		assertEquals("Could Not Authenticate User", resp.getBody());
	}
	
	@Test
	public void succeedApproveVader() throws URISyntaxException
	{
		PublicFigureController pfController = sharedApp.getpFigureController();

		ResponseEntity<String> resp = pfController.approvePublicFigure(RequestEntity.post(
				new URI("/Add")).header("Authorization", 
						UserTokens.userToken1).body(pubEntries[0].getFigure().getId()));
		
		assertTrue(resp.getStatusCode().is2xxSuccessful());
		
		System.out.println("succeedApproveVader response Body: " + resp.getBody());
	}
	
	@Test
	public void succeedRejectVoldemort() throws URISyntaxException
	{
		PublicFigureController pfController = sharedApp.getpFigureController();

		ResponseEntity<String> resp = pfController.rejectPublicFigure(RequestEntity.post(
				new URI("/Add")).header("Authorization",
						UserTokens.userToken1).body(pubEntries[1].getFigure().getId()));
		
		assertTrue(resp.getStatusCode().is2xxSuccessful());

		System.out.println("succeedRejectVoldemort response Body: " + resp.getBody());
	}
}
