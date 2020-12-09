package com.trecapps.false_hood.repo_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.publicFigure.PublicFigureRepo;
import com.trecapps.false_hood.repos.PublicFigureRepository;

public class PublicFigureRepositoryTest {
    PublicFigureRepo repo;

    static PublicFigure publicFigures[] = new PublicFigure[5];

    @BeforeAll
    static public void initializeArray()
    {
        publicFigures[0] = new PublicFigure(null, "Sean", null, "Hannity");
        
        publicFigures[1] = new PublicFigure(null, "Tucker", null, "Carlson");
        
        publicFigures[2] = new PublicFigure(null, "Rachel", null, "Maddow");
        
        publicFigures[3] = new PublicFigure(null, "Jake", null, "Tapper");
        
        publicFigures[4] = new PublicFigure(null, "Chuck", null, "Todd");
    }

    @Test
    public void emptyTest()
    {
        repo = new PublicFigureRepository();
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
        repo = new PublicFigureRepository();

        PublicFigure f = repo.save(publicFigures[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new PublicFigureRepository();
    	
    	PublicFigure f = repo.save(publicFigures[0]);
    	
    	assertTrue(f.getId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new PublicFigureRepository();
    	
    	repo.save(publicFigures[0]);
    	repo.save(publicFigures[1]);
    	repo.save(publicFigures[2]);
    	repo.save(publicFigures[3]);
    	repo.save(publicFigures[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new PublicFigureRepository();
    	
    	repo.save(publicFigures[0]);
    	repo.save(publicFigures[1]);
    	repo.save(publicFigures[2]);
    	repo.save(publicFigures[3]);
    	repo.save(publicFigures[4]);
    	
    	assertTrue(repo.existsById(publicFigures[2].getId()));
    	
    	repo.delete(publicFigures[2]);
    	
    	assertFalse(repo.existsById(publicFigures[2].getId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new PublicFigureRepository();
    	
    	repo.save(publicFigures[0]);
    	repo.save(publicFigures[1]);
    	repo.save(publicFigures[2]);
    	repo.save(publicFigures[3]);
    	repo.save(publicFigures[4]);
    	
    	List<PublicFigure> remover = new ArrayList<PublicFigure>();
    	remover.add(publicFigures[2]);
    	remover.add(publicFigures[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(publicFigures[2].getId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new PublicFigureRepository();
    	
    	repo.save(publicFigures[0]);
    	repo.save(publicFigures[1]);
    	repo.save(publicFigures[2]);
    	repo.save(publicFigures[3]);
    	repo.save(publicFigures[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new PublicFigureRepository();
    	
    	PublicFigure f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new PublicFigureRepository();
    	repo.save(publicFigures[0]);
    	repo.save(publicFigures[1]);
    	repo.save(publicFigures[2]);
    	repo.save(publicFigures[3]);
    	repo.save(publicFigures[4]);
    	
    	PublicFigure f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    	assertEquals((Long)1l, f.getId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new PublicFigureRepository();
    	repo.save(publicFigures[0]);
    	repo.save(publicFigures[1]);
    	repo.save(publicFigures[2]);
    	repo.save(publicFigures[3]);
    	repo.save(publicFigures[4]);
    	
    	Optional<PublicFigure> optF = repo.findById((Long)2l);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new PublicFigureRepository();
    	
    	Optional<PublicFigure> optF = repo.findById((Long)2l);
    	
    	assertTrue(optF.isEmpty());
    }
}
