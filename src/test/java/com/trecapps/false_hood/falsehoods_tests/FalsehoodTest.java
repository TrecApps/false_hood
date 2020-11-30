package com.trecapps.false_hood.falsehoods_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.trecapps.false_hood.controllers.AuthFalsehoodController;
import com.trecapps.false_hood.controllers.FalsehoodController;
import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FullFalsehood;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.falsehoods.MediaOutletService;
import com.trecapps.false_hood.falsehoods.SearchFalsehood;
import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.miscellanous.VerdictSubmission;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.publicFigure.PublicFigureService;
import com.trecapps.false_hood.resource_tests.MediaOutletTest;
import com.trecapps.false_hood.resource_tests.PublicFigureTest;
import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class FalsehoodTest {

	static FalsehoodApp sharedApp;
	
	static FullFalsehood falsehoods[] = new FullFalsehood[5];
	
	static ArrayList<String> outlets = new ArrayList<>();
	
	static List<PublicFigure> figures;
	
	static final long DATE_2010 = 1290000000000L;
	static final long DATE_2014 = 1403000000000L;
	static final long DATE_2017 = 1507900000000L;
	static final long DATE_2020 = 1603000000000L;
	static final long DATE_2008 = 1210000000000L;
	
	@BeforeClass
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
	
		FalsehoodUserService userService = sharedApp.getUserService();
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken1);
		
		// This should get the user 1 enough credibility to do what needs to be done
		userService.adjustCredibility(user, 60);
		
		MediaOutletTest.initializeOutlets(sharedApp);
		PublicFigureTest.initializePublicFigures(sharedApp);
		
		initializeOutlets(sharedApp);
		
	}
	
	@Test
	public void searchByDates()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		SearchFalsehood search = new SearchFalsehood();
		
		search.setFrom(new Date(DATE_2017 - 50L));
		List<Falsehood> f = fController.GetFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setTo(new Date(DATE_2020 - 1L));
		f = fController.GetFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setFrom(null);
		f = fController.GetFalsehoodByParams(search);
		assertEquals(8, f.size());
	}
	
	@Test
	public void searchByFigure()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		List<PublicFigure> localFigures = new ArrayList<>();
		localFigures.add(figures.get(1));
		search.setAuthors(localFigures);
		
		List<Falsehood> f = fController.GetFalsehoodByParams(search);
		
		assertEquals(2, f.size());
		
		localFigures.add(figures.get(2));
		search.setAuthors(localFigures);
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(6, f.size());
	}
	
	@Test
	public void searchByOutlet()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		List<String> outletNames = new ArrayList<String>();
		outletNames.add(outlets.get(0));
		
		search.setOutlets(outletNames);
		
		List<Falsehood> f = fController.GetFalsehoodByParams(search);
		
		assertEquals(4, f.size());
		
		outletNames.add(outlets.get(1));
		search.setOutlets(outletNames);
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(8, f.size());
	}
	
	@Test
	public void searchByTerms()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		search.setTerms("Vader Serena");
		
		List<Falsehood> f = fController.GetFalsehoodByParams(search);
		
		assertEquals(8, f.size());
		
		search.setTerms("Dumbledore Voldemort");
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(4, f.size());
	}
	
	@Test
	public void searchBySeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		
		List<Falsehood> f = fController.GetFalsehoodByParams(search);
		
		assertEquals(8, f.size());
		
		search.setMinimum(Severity.HYPOCRISY);;
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(4, f.size());
		
		search.setMaximum(null);
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(6, f.size());
	}
	
	public static void initializeOutlets(FalsehoodApp app) throws URISyntaxException
	{
		MediaOutletService outletService = app.getOutletService();
		PublicFigureService figureService = app.getPublicFigureService();
		
		MediaOutlet outlet1 = outletService.GetMediaOutlet(0);
		outlets.add(outlet1.getName());
		MediaOutlet outlet2 = outletService.GetMediaOutlet(1);
		outlets.add(outlet2.getName());
		MediaOutlet outlet3 = outletService.GetMediaOutlet(2);
		outlets.add(outlet3.getName());
		
		figures = figureService.getPublicFigures(true, 0, 10);
		assertTrue(figures.size() > 2);
		
		if(falsehoods[0] == null)
		{
			falsehoods[0] = new FullFalsehood("The first Falsehood Detected", // Wed Nov 17 2010 06:20:00
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)1, figures.get(0), null, "source1", new Date(DATE_2010),""),
					"Vader;;Palpatine;;Empire");
		}
		if(falsehoods[1] == null)
		{
			falsehoods[1] = new FullFalsehood("The Second Falsehood Detected", // Fri Oct 13 2017 07:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)3, figures.get(0), null, "source2", new Date(DATE_2017),""),
					"Vader;;Voldemort;;Thanos");
		}
		if(falsehoods[2] == null)
		{
			falsehoods[2] = new FullFalsehood("The Third Falsehood Detected", // Tue Jun 17 2014 04:13:20
					new Falsehood(null, outlet3, (byte)0, (byte)0, null, (byte)4, figures.get(2), null, "source3", new Date(DATE_2014),""),
					"Voldemort;;Dumbledore;;Ministry");
		}
		if(falsehoods[3] == null)
		{
			falsehoods[3] = new FullFalsehood("The Fourth Falsehood Detected", // Sat Oct 17 2020 23:46:40
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)5, figures.get(2), null, "source4", new Date(DATE_2020),""),
					"Thanos;;Ironman;;Thor;;Serena");
		}
		if(falsehoods[4] == null)
		{
			falsehoods[4] = new FullFalsehood("The Fifth Falsehood Detected", // Mon May 05 2008 09:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)0, figures.get(1), null, "source5", new Date(DATE_2008),""),
					"Loki;;Sif;;Serena");
		}
		
		AuthFalsehoodController afController = app.getAuthFalsehoodController();
		
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[0].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[1].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[2].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[3].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[4].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[0].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[1].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[2].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[3].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[4].clone()), null);
	}
	
	@Test
	public void succeedApprove() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(0))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(1))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(2))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(3))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(4))), null);
		
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		List<Falsehood> localFalsehoods = fController.GetFalsehoodByParams(new SearchFalsehood(null,null, null, outlets, 20, null,null, null));
		
		int succeeded = 0;
		
		byte ver = FalsehoodStatus.VERIFIED.GetValue();
		
		for(Falsehood f: localFalsehoods)
		{
			if(f.getStatus() == ver) // 
				succeeded++;
		}
		System.out.println("About to test 'succeedApprove', with list that has " + localFalsehoods.size() + " items in it!");
		assertEquals(5, succeeded);
	}
	
	@Test
	public void failApprove() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		ResponseEntity<String> resp1 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(0))), null);
		ResponseEntity<String> resp2 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(1))), null);
		ResponseEntity<String> resp3 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(2))), null);
		ResponseEntity<String> resp4 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(3))), null);
		ResponseEntity<String> resp5 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(4))), null);
		
		assertTrue(resp1.getStatusCode().is4xxClientError());
		assertTrue(resp2.getStatusCode().is4xxClientError());
		assertTrue(resp3.getStatusCode().is4xxClientError());
		assertTrue(resp4.getStatusCode().is4xxClientError());
		assertTrue(resp5.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void succeedReject() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(5))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(6))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(7))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(8))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(9))), null);
		
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		List<Falsehood> localFalsehoods = fController.GetFalsehoodByParams(new SearchFalsehood(null,null, null, outlets, 20, null,null, null));
		
		int succeeded = 0;
		
		for(Falsehood f: localFalsehoods)
		{
			if(f.getStatus() == 6) // 6 is rejected
				succeeded++;
		}
		
		assertEquals(5, succeeded);
	}
	
	@Test
	public void failReject() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		ResponseEntity<String> resp1 = afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(5))), null);
		ResponseEntity<String> resp2 = afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(6))), null);
		ResponseEntity<String> resp3 = afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(7))), null);
		ResponseEntity<String> resp4 = afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(8))), null);
		ResponseEntity<String> resp5 = afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(9))), null);
		
		assertTrue(resp1.getStatusCode().is4xxClientError());
		assertTrue(resp2.getStatusCode().is4xxClientError());
		assertTrue(resp3.getStatusCode().is4xxClientError());
		assertTrue(resp4.getStatusCode().is4xxClientError());
		assertTrue(resp5.getStatusCode().is4xxClientError());
	}
}
