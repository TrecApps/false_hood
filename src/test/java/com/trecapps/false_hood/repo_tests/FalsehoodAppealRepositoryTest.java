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

import com.trecapps.false_hood.appeals.FalsehoodAppeal;
import com.trecapps.false_hood.appeals.FalsehoodAppealRepo;
import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.repos.FalsehoodAppealRepository;

public class FalsehoodAppealRepositoryTest {
    FalsehoodAppealRepo repo;

    static FalsehoodAppeal falsehoodAppeals[] = new FalsehoodAppeal[5];

    @BeforeClass
    static public void initializeArray()
    {
        falsehoodAppeals[0] = new FalsehoodAppeal(null, new Falsehood(), null, "REJECTED", null);
        
        falsehoodAppeals[1] = new FalsehoodAppeal(null, new Falsehood(), null, "REJECTED", null);
        
        falsehoodAppeals[2] = new FalsehoodAppeal(null, new Falsehood(), null, "REJECTED", null);
        
        falsehoodAppeals[3] = new FalsehoodAppeal(null, new Falsehood(), null, "REJECTED", null);
        
        falsehoodAppeals[4] = new FalsehoodAppeal(null, new Falsehood(), null, "REJECTED", null);
    }

    @Test
    public void emptyTest()
    {
        repo = new FalsehoodAppealRepository();
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
        repo = new FalsehoodAppealRepository();

        FalsehoodAppeal f = repo.save(falsehoodAppeals[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new FalsehoodAppealRepository();
    	
    	FalsehoodAppeal f = repo.save(falsehoodAppeals[0]);
    	
    	assertTrue(f.getId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new FalsehoodAppealRepository();
    	
    	repo.save(falsehoodAppeals[0]);
    	repo.save(falsehoodAppeals[1]);
    	repo.save(falsehoodAppeals[2]);
    	repo.save(falsehoodAppeals[3]);
    	repo.save(falsehoodAppeals[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new FalsehoodAppealRepository();
    	
    	repo.save(falsehoodAppeals[0]);
    	repo.save(falsehoodAppeals[1]);
    	repo.save(falsehoodAppeals[2]);
    	repo.save(falsehoodAppeals[3]);
    	repo.save(falsehoodAppeals[4]);
    	
    	assertTrue(repo.existsById(falsehoodAppeals[2].getId()));
    	
    	repo.delete(falsehoodAppeals[2]);
    	
    	assertFalse(repo.existsById(falsehoodAppeals[2].getId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new FalsehoodAppealRepository();
    	
    	repo.save(falsehoodAppeals[0]);
    	repo.save(falsehoodAppeals[1]);
    	repo.save(falsehoodAppeals[2]);
    	repo.save(falsehoodAppeals[3]);
    	repo.save(falsehoodAppeals[4]);
    	
    	List<FalsehoodAppeal> remover = new ArrayList<FalsehoodAppeal>();
    	remover.add(falsehoodAppeals[2]);
    	remover.add(falsehoodAppeals[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(falsehoodAppeals[2].getId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new FalsehoodAppealRepository();
    	
    	repo.save(falsehoodAppeals[0]);
    	repo.save(falsehoodAppeals[1]);
    	repo.save(falsehoodAppeals[2]);
    	repo.save(falsehoodAppeals[3]);
    	repo.save(falsehoodAppeals[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new FalsehoodAppealRepository();
    	
    	FalsehoodAppeal f = repo.getOne(BigInteger.ONE);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new FalsehoodAppealRepository();
    	repo.save(falsehoodAppeals[0]);
    	repo.save(falsehoodAppeals[1]);
    	repo.save(falsehoodAppeals[2]);
    	repo.save(falsehoodAppeals[3]);
    	repo.save(falsehoodAppeals[4]);
    	
    	FalsehoodAppeal f = repo.getOne(BigInteger.ONE);
    	
    	assertTrue(f != null);
    	assertEquals(BigInteger.ONE, f.getId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new FalsehoodAppealRepository();
    	repo.save(falsehoodAppeals[0]);
    	repo.save(falsehoodAppeals[1]);
    	repo.save(falsehoodAppeals[2]);
    	repo.save(falsehoodAppeals[3]);
    	repo.save(falsehoodAppeals[4]);
    	
    	Optional<FalsehoodAppeal> optF = repo.findById(BigInteger.TWO);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new FalsehoodAppealRepository();
    	
    	Optional<FalsehoodAppeal> optF = repo.findById(BigInteger.TWO);
    	
    	assertTrue(optF.isEmpty());
    }
}
