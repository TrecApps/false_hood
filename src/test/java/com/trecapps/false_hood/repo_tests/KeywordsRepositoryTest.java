package com.trecapps.false_hood.repo_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.keywords.Keyword;
import com.trecapps.false_hood.keywords.KeywordRepo;
import com.trecapps.false_hood.repos.KeywordRepository;

public class KeywordsRepositoryTest {
	   KeywordRepo repo;

	    static Keyword keywords[] = new Keyword[5];

	    @BeforeClass
	    static public void initializeArray()
	    {
	        keywords[0] = new Keyword("word1", new LinkedList<Falsehood>());
	        
	        keywords[1] = new Keyword("word2", new LinkedList<Falsehood>());
	        
	        keywords[2] = new Keyword("word3", new LinkedList<Falsehood>());
	        
	        keywords[3] = new Keyword("word4", new LinkedList<Falsehood>());
	        
	        keywords[4] = new Keyword("word5", new LinkedList<Falsehood>());
	    }

	    @Test
	    public void emptyTest()
	    {
	        repo = new KeywordRepository();
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
	        repo = new KeywordRepository();

	        Keyword f = repo.save(keywords[0]);

	        assertTrue(f != null);
	        assertEquals(1, repo.count());
	    }
	    
	    @Test
	    public void idAssigned()
	    {
	    	repo = new KeywordRepository();
	    	
	    	Keyword f = repo.save(keywords[0]);
	    	
	    	assertTrue(f.getWord() != null);
	    }
	    
	    @Test
	    public void assignall()
	    {
	    	repo = new KeywordRepository();
	    	
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	assertEquals(5, repo.count());
	    }
	    
	    @Test
	    public void delete()
	    {
	    	repo = new KeywordRepository();
	    	
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	assertTrue(repo.existsById(keywords[2].getWord()));
	    	
	    	repo.delete(keywords[2]);
	    	
	    	assertFalse(repo.existsById(keywords[2].getWord()));
	    }
	    
	    @Test
	    public void deleteByList()
	    {
	    	repo = new KeywordRepository();
	    	
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	List<Keyword> remover = new ArrayList<Keyword>();
	    	remover.add(keywords[2]);
	    	remover.add(keywords[4]);
	    	
	    	repo.deleteAll(remover);
	    	
	    	assertEquals(3, repo.count());
	    	assertFalse(repo.existsById(keywords[2].getWord()));
	    }
	    
	    @Test
	    public void deleteAll()
	    {
	    	repo = new KeywordRepository();
	    	
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	assertEquals(5, repo.count());
	    	
	    	repo.deleteAll();
	    	
	    	assertEquals(0, repo.count());
	    }
	    
	    @Test
	    public void testNegativeGetOne()
	    {
	    	repo = new KeywordRepository();
	    	
	    	Keyword f = repo.getOne("word1");
	    	
	    	assertTrue(f != null);
	    }
	    
	    @Test
	    public void testPositiveGetOne()
	    {
	    	repo = new KeywordRepository();
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	Keyword f = repo.getOne("word1");
	    	
	    	assertTrue(f != null);
	    	assertEquals("word1", f.getWord());
	    }
	    
	    @Test
	    public void testfindByIdPositive()
	    {
	    	repo = new KeywordRepository();
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	Optional<Keyword> optF = repo.findById("word2");
	    	
	    	assertTrue(optF.isPresent());
	    }
	    
	    @Test
	    public void testfindByIdNegative()
	    {
	    	repo = new KeywordRepository();
	    	
	    	Optional<Keyword> optF = repo.findById("word2");
	    	
	    	assertTrue(optF.isEmpty());
	    }
}
