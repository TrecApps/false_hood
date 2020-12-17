package com.trecapps.false_hood.falsehoods_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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


@TestMethodOrder(OrderAnnotation.class)
public class FalsehoodTest {

	static FalsehoodApp sharedApp;
	
	static FullFalsehood falsehoods[] = new FullFalsehood[10];
	
	static ArrayList<String> outlets = new ArrayList<>();
	
	static List<PublicFigure> figures;
	static List<MediaOutlet> rOutlets = new ArrayList<>();
	
	static final long DATE_2010 = 1290000000000L;
	static final long DATE_2014 = 1403000000000L;
	static final long DATE_2017 = 1507900000000L;
	static final long DATE_2020 = 1603000000000L;
	static final long DATE_2008 = 1210000000000L;
	
	@BeforeAll
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
	@Order(2)
	public void searchByDates()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		SearchFalsehood search = new SearchFalsehood();
		
		search.setFrom(new Date(DATE_2017 - 50L));
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		
		search.setTo(new Date(DATE_2020 - 1L));
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setFrom(null);
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	}
	
	@Test
	@Order(2)
	public void searchByFigure()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		
		search.setAuthor(figures.get(1));
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setAuthor(figures.get(2));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
	}
	
	@Test
	@Order(2)
	public void searchByOutlet()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		
		search.setOutlet(rOutlets.get(0));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setOutlet(rOutlets.get(1));
		
		f = fController.searchFalsehoodByParams(search);
		
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
	}
	

	
	@Test
	@Order(2)
	public void searchBySeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setMinimum(Severity.HYPOCRISY);;
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setMaximum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
	}
	
	@Test
	@Order(2)
	public void searchByDatesAndSeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// Start with Minimum
		search.setMinimum(Severity.OBJECTOVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setTo(new Date(DATE_2014 - 100));
		
		f = fController.searchFalsehoodByParams(search);
		
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setTo(new Date(DATE_2014 + 100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setFrom(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Blank Slate for Maximum Severity
		search = new SearchFalsehood();
		
		search.setMaximum(Severity.MISLEADING);
		search.setTo(new Date(DATE_2020 -100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Blank Slate for minimum and maximum severity
		search = new SearchFalsehood();
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
	}
	
	@Test
	@Order(2)
	public void searchByDatesFigureAndSeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// Start with Minimum
		search.setAuthor(figures.get(0));
		search.setMinimum(Severity.OBJECTOVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setTo(new Date(DATE_2014 - 100));
		
		f = fController.searchFalsehoodByParams(search);
		
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setTo(new Date(DATE_2014 + 100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setFrom(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Blank Slate for Maximum Severity
		search = new SearchFalsehood();
		search.setAuthor(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setTo(new Date(DATE_2020 -100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Blank Slate for minimum and maximum severity
		search = new SearchFalsehood();
		search.setAuthor(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Public Figure, Before
		search = new SearchFalsehood();
		search.setAuthor(figures.get(0));
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Public Figure, Between
		search.setFrom(new Date(DATE_2014));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Public Figure, Min Severity
		search.setTo(null);
		search.setFrom(null);
		
		search.setMinimum(Severity.MISLEADING);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Public Figure, Severity
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Public Figure, Max Severity
		search.setMinimum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
	}

	@Test
	@Order(2)
	public void searchByDatesFigureAndOutlet()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// First do between, Outlet
		search.setOutlet(rOutlets.get(0));
		search.setFrom(new Date(DATE_2008));

		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());

		// now do Before, Outlet
		search.setFrom(null);
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());

		// now do Max Severity, Outlet
		search.setOutlet(rOutlets.get(1));
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		search.setTo(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Min Severity, Outlet
		search.setMinimum(Severity.OBJECTOVE_FALSEHOOD);
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Severity, Outlet
		search.setMinimum(Severity.FAULTY_LOGIC);
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Max Severity, Outlet, and Before
		search.setOutlet(rOutlets.get(1));
		search.setMaximum(Severity.LIE);
		search.setTo(new Date(DATE_2014));
		search.setMinimum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Max Severity, Outlet, and Between
		search.setFrom(new Date(DATE_2010));
		search.setOutlet(rOutlets.get(1));
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		

		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Min Severity, Outlet, Before
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		search.setMaximum(null);
		search.setTo(new Date(DATE_2017));
		search.setFrom(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Min Severity, Outlet, Between
		search.setFrom(new Date(2010));
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Severity, Outlet, Before
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		search.setFrom(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Severity, Outlet, Between
		search.setFrom(new Date(2010));
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
	}
	
	@Test
	@Order(3)
	public void succeedApprove2() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(10))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(11))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(12))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(13))), null);
		afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("", BigInteger.valueOf(14))), null);
		
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		List<Falsehood> localFalsehoods = fController.searchFalsehoodByParams(new SearchFalsehood(null,null, null, outlets, null, 0, 20, null,null,null, null));
		
		int succeeded = 0;
		
		byte ver = FalsehoodStatus.VERIFIED.GetValue();
		
		for(Falsehood f: localFalsehoods)
		{
			if(f.getStatus() == ver) // 
				succeeded++;
		}
		assertEquals(10, succeeded);
	}
	
	@Test
	@Order(3)
	public void succeedReject2() throws URISyntaxException
	{
		AuthFalsehoodController afController = sharedApp.getAuthFalsehoodController();
		
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(15))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(16))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(17))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(18))), null);
		afController.rejectFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(new VerdictSubmission("Duplicate", BigInteger.valueOf(19))), null);
		
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		List<Falsehood> localFalsehoods = fController.searchRFalsehoodByParams(new SearchFalsehood(null,null, null, outlets, null, 0, 20, null,null,null, null));
		
		int succeeded = 0;
		
		for(Falsehood f: localFalsehoods)
		{
			if(f.getStatus() > 4) // 6 is rejected
				succeeded++;
		}
		
		assertEquals(10, succeeded);
	}
	
	@Test
	@Order(4)
	public void searchByOutletAndFigure()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// First do the most basic search specified by this mehtod's purpose
		search.setOutlet(rOutlets.get(0));
		search.setAuthor(figures.get(0));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// By Max Severity
		search.setMaximum(Severity.HYPOCRISY);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// By Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// By Min Severity
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Start Before
		search.setTo(new Date(DATE_2020));
		search.setMinimum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Before, Min Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Before, Severity
		search.setMaximum(Severity.HYPOCRISY);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Before, Max Severity
		search.setMinimum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Between
		search.setFrom(new Date(DATE_2010));
		search.setMaximum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Before, Min Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Before, Severity
		search.setMaximum(Severity.HYPOCRISY);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Before, Max Severity
		search.setMinimum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
	}
	
	public static void initializeOutlets(FalsehoodApp app) throws URISyntaxException
	{
		MediaOutletService outletService = app.getOutletService();
		PublicFigureService figureService = app.getPublicFigureService();
		
		MediaOutlet outlet1 = outletService.GetMediaOutlet(0);
		outlets.add(outlet1.getName());
		rOutlets.add(outlet1);
		MediaOutlet outlet2 = outletService.GetMediaOutlet(1);
		outlets.add(outlet2.getName());
		rOutlets.add(outlet2);
		MediaOutlet outlet3 = outletService.GetMediaOutlet(2);
		outlets.add(outlet3.getName());
		rOutlets.add(outlet3);
		
		
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
		
		
		if(falsehoods[5] == null)
		{
			falsehoods[5] = new FullFalsehood("The first Falsehood Detected", // Wed Nov 17 2010 06:20:00
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)1, figures.get(0), null, "source1", new Date(DATE_2017),""),
					"Vader;;Palpatine;;Empire");
		}
		if(falsehoods[6] == null)
		{
			falsehoods[6] = new FullFalsehood("The Second Falsehood Detected", // Fri Oct 13 2017 07:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)3, figures.get(0), null, "source2", new Date(DATE_2014),""),
					"Vader;;Voldemort;;Thanos");
		}
		if(falsehoods[7] == null)
		{
			falsehoods[7] = new FullFalsehood("The Third Falsehood Detected", // Tue Jun 17 2014 04:13:20
					new Falsehood(null, outlet3, (byte)0, (byte)0, null, (byte)4, figures.get(2), null, "source3", new Date(DATE_2010),""),
					"Voldemort;;Dumbledore;;Ministry");
		}
		if(falsehoods[8] == null)
		{
			falsehoods[8] = new FullFalsehood("The Fourth Falsehood Detected", // Sat Oct 17 2020 23:46:40
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)5, figures.get(2), null, "source4", new Date(DATE_2008),""),
					"Thanos;;Ironman;;Thor;;Serena");
		}
		if(falsehoods[9] == null)
		{
			falsehoods[9] = new FullFalsehood("The Fifth Falsehood Detected", // Mon May 05 2008 09:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)0, figures.get(1), null, "source5", new Date(DATE_2020),""),
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
		
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[5].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[6].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[7].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[8].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[9].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[5].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[6].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[7].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[8].clone()), null);
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[9].clone()), null);
	}
	
	@Test
	@Order(1)
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
		
		List<Falsehood> localFalsehoods = fController.searchFalsehoodByParams(new SearchFalsehood(null,null, null, outlets, null, 0, 20, null,null,null, null));
		
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
	@Order(1)
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
	@Order(1)
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
		
		List<Falsehood> localFalsehoods = fController.searchRFalsehoodByParams(new SearchFalsehood(null,null, null, outlets, null, 0, 20, null,null,null, null));
		
		int succeeded = 0;
		
		for(Falsehood f: localFalsehoods)
		{
			if(f.getStatus() > 4) // 6 is rejected
				succeeded++;
		}
		
		assertEquals(5, succeeded);
	}
	
	@Test
	@Order(1)
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
