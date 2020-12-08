package com.trecapps.false_hood.resource_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.trecapps.false_hood.controllers.AuthFalsehoodController;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.falsehoods.MediaOutletEntry;
import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class MediaOutletTest {

	static FalsehoodApp sharedApp;
	
	static MediaOutletEntry medEntries[] = new MediaOutletEntry[3];
	
	@BeforeClass
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
	
		FalsehoodUserService userService = sharedApp.getUserService();
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken1);
		
		// This should get the user 1 enough credibility to do what needs to be done
		userService.adjustCredibility(user, 250);
		initializeOutlets(sharedApp);
	}
	
	public static void initializeOutlets(FalsehoodApp app) throws URISyntaxException
	{
		if(medEntries[0] == null)
		medEntries[0] = new MediaOutletEntry(
				new MediaOutlet(null, (short)1750, "Daily Prophet", (byte)0, null),
				"The Prie Outlet of the Wizarding World");
		if(medEntries[1] == null)
		medEntries[1] = new MediaOutletEntry(
				new MediaOutlet(null, (short)-2000, "Holonet", (byte)0, null),
				"The Galaxy's version of the Internet, reporting on the developments of the clone wars!");
		if(medEntries[2] == null)
		medEntries[2] = new MediaOutletEntry(
				new MediaOutlet(null, (short)1960, "Daily Bugle", (byte)0, null),
				"Founded by JJJ, this outlet has it out for Spider-Man.");
		
		AuthFalsehoodController afController = app.getAuthFalsehoodController();
		
		afController.addMediaOutlet(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(medEntries[0]));
		afController.addMediaOutlet(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(medEntries[1]));
		afController.addMediaOutlet(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(medEntries[2]));
	}
	
	@Test
	public void addWithInvalidUser() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		ResponseEntity<String> resp = afController.addMediaOutlet(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken2).body(medEntries[0]));
		assertTrue(resp.getStatusCode().is4xxClientError());
		
		assertEquals("Could Not Authenticate User", resp.getBody());
	}
	
	@Test
	public void failApproveProphet() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		ResponseEntity<String> resp = afController.approveMediaOutlet(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(medEntries[0].getOutlet().getOutletId()));
		assertTrue(resp.getStatusCode().is4xxClientError());
		
		assertEquals("Could Not Authenticate User", resp.getBody());
	}
	
	@Test
	public void failRejectProphet() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		ResponseEntity<String> resp = afController.rejectMediaOutlet(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(medEntries[0].getOutlet().getOutletId()));
		assertTrue(resp.getStatusCode().is4xxClientError());
		
		assertEquals("Could Not Authenticate User", resp.getBody());
	}
	
	@Test
	public void succeedApproveHolonet() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		ResponseEntity<String> resp = afController.approveMediaOutlet(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(medEntries[1].getOutlet().getOutletId()));
		assertTrue(resp.getStatusCode().is2xxSuccessful());
	}
	
	@Test
	public void succeedRejectBugle() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		ResponseEntity<String> resp = afController.rejectMediaOutlet(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(medEntries[2].getOutlet().getOutletId()));
		assertTrue(resp.getStatusCode().is2xxSuccessful());
	}
}
