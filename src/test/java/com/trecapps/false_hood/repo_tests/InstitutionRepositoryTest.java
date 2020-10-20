package com.trecapps.false_hood.repo_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.trecapps.false_hood.publicFalsehoods.Institution;
import com.trecapps.false_hood.publicFalsehoods.InstitutionRepo;
import com.trecapps.false_hood.repos.InstitutionRepository;

public class InstitutionRepositoryTest {
    InstitutionRepo repo;

    static Institution regions[] = new Institution[5];

    @BeforeClass
    static public void initializeArray()
    {
        regions[0] = new Institution(null, "Deleware");
        
        regions[1] = new Institution(null, "New York");
        
        regions[2] = new Institution(null, "Death Star");
        
        regions[3] = new Institution(null, "Lair");
        
        regions[4] = new Institution(null, "New Jersey");
    }

    @Test
    public void emptyTest()
    {
        repo = new InstitutionRepository();
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
        repo = new InstitutionRepository();

        Institution f = repo.save(regions[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new InstitutionRepository();
    	
    	Institution f = repo.save(regions[0]);
    	
    	assertTrue(f.getId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new InstitutionRepository();
    	
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
    	repo = new InstitutionRepository();
    	
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
    	repo = new InstitutionRepository();
    	
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	List<Institution> remover = new ArrayList<Institution>();
    	remover.add(regions[2]);
    	remover.add(regions[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(regions[2].getId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new InstitutionRepository();
    	
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
    	repo = new InstitutionRepository();
    	
    	Institution f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new InstitutionRepository();
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	Institution f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    	assertEquals(Long.valueOf("1"), f.getId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new InstitutionRepository();
    	repo.save(regions[0]);
    	repo.save(regions[1]);
    	repo.save(regions[2]);
    	repo.save(regions[3]);
    	repo.save(regions[4]);
    	
    	Optional<Institution> optF = repo.findById(2l);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new InstitutionRepository();
    	
    	Optional<Institution> optF = repo.findById(2l);
    	
    	assertTrue(optF.isEmpty());
    }
}
