package com.trecapps.false_hood.repo_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.falsehoods.MediaOutletRepo;
import com.trecapps.false_hood.repos.MediaOutletRepository;

public class MediaOutletRepositoryTest {
    MediaOutletRepo repo;

    static MediaOutlet mediaOutlets[] = new MediaOutlet[5];

    @BeforeClass
    static public void initializeArray()
    {
        mediaOutlets[0] = new MediaOutlet(null, (short) 1998, "Fox News");
        
        mediaOutlets[1] = new MediaOutlet(null, (short) 1996, "MSNBC");
        
        mediaOutlets[2] = new MediaOutlet(null, (short) 1980, "CNN");
        
        mediaOutlets[3] = new MediaOutlet(null, (short) 1943, "ABC");
        
        mediaOutlets[4] = new MediaOutlet(null, (short) 1851, "The New York Times");
    }

    @Test
    public void emptyTest()
    {
        repo = new MediaOutletRepository();
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
        repo = new MediaOutletRepository();

        MediaOutlet f = repo.save(mediaOutlets[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new MediaOutletRepository();
    	
    	MediaOutlet f = repo.save(mediaOutlets[0]);
    	
    	assertTrue(f.getOutletId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new MediaOutletRepository();
    	
    	repo.save(mediaOutlets[0]);
    	repo.save(mediaOutlets[1]);
    	repo.save(mediaOutlets[2]);
    	repo.save(mediaOutlets[3]);
    	repo.save(mediaOutlets[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new MediaOutletRepository();
    	
    	repo.save(mediaOutlets[0]);
    	repo.save(mediaOutlets[1]);
    	repo.save(mediaOutlets[2]);
    	repo.save(mediaOutlets[3]);
    	repo.save(mediaOutlets[4]);
    	
    	assertTrue(repo.existsById(mediaOutlets[2].getOutletId()));
    	
    	repo.delete(mediaOutlets[2]);
    	
    	assertFalse(repo.existsById(mediaOutlets[2].getOutletId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new MediaOutletRepository();
    	
    	repo.save(mediaOutlets[0]);
    	repo.save(mediaOutlets[1]);
    	repo.save(mediaOutlets[2]);
    	repo.save(mediaOutlets[3]);
    	repo.save(mediaOutlets[4]);
    	
    	List<MediaOutlet> remover = new ArrayList<MediaOutlet>();
    	remover.add(mediaOutlets[2]);
    	remover.add(mediaOutlets[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(mediaOutlets[2].getOutletId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new MediaOutletRepository();
    	
    	repo.save(mediaOutlets[0]);
    	repo.save(mediaOutlets[1]);
    	repo.save(mediaOutlets[2]);
    	repo.save(mediaOutlets[3]);
    	repo.save(mediaOutlets[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new MediaOutletRepository();
    	
    	MediaOutlet f = repo.getOne((Integer)1);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new MediaOutletRepository();
    	repo.save(mediaOutlets[0]);
    	repo.save(mediaOutlets[1]);
    	repo.save(mediaOutlets[2]);
    	repo.save(mediaOutlets[3]);
    	repo.save(mediaOutlets[4]);
    	
    	MediaOutlet f = repo.getOne((Integer)1);
    	
    	assertTrue(f != null);
    	assertEquals((Integer)1, f.getOutletId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new MediaOutletRepository();
    	repo.save(mediaOutlets[0]);
    	repo.save(mediaOutlets[1]);
    	repo.save(mediaOutlets[2]);
    	repo.save(mediaOutlets[3]);
    	repo.save(mediaOutlets[4]);
    	
    	Optional<MediaOutlet> optF = repo.findById((Integer)2);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new MediaOutletRepository();
    	
    	Optional<MediaOutlet> optF = repo.findById((Integer)2);
    	
    	assertTrue(optF.isEmpty());
    }
}
