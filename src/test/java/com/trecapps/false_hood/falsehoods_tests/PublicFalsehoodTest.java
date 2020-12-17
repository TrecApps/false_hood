package com.trecapps.false_hood.falsehoods_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import com.trecapps.false_hood.controllers.AuthPublicFalsehoodController;
import com.trecapps.false_hood.controllers.FalsehoodController;
import com.trecapps.false_hood.controllers.PublicFalsehoodController;
import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.SearchFalsehood;
import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.miscellanous.VerdictSubmission;
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

@TestMethodOrder(OrderAnnotation.class)
public class PublicFalsehoodTest {
	static FalsehoodApp sharedApp;
	
	static FullPublicFalsehood[] falsehoods = new FullPublicFalsehood[10];
	
	static List<PublicFigure> figures;
	static List<Region> regions = new LinkedList<>();
	static List<Institution> institutions = new LinkedList<>();
	
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
		userService.adjustCredibility(user, 70);
		
		RegionTest.initializePublicFigures(sharedApp);
		InstitutionTest.initializePublicFigures(sharedApp);
		PublicFigureTest.initializePublicFigures(sharedApp);
		
		initializeFalsehoods(sharedApp);
		
	}
	
	@Test
	@Order(3)
	public void searchByDates()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		search.setFrom(new Date(DATE_2017 - 50L));
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setTo(new Date(DATE_2020 - 1L));
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setFrom(null);
		f = fController.searchFalsehoodByParams(search);
		assertEquals(8, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(8, f.size());
	}
	
	@Test
	@Order(3)
	public void searchByFigure()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		search.setOfficial(figures.get(1));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setOfficial(figures.get(2));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
	}
	

	
	@Test
	@Order(3)
	public void searchBySeverity()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(8, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(8, f.size());
		
		search.setMinimum(Severity.HYPOCRISY);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setMaximum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(6, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(6, f.size());
	}
	
	@Test
	@Order(3)
	public void searchByInstitutions()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		
		search.setInstitution(institutions.get(0));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setInstitution(institutions.get(1));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
	}
	
	@Test
	@Order(3)
	public void searchByDatesAndSeverity()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		// Start with Minimum
		search.setMinimum(Severity.OBJECTOVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		search.setTo(new Date(DATE_2014 - 100));
		
		f = fController.searchFalsehoodByParams(search);
		
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setTo(new Date(DATE_2014 + 100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		search.setFrom(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Blank Slate for Maximum Severity
		search = new SearchPublicFalsehood();
		
		search.setMaximum(Severity.MISLEADING);
		search.setTo(new Date(DATE_2020 -100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(5, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(5, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Blank Slate for minimum and maximum severity
		search = new SearchPublicFalsehood();
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
	}
	
	@Test
	@Order(3)
	public void searchByDatesFigureAndSeverity()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		// Start with Minimum
		search.setOfficial(figures.get(0));
		search.setMinimum(Severity.OBJECTOVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
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
		search = new SearchPublicFalsehood();
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setTo(new Date(DATE_2020 -100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Blank Slate for minimum and maximum severity
		search = new SearchPublicFalsehood();
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		
		
		
		search = new SearchPublicFalsehood();
		
		// Start with Minimum, No Time Constraints this time
		search.setOfficial(figures.get(0));
		search.setMinimum(Severity.OBJECTOVE_FALSEHOOD);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Blank Slate for Maximum Severity, No Time Constraints this time
		search = new SearchPublicFalsehood();
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
	
		
		// Blank Slate for minimum and maximum severity, No Time Constraints this time
		search = new SearchPublicFalsehood();
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		
		// Before, Official
		search = new SearchPublicFalsehood();
		search.setOfficial(figures.get(1));
		search.setTo(new Date(DATE_2014));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Between, Official
		search.setFrom(new Date(DATE_2010));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Max Severity, Official
		search.setTo(null);
		search.setFrom(null);
		search.setMaximum(Severity.OBJECTOVE_FALSEHOOD);
		
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Severity, Official
		search.setMinimum(Severity.HYPOCRISY);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Min Severity, Official
		search.setMaximum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	}
	
	@Test
	@Order(3)
	public void searchByRegionsNoInstitutions()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		// Region And Public Figure
		search.setRegion(regions.get(1));
		search.setOfficial(figures.get(0));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setOfficial(figures.get(1));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Region, Public FIgure, Before
		search.setOfficial(figures.get(0));
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Public Figure, Between
		search.setFrom(new Date(DATE_2014));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Min Severity
		search.setFrom(null);
		search.setTo(null);
		search.setMinimum(Severity.FAULTY_LOGIC);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Public Figure, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Before, Max Severity
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Before, Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Before, Min Severity
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	
		// Region, Public Figure, Between, Min Severity
		search.setFrom(new Date(DATE_2010));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Region, Public Figure, Between, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Region, Public Figure, Between, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		///////// No Public FIgure now //////////////////
		search = new SearchPublicFalsehood();
		
		// Region
		search.setRegion(regions.get(1));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		
		// Region, Before
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Between
		search.setFrom(new Date(DATE_2014));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Min Severity
		search.setFrom(null);
		search.setTo(null);
		search.setMinimum(Severity.FAULTY_LOGIC);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Before, Max Severity
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Before, Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Before, Min Severity
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	
		// Region, Between, Min Severity
		search.setFrom(new Date(DATE_2010));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Region, Between, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Region, Between, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
	}
	
	@Test
	@Order(3)
	public void searchByRegionsInstitutions()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		// Region And Public Figure
		search.setRegion(regions.get(1));
		search.setInstitution(institutions.get(2));
		search.setOfficial(figures.get(0));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setOfficial(figures.get(1));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Region, Public FIgure, Before
		search.setOfficial(figures.get(0));
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Public Figure, Between
		search.setFrom(new Date(DATE_2014));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Min Severity
		search.setFrom(null);
		search.setTo(null);
		search.setMinimum(Severity.FAULTY_LOGIC);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Public Figure, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Before, Max Severity
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Before, Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Public Figure, Before, Min Severity
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	
		// Region, Public Figure, Between, Min Severity
		search.setFrom(new Date(DATE_2010));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Region, Public Figure, Between, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Region, Public Figure, Between, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		///////// No Public FIgure now //////////////////
		search = new SearchPublicFalsehood();
		
		// Region
		search.setRegion(regions.get(1));

		search.setInstitution(institutions.get(2));
		

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Before
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Between
		search.setFrom(new Date(DATE_2014));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Min Severity
		search.setFrom(null);
		search.setTo(null);
		search.setMinimum(Severity.FAULTY_LOGIC);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Region, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Before, Max Severity
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Before, Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Region, Before, Min Severity
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	
		// Region, Between, Min Severity
		search.setFrom(new Date(DATE_2010));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Region, Between, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Region, Between, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
	}
	
	@Test
	@Order(3)
	public void searchByInstitutionsNoRegions()
	{
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		
		// Institution And Public Figure
		search.setInstitution(institutions.get(2));
		search.setOfficial(figures.get(0));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		search.setOfficial(figures.get(1));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		
		// Institution, Public FIgure, Before
		search.setOfficial(figures.get(0));
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Institution, Public Figure, Between
		search.setFrom(new Date(DATE_2014));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Public Figure, Min Severity
		search.setFrom(null);
		search.setTo(null);
		search.setMinimum(Severity.FAULTY_LOGIC);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Institution, Public Figure, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Public Figure, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Public Figure, Before, Max Severity
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Public Figure, Before, Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Public Figure, Before, Min Severity
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	
		// Institution, Public Figure, Between, Min Severity
		search.setFrom(new Date(DATE_2010));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Institution, Public Figure, Between, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Institution, Public Figure, Between, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		///////// No Public FIgure now //////////////////
		search = new SearchPublicFalsehood();
		
		// Institution
		search.setInstitution(institutions.get(2));
		
		// Institution, Before
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Institution, Between
		search.setFrom(new Date(DATE_2014));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Min Severity
		search.setFrom(null);
		search.setTo(null);
		search.setMinimum(Severity.FAULTY_LOGIC);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		
		// Institution, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Before, Max Severity
		search.setTo(new Date(DATE_2020));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Before, Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		
		// Institution, Before, Min Severity
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
	
		// Institution, Between, Min Severity
		search.setFrom(new Date(DATE_2010));

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		
		// Institution, Between, Severity
		search.setMaximum(Severity.MISLEADING);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		
		// Institution, Between, Max Severity
		search.setMinimum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
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
		
		if(falsehoods[5] == null)
			falsehoods[5] = new FullPublicFalsehood("Public Falsehood 6",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.POLITICIAN, regions.get(1), institutions.get(2), (byte)1, new Date(DATE_2020)),
					"Vader;;Palpatine;;Empire");
		if(falsehoods[6] == null)
			falsehoods[6] = new FullPublicFalsehood("Public Falsehood 7",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.LAW_ENFORCEMENT, regions.get(2), institutions.get(1), (byte)3, new Date(DATE_2014)),
					"Vader;;Voldemort;;Thanos");
		if(falsehoods[7] == null)
			falsehoods[7] = new FullPublicFalsehood("Public Falsehood 8",
					new PublicFalsehood(null, null, (byte)0, figures.get(2), PublicFalsehood.ECONOMIST, regions.get(0), institutions.get(0), (byte)4, new Date(DATE_2017)),
					"Voldemort;;Dumbledore;;Ministry");
		if(falsehoods[8] == null)
			falsehoods[8] = new FullPublicFalsehood("Public Falsehood 9",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.ENVIRONMENTALIST, regions.get(1), institutions.get(2), (byte)5, new Date(DATE_2008)),
					"Thanos;;Ironman;;Thor;;Serena");
		if(falsehoods[9] == null)
			falsehoods[9] = new FullPublicFalsehood("Public Falsehood 10",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.INTELLIGENCE, regions.get(2), institutions.get(0), (byte)0, new Date(DATE_2010)),
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
		
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[5].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[6].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[7].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[8].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[9].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[5].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[6].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[7].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[8].clone()));
		afController.insertFalsehood(RequestEntity.post(new URI("/AddOutlet")).header("Authorization", UserTokens.userToken1).body(falsehoods[9].clone()));
	}
	
	@Test
	@Order(1)
	public void succeedApprove() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
		FullPublicFalsehood update = falsehoods[0].clone();
		update.getMetadata().setId(BigInteger.valueOf(0));
		update.getMetadata().setStatus((byte)1);
		
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
		
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		List<PublicFalsehood> localFalsehoods = fController.searchFalsehoodByParams(new SearchPublicFalsehood(null,null, null, regions, null, 20, null,null, null, null));
		
		System.out.println("In Test with number of falsehoods: " + localFalsehoods.size());
		
		
		assertEquals(10, localFalsehoods.size());
	}
	
	@Test
	@Order(1)
	public void failApprove() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
		FullPublicFalsehood update = falsehoods[0].clone();
		update.getMetadata().setId(BigInteger.valueOf(5));
		update.getMetadata().setStatus((byte)1);
		
		ResponseEntity<String> resp1 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(0))), null);
		update = falsehoods[1].clone();
		update.getMetadata().setId(BigInteger.valueOf(6));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp2 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(1))), null);
		update = falsehoods[2].clone();
		update.getMetadata().setId(BigInteger.valueOf(7));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp3 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(2))), null);
		update = falsehoods[3].clone();
		update.getMetadata().setId(BigInteger.valueOf(8));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp4 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(3))), null);
		update = falsehoods[4].clone();
		update.getMetadata().setId(BigInteger.valueOf(9));
		update.getMetadata().setStatus((byte)1);
		ResponseEntity<String> resp5 = afController.approveFalsehood(RequestEntity.post(
				new URI("/AddOutlet")).header("Authorization", UserTokens.userToken3).body(new VerdictSubmission("", BigInteger.valueOf(4))), null);
		
		assertTrue(resp1.getStatusCode().is4xxClientError());
		assertTrue(resp2.getStatusCode().is4xxClientError());
		assertTrue(resp3.getStatusCode().is4xxClientError());
		assertTrue(resp4.getStatusCode().is4xxClientError());
		assertTrue(resp5.getStatusCode().is4xxClientError());
	}
	
	@Test
	@Order(2)
	public void succeedReject() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
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
		
		PublicFalsehoodController fController = sharedApp.getpFalsehoodController();
		
		List<PublicFalsehood> localFalsehoods = fController.searchRFalsehoodByParams(new SearchPublicFalsehood(null,null, null, null, null, 20, null,null, null,null));
		
		
		assertEquals(10, localFalsehoods.size());
	}
	
	@Test
	@Order(2)
	public void failReject() throws URISyntaxException
	{
		AuthPublicFalsehoodController afController = sharedApp.getAuthPFalsehoodController();
		
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
