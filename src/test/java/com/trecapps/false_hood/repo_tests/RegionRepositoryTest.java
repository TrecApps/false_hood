package com.trecapps.false_hood.repo_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.trecapps.false_hood.publicFalsehoods.*;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.repos.RegionRepository;

public class RegionRepositoryTest {
    RegionRepo repo;

    static Region regions[] = new Region[5];

    @BeforeClass
    static public void initializeArray()
    {
        regions[0] = new Region(null, "Deleware", (byte)0, null);
        
        regions[1] = new Region(null, "New York", (byte)0, null);
        
        regions[2] = new Region(null, "Death Star", (byte)0, null);
        
        regions[3] = new Region(null, "Lair", (byte)0, null);
        
        regions[4] = new Region(null, "New Jersey", (byte)0, null);
    }

    @Test
    public void emptyTest()
    {
        repo = new RegionRepository();
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
        repo = new RegionRepository();

        Region f = repo.save(regions[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new RegionRepository();
    	
    	Region f = repo.save(regions[0]);
    	
    	assertTrue(f.getId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new RegionRepository();
    	
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new RegionRepository();
    	
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	assertTrue(repo.existsById(regions[2].getId()));
    	
    	repo.delete(regions[2]);
    	
    	assertFalse(repo.existsById(regions[2].getId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new RegionRepository();
    	
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	List<Region> remover = new ArrayList<Region>();
    	remover.add(regions[2]);
    	remover.add(regions[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(regions[2].getId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new RegionRepository();
    	
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new RegionRepository();
    	
    	Region f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new RegionRepository();
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	Region f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    	assertEquals(Long.valueOf("1"), f.getId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new RegionRepository();
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	Optional<Region> optF = repo.findById(2l);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new RegionRepository();
    	
    	Optional<Region> optF = repo.findById(2l);
    	
    	assertTrue(optF.isEmpty());
    }
}
