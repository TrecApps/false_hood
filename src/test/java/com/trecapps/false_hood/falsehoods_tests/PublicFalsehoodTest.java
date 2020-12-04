package com.trecapps.false_hood.falsehoods_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.trecapps.false_hood.controllers.AuthPublicFalsehoodController;
import com.trecapps.false_hood.controllers.FalsehoodController;
import com.trecapps.false_hood.controllers.PublicFalsehoodController;
import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.SearchFalsehood;
import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.publicFalsehoods.FullPublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.Institution;
import com.trecapps.false_hood.publicFalsehoods.PublicAttributeService;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;
import com.trecapps.false_hood.publicFalsehoods.Region;
import com.trecapps.false_hood.publicFalsehoods.SearchPublicFalsehood;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.publicFigure.PublicFigureService;
import com.trecapps.false_hood.resource_tests.InstitutionTest;
import com.trecapps.false_hood.resource_tests.PublicFigureTest;
import com.trecapps.false_hood.resource_tests.RegionTest;
import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class PublicFalsehoodTest {
	static FalsehoodApp sharedApp;
	
	static FullPublicFalsehood[] falsehoods = new FullPublicFalsehood[5];
	
	static List<PublicFigure> figures;
	static List<Region> regions = new LinkedList<>();
	static List<Institution> institutions = new LinkedList<>();
	
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
		userService.adjustCredibility(user, 70);
		
		RegionTest.initializePublicFigures(sharedApp);
		InstitutionTest.initializePublicFigures(sharedApp);
		PublicFigureTest.initializePublicFigures(sharedApp);
		
		initializeFalsehoods(sharedApp);
		
	}
	
	@Test
	public void searchByDates()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		search.setFrom(new Date(DATE_2017 - 50L));
		List<PublicFalsehood> f = fController.GetFalsehoodByParams(search);
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
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		List<PublicFigure> localFigures = new ArrayList<>();
		localFigures.add(figures.get(1));
		search.setAuthors(localFigures);
		
		List<PublicFalsehood> f = fController.GetFalsehoodByParams(search);
		
		assertEquals(4, f.size());
		
		localFigures.add(figures.get(2));
		search.setAuthors(localFigures);
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(6, f.size());
	}
	
	@Test
	public void searchByTerms()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		search.setTerms("Vader Serena");
		
		List<PublicFalsehood> f = fController.GetFalsehoodByParams(search);
		
		assertEquals(8, f.size());
		
		search.setTerms("Dumbledore Voldemort");
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(4, f.size());
	}
	
	@Test
	public void searchBySeverity()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		
		List<PublicFalsehood> f = fController.GetFalsehoodByParams(search);
		
		assertEquals(8, f.size());
		
		search.setMinimum(Severity.HYPOCRISY);
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(4, f.size());
		
		search.setMaximum(null);
		
		f = fController.GetFalsehoodByParams(search);
		
		assertEquals(6, f.size());
	}
	
	@Test
	public void searchByInstitutions()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		List<Institution> localInts = new ArrayList<>();
		localInts.add(institutions.get(0));
		
		search.setInstitutions(localInts);
		
		List<PublicFalsehood> f = fController.GetFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		localInts.add(institutions.get(1));
		
		f = fController.GetFalsehoodByParams(search);
		assertEquals(6, f.size());
	}
	
	public static void initializeFalsehoods(FalsehoodApp app) throws URISyntaxException
	{
		PublicAttributeService attService = app.getAttService();
		PublicFigureService figureService = app.getPublicFigureService();
		
		figures = figureService.getPublicFigures(true, 0, 10);
		
		institutions.add(attService.getInstitution(0L).getInstitution());
		institutions.add(attService.getInstitution(1L).getInstitution());
		institutions.add(attService.getInstitution(2L).getInstitution());
		
		regions.add(attService.getRegion(0L).getRegion());
		regions.add(attService.getRegion(1L).getRegion());
		regions.add(attService.getRegion(2L).getRegion());
		
		if(falsehoods[0] == null)
			falsehoods[0] = new FullPublicFalsehood("Public Falsehood 1",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.POLITICIAN, regions.get(1), institutions.get(2), (byte)1, new Date(DATE_2010)),
					"Vader;;Palpatine;;Empire");
		if(falsehoods[1] == null)
			falsehoods[1] = new FullPublicFalsehood("Public Falsehood 2",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.LAW_ENFORCEMENT, regions.get(2), institutions.get(0), (byte)3, new Date(DATE_2017)),
					"Vader;;Voldemort;;Thanos");
		if(falsehoods[2] == null)
			falsehoods[2] = new FullPublicFalsehood("Public Falsehood 3",
					new PublicFalsehood(null, null, (byte)0, figures.get(2), PublicFalsehood.ECONOMIST, regions.get(0), institutions.get(1), (byte)4, new Date(DATE_2014)),
					"Voldemort;;Dumbledore;;Ministry");
		if(falsehoods[3] == null)
			falsehoods[3] = new FullPublicFalsehood("Public Falsehood 4",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.ENVIRONMENTALIST, regions.get(1), institutions.get(2), (byte)5, new Date(DATE_2020)),
					"Thanos;;Ironman;;Thor;;Serena");
		if(falsehoods[4] == null)
			falsehoods[4] = new FullPublicFalsehood("Public Falsehood 5",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.INTELLIGENCE, regions.get(2), institutions.get(0), (byte)0, new Date(DATE_2008)),
					"Loki;;Sif;;Serena");
		
		AuthPublicFalsehoodController afController = app.getAuthPFalsehoodController();
		
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[0].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[1].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[2].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[3].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[4].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[0].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[1].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[2].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[3].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[4].clone()));
	}
	
	@Test
	public void succeedApprove() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
		FullPublicFalsehood update = falsehoods[0].clone();
		update.getMetadata().setId(BigInteger.valueOf(0));
		update.getMetadata().setStatus((byte)1);
		
		ResponseEntity<String> resp = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		
		update = falsehoods[1].clone();
		update.getMetadata().setId(BigInteger.valueOf(1));
		update.getMetadata().setStatus((byte)1);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		update = falsehoods[2].clone();
		update.getMetadata().setId(BigInteger.valueOf(2));
		update.getMetadata().setStatus((byte)1);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		update = falsehoods[3].clone();
		update.getMetadata().setId(BigInteger.valueOf(3));
		update.getMetadata().setStatus((byte)1);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		update = falsehoods[4].clone();
		update.getMetadata().setId(BigInteger.valueOf(4));
		update.getMetadata().setStatus((byte)1);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		List<PublicFalsehood> localFalsehoods = fController.GetFalsehoodByParams(new SearchPublicFalsehood(null,null, null, regions, null, 20, null,null, null, null));
		
		System.out.println("In Test with number of falsehoods: " + localFalsehoods.size());
		
		int succeeded = 0;
		
		for(PublicFalsehood f: localFalsehoods)
		{
			if(f.getStatus() == 1) // 1 is approved
				succeeded++;
		}
		
		assertEquals(5, succeeded);
	}
	
	@Test
	public void failApprove() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
		FullPublicFalsehood update = falsehoods[0].clone();
		update.getMetadata().setId(BigInteger.valueOf(5));
		update.getMetadata().setStatus((byte)1);
		
		ResponseEntity<String> resp1 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[1].clone();
		update.getMetadata().setId(BigInteger.valueOf(6));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp2 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[2].clone();
		update.getMetadata().setId(BigInteger.valueOf(7));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp3 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[3].clone();
		update.getMetadata().setId(BigInteger.valueOf(8));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp4 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[4].clone();
		update.getMetadata().setId(BigInteger.valueOf(9));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp5 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		
		assertTrue(resp1.getStatusCode().is4xxClientError());
		assertTrue(resp2.getStatusCode().is4xxClientError());
		assertTrue(resp3.getStatusCode().is4xxClientError());
		assertTrue(resp4.getStatusCode().is4xxClientError());
		assertTrue(resp5.getStatusCode().is4xxClientError());
	}
	
	@Test
	public void succeedReject() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
		FullPublicFalsehood update = falsehoods[0].clone();
		update.getMetadata().setId(BigInteger.valueOf(5));
		update.getMetadata().setStatus((byte)6);
		
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		update = falsehoods[1].clone();
		update.getMetadata().setId(BigInteger.valueOf(6));
		update.getMetadata().setStatus((byte)6);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		update = falsehoods[2].clone();
		update.getMetadata().setId(BigInteger.valueOf(7));
		update.getMetadata().setStatus((byte)6);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		update = falsehoods[3].clone();
		update.getMetadata().setId(BigInteger.valueOf(8));
		update.getMetadata().setStatus((byte)6);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		update = falsehoods[4].clone();
		update.getMetadata().setId(BigInteger.valueOf(9));
		update.getMetadata().setStatus((byte)6);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(update));
		
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		List<PublicFalsehood> localFalsehoods = fController.GetFalsehoodByParams(new SearchPublicFalsehood(null,null, null, regions, null, 20, null,null, null,null));
		
		int succeeded = 0;
		
		for(PublicFalsehood f: localFalsehoods)
		{
			if(f.getStatus() == 6) // 6 is rejected
				succeeded++;
		}
		
		assertEquals(5, succeeded);
	}
	
	@Test
	public void failReject() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
		FullPublicFalsehood update = falsehoods[0].clone();
		update.getMetadata().setId(BigInteger.valueOf(0));
		update.getMetadata().setStatus((byte)6);
		
		ResponseEntity<String> resp1 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[1].clone();
		update.getMetadata().setId(BigInteger.valueOf(1));
		update.getMetadata().setStatus((byte)6);
		ResponseEntity<String> resp2 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[2].clone();
		update.getMetadata().setId(BigInteger.valueOf(2));
		update.getMetadata().setStatus((byte)6);
		ResponseEntity<String> resp3 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[3].clone();
		update.getMetadata().setId(BigInteger.valueOf(3));
		update.getMetadata().setStatus((byte)6);
		ResponseEntity<String> resp4 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		update = falsehoods[4].clone();
		update.getMetadata().setId(BigInteger.valueOf(4));
		update.getMetadata().setStatus((byte)6);
		ResponseEntity<String> resp5 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(update));
		
		assertTrue(resp1.getStatusCode().is4xxClientError());
		assertTrue(resp2.getStatusCode().is4xxClientError());
		assertTrue(resp3.getStatusCode().is4xxClientError());
		assertTrue(resp4.getStatusCode().is4xxClientError());
		assertTrue(resp5.getStatusCode().is4xxClientError());
	}
}
