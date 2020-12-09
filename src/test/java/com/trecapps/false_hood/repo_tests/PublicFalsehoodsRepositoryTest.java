package com.trecapps.false_hood.repo_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.trecapps.false_hood.publicFalsehoods.*;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.repos.PublicFalsehoodRepository;

public class PublicFalsehoodsRepositoryTest {
    PublicFalsehoodRepo repo;

    static PublicFalsehood PublicFalsehoods[] = new PublicFalsehood[5];

    @BeforeAll
    static public void initializeArray()
    {
        PublicFalsehoods[0] = new PublicFalsehood(null, // Id
                null,									// CommonLie
                PublicFalsehood.SUBMITTED,				// Status
                new PublicFigure(0l, "Joseph", null, "Biden"), // Figure
                PublicFalsehood.POLITICIAN,								// Official type
                new Region(null, "Deleware", (byte)0, null),			// Region
                new Institution(null, "U.S. Senate", (byte)0, null),	// Institution
                (byte)0,					
                new Date(Calendar.getInstance().getTime().getTime()));
        
        PublicFalsehoods[1] = new PublicFalsehood(null, // Id
                null,									// CommonLie
                PublicFalsehood.VERIFIED,				// Status
                new PublicFigure(2l, "Donald", "John", "Trump"), // Figure
                PublicFalsehood.POLITICIAN,								// Official type
                new Region(null, "New York", (byte)0, null),			// Region
                new Institution(null, "U.S. White House", (byte)0, null),	// Institution
                (byte)0,					
                new Date(Calendar.getInstance().getTime().getTime()));
        
        PublicFalsehoods[2] = new PublicFalsehood(null, // Id
                null,									// CommonLie
                PublicFalsehood.SUBMITTED,				// Status
                new PublicFigure(3l, "Wilhuff ", null, "Tarkin"), // Figure
                PublicFalsehood.MILITARY,								// Official type
                new Region(null, "Death Star", (byte)0, null),			// Region
                new Institution(null, "Galactic Empire", (byte)0, null),	// Institution
                (byte)0,					
                new Date(Calendar.getInstance().getTime().getTime()));
        
        PublicFalsehoods[3] = new PublicFalsehood(null, // Id
                null,									// CommonLie
                PublicFalsehood.SUBMITTED,				// Status
                new PublicFigure(4l, "Dr.", null, "Evil"), // Figure
                PublicFalsehood.PUBLIC_HEALTH,			// Official type
                new Region(null, "Lair", (byte)0, null),			// Region
                new Institution(null, "Virtucon Industries", (byte)0, null),	// Institution
                (byte)0,					
                new Date(Calendar.getInstance().getTime().getTime()));
        
        PublicFalsehoods[4] = new PublicFalsehood(null, // Id
                null,									// CommonLie
                PublicFalsehood.SUBMITTED,				// Status
                new PublicFigure(5l, "Paul", "Robin", "Krugman"), // Figure
                PublicFalsehood.ECONOMIST,								// Official type
                new Region(null, "New Jersey", (byte)0, null),			// Region
                new Institution(null, "Princeton University", (byte)0, null),	// Institution
                (byte)0,					
                new Date(Calendar.getInstance().getTime().getTime()));
    }

    @Test
    public void emptyTest()
    {
        repo = new PublicFalsehoodRepository();
        boolean exceptionCaught = false;
        try {
            repo.save(null);
        }
        catch(IllegalArgumentException e)
        {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        assertEquals(0, repo.count());
    }

    @Test
    public void singleTest()
    {
        repo = new PublicFalsehoodRepository();

        PublicFalsehood f = repo.save(PublicFalsehoods[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new PublicFalsehoodRepository();
    	
    	PublicFalsehood f = repo.save(PublicFalsehoods[0]);
    	
    	assertTrue(f.getId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new PublicFalsehoodRepository();
    	
    	repo.save(PublicFalsehoods[0]);
    	repo.save(PublicFalsehoods[1]);
    	repo.save(PublicFalsehoods[2]);
    	repo.save(PublicFalsehoods[3]);
    	repo.save(PublicFalsehoods[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new PublicFalsehoodRepository();
    	
    	repo.save(PublicFalsehoods[0]);
    	repo.save(PublicFalsehoods[1]);
    	repo.save(PublicFalsehoods[2]);
    	repo.save(PublicFalsehoods[3]);
    	repo.save(PublicFalsehoods[4]);
    	
    	assertTrue(repo.existsById(PublicFalsehoods[2].getId()));
    	
    	repo.delete(PublicFalsehoods[2]);
    	
    	assertFalse(repo.existsById(PublicFalsehoods[2].getId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new PublicFalsehoodRepository();
    	
    	repo.save(PublicFalsehoods[0]);
    	repo.save(PublicFalsehoods[1]);
    	repo.save(PublicFalsehoods[2]);
    	repo.save(PublicFalsehoods[3]);
    	repo.save(PublicFalsehoods[4]);
    	
    	List<PublicFalsehood> remover = new ArrayList<PublicFalsehood>();
    	remover.add(PublicFalsehoods[2]);
    	remover.add(PublicFalsehoods[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(PublicFalsehoods[2].getId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new PublicFalsehoodRepository();
    	
    	repo.save(PublicFalsehoods[0]);
    	repo.save(PublicFalsehoods[1]);
    	repo.save(PublicFalsehoods[2]);
    	repo.save(PublicFalsehoods[3]);
    	repo.save(PublicFalsehoods[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new PublicFalsehoodRepository();
    	
    	PublicFalsehood f = repo.getOne(BigInteger.ONE);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new PublicFalsehoodRepository();
    	repo.save(PublicFalsehoods[0]);
    	repo.save(PublicFalsehoods[1]);
    	repo.save(PublicFalsehoods[2]);
    	repo.save(PublicFalsehoods[3]);
    	repo.save(PublicFalsehoods[4]);
    	
    	PublicFalsehood f = repo.getOne(BigInteger.ONE);
    	
    	assertTrue(f != null);
    	assertEquals(BigInteger.ONE, f.getId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new PublicFalsehoodRepository();
    	repo.save(PublicFalsehoods[0]);
    	repo.save(PublicFalsehoods[1]);
    	repo.save(PublicFalsehoods[2]);
    	repo.save(PublicFalsehoods[3]);
    	repo.save(PublicFalsehoods[4]);
    	
    	Optional<PublicFalsehood> optF = repo.findById(BigInteger.TWO);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new PublicFalsehoodRepository();
    	
    	Optional<PublicFalsehood> optF = repo.findById(BigInteger.TWO);
    	
    	assertTrue(optF.isEmpty());
    }
}
