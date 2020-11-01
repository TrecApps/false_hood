package com.trecapps.false_hood.repo_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.commonLie.CommonLieRepo;
import com.trecapps.false_hood.repos.CommonLieRepository;

public class CommonLieRepositoryTest {
    CommonLieRepo repo;

    static CommonLie commonLies[] = new CommonLie[5];

    @BeforeClass
    static public void initializeArray()
    {
        commonLies[0] = new CommonLie(null, "Lie1");
        
        commonLies[1] = new CommonLie(null, "Lie2");
        
        commonLies[2] = new CommonLie(null, "Lie3");
        
        commonLies[3] = new CommonLie(null, "Lie4");
        
        commonLies[4] = new CommonLie(null, "Lie5");
    }

    @Test
    public void emptyTest()
    {
        repo = new CommonLieRepository();
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
        repo = new CommonLieRepository();

        CommonLie f = repo.save(commonLies[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new CommonLieRepository();
    	
    	CommonLie f = repo.save(commonLies[0]);
    	
    	assertTrue(f.getId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new CommonLieRepository();
    	
    	repo.save(commonLies[0]);
    	repo.save(commonLies[1]);
    	repo.save(commonLies[2]);
    	repo.save(commonLies[3]);
    	repo.save(commonLies[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new CommonLieRepository();
    	
    	repo.save(commonLies[0]);
    	repo.save(commonLies[1]);
    	repo.save(commonLies[2]);
    	repo.save(commonLies[3]);
    	repo.save(commonLies[4]);
    	
    	assertTrue(repo.existsById(commonLies[2].getId()));
    	
    	repo.delete(commonLies[2]);
    	
    	assertFalse(repo.existsById(commonLies[2].getId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new CommonLieRepository();
    	
    	repo.save(commonLies[0]);
    	repo.save(commonLies[1]);
    	repo.save(commonLies[2]);
    	repo.save(commonLies[3]);
    	repo.save(commonLies[4]);
    	
    	List<CommonLie> remover = new ArrayList<CommonLie>();
    	remover.add(commonLies[2]);
    	remover.add(commonLies[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(commonLies[2].getId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new CommonLieRepository();
    	
    	repo.save(commonLies[0]);
    	repo.save(commonLies[1]);
    	repo.save(commonLies[2]);
    	repo.save(commonLies[3]);
    	repo.save(commonLies[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new CommonLieRepository();
    	
    	CommonLie f = repo.getOne((Long)1l);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new CommonLieRepository();
    	repo.save(commonLies[0]);
    	repo.save(commonLies[1]);
    	repo.save(commonLies[2]);
    	repo.save(commonLies[3]);
    	repo.save(commonLies[4]);
    	
    	CommonLie f = repo.getOne((Long)1l);
    	
    	assertTrue(f != null);
    	assertEquals((Long)1l, f.getId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new CommonLieRepository();
    	repo.save(commonLies[0]);
    	repo.save(commonLies[1]);
    	repo.save(commonLies[2]);
    	repo.save(commonLies[3]);
    	repo.save(commonLies[4]);
    	
    	Optional<CommonLie> optF = repo.findById((Long)2l);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new CommonLieRepository();
    	
    	Optional<CommonLie> optF = repo.findById((Long)2l);
    	
    	assertTrue(optF.isEmpty());
    }
    
}
