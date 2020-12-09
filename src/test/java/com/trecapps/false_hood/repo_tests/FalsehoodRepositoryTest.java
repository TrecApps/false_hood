package com.trecapps.false_hood.repo_tests;


import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodRepo;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.miscellanous.FalsehoodStatus;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.repos.FalsehoodRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FalsehoodRepositoryTest {

    FalsehoodRepo repo;

    static Falsehood falsehoods[] = new Falsehood[5];

    @BeforeAll
    static public void initializeArray()
    {
        falsehoods[0] = new Falsehood(null,
                new MediaOutlet(0, (short) 1998, "Fox News",(byte)0, null),
                FalsehoodStatus.SUBMITTED.GetValue(),
                (byte)1,
                null,
                Falsehood.LIE,
                new PublicFigure(0l, "Sean", null, "Hannity"),
                null,
                "Hannity",
                new Date(Calendar.getInstance().getTime().getTime()),
                "");
        
        falsehoods[1] = new Falsehood(null,
                new MediaOutlet(0, (short) 1998, "Fox News",(byte)0, null),
                FalsehoodStatus.VERIFIED.GetValue(),
                (byte)1,
                null,
                Falsehood.LIE,
                new PublicFigure(1l, "Tucker", null, "Carlson"),
                null,
                "Carlson Tonight",
                new Date(Calendar.getInstance().getTime().getTime()),
                "");
        
        falsehoods[2] = new Falsehood(null,
                new MediaOutlet(0, (short) 1996, "MSNBC",(byte)0, null),
                FalsehoodStatus.SUBMITTED.GetValue(),
                (byte)1,
                null,
                Falsehood.LIE,
                new PublicFigure(2l, "Rachel", null, "Maddow"),
                null,
                "The Rachel Maddow Show",
                new Date(Calendar.getInstance().getTime().getTime()),
                "");
        
        falsehoods[3] = new Falsehood(null,
                new MediaOutlet(0, (short) 1998, "CNN",(byte)0, null),
                FalsehoodStatus.VERIFIED.GetValue(),
                (byte)1,
                null,
                Falsehood.LIE,
                new PublicFigure(1l, "Jake", null, "Tapper"),
                null,
                "Carlson Tonight",
                new Date(Calendar.getInstance().getTime().getTime()),
                "");
        
        falsehoods[4] = new Falsehood(null,
                new MediaOutlet(0, (short) 1996, "MSNBC",(byte)0, null),
                FalsehoodStatus.SUBMITTED.GetValue(),
                (byte)1,
                null,
                Falsehood.LIE,
                new PublicFigure(2l, "Chuck", null, "Todd"),
                null,
                "The Rachel Maddow Show",
                new Date(Calendar.getInstance().getTime().getTime()),
                "");
    }

    @Test
    public void emptyTest()
    {
        repo = new FalsehoodRepository();
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
        repo = new FalsehoodRepository();

        Falsehood f = repo.save(falsehoods[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new FalsehoodRepository();
    	
    	Falsehood f = repo.save(falsehoods[0]);
    	
    	assertTrue(f.getId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new FalsehoodRepository();
    	
    	repo.save(falsehoods[0]);
    	repo.save(falsehoods[1]);
    	repo.save(falsehoods[2]);
    	repo.save(falsehoods[3]);
    	repo.save(falsehoods[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new FalsehoodRepository();
    	
    	repo.save(falsehoods[0]);
    	repo.save(falsehoods[1]);
    	repo.save(falsehoods[2]);
    	repo.save(falsehoods[3]);
    	repo.save(falsehoods[4]);
    	
    	assertTrue(repo.existsById(falsehoods[2].getId()));
    	
    	repo.delete(falsehoods[2]);
    	
    	assertFalse(repo.existsById(falsehoods[2].getId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new FalsehoodRepository();
    	
    	repo.save(falsehoods[0]);
    	repo.save(falsehoods[1]);
    	repo.save(falsehoods[2]);
    	repo.save(falsehoods[3]);
    	repo.save(falsehoods[4]);
    	
    	List<Falsehood> remover = new ArrayList<Falsehood>();
    	remover.add(falsehoods[2]);
    	remover.add(falsehoods[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(falsehoods[2].getId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new FalsehoodRepository();
    	
    	repo.save(falsehoods[0]);
    	repo.save(falsehoods[1]);
    	repo.save(falsehoods[2]);
    	repo.save(falsehoods[3]);
    	repo.save(falsehoods[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new FalsehoodRepository();
    	
    	Falsehood f = repo.getOne(BigInteger.ONE);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new FalsehoodRepository();
    	repo.save(falsehoods[0]);
    	repo.save(falsehoods[1]);
    	repo.save(falsehoods[2]);
    	repo.save(falsehoods[3]);
    	repo.save(falsehoods[4]);
    	
    	Falsehood f = repo.getOne(BigInteger.ONE);
    	
    	assertTrue(f != null);
    	assertEquals(BigInteger.ONE, f.getId());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new FalsehoodRepository();
    	repo.save(falsehoods[0]);
    	repo.save(falsehoods[1]);
    	repo.save(falsehoods[2]);
    	repo.save(falsehoods[3]);
    	repo.save(falsehoods[4]);
    	
    	Optional<Falsehood> optF = repo.findById(BigInteger.TWO);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new FalsehoodRepository();
    	
    	Optional<Falsehood> optF = repo.findById(BigInteger.TWO);
    	
    	assertTrue(optF.isEmpty());
    }
}
