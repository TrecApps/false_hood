package com.trecapps.false_hood.repo_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.trecapps.false_hood.publicFalsehoods.*;
import com.trecapps.false_hood.keywords.PublicKeyword;
import com.trecapps.false_hood.keywords.PublicKeywordRepo;
import com.trecapps.false_hood.repos.PublicKeywordsRepository;

public class PublicKeywordsRepositoryTest {
	 PublicKeywordRepo repo;

	    static PublicKeyword keywords[] = new PublicKeyword[5];

	    @BeforeAll
	    static public void initializeArray()
	    {
	        keywords[0] = new PublicKeyword("word1", new LinkedList<PublicFalsehood>());
	        
	        keywords[1] = new PublicKeyword("word2", new LinkedList<PublicFalsehood>());
	        
	        keywords[2] = new PublicKeyword("word3", new LinkedList<PublicFalsehood>());
	        
	        keywords[3] = new PublicKeyword("word4", new LinkedList<PublicFalsehood>());
	        
	        keywords[4] = new PublicKeyword("word5", new LinkedList<PublicFalsehood>());
	    }

	    @Test
	    public void emptyTest()
	    {
	        repo = new PublicKeywordsRepository();
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
	        repo = new PublicKeywordsRepository();

	        PublicKeyword f = repo.save(keywords[0]);

	        assertTrue(f != null);
	        assertEquals(1, repo.count());
	    }
	    
	    @Test
	    public void idAssigned()
	    {
	    	repo = new PublicKeywordsRepository();
	    	
	    	PublicKeyword f = repo.save(keywords[0]);
	    	
	    	assertTrue(f.getWord() != null);
	    }
	    
	    @Test
	    public void assignall()
	    {
	    	repo = new PublicKeywordsRepository();
	    	
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
	    	repo = new PublicKeywordsRepository();
	    	
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
	    	repo = new PublicKeywordsRepository();
	    	
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	List<PublicKeyword> remover = new ArrayList<PublicKeyword>();
	    	remover.add(keywords[2]);
	    	remover.add(keywords[4]);
	    	
	    	repo.deleteAll(remover);
	    	
	    	assertEquals(3, repo.count());
	    	assertFalse(repo.existsById(keywords[2].getWord()));
	    }
	    
	    @Test
	    public void deleteAll()
	    {
	    	repo = new PublicKeywordsRepository();
	    	
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
	    	repo = new PublicKeywordsRepository();
	    	
	    	PublicKeyword f = repo.getOne("word1");
	    	
	    	assertTrue(f != null);
	    }
	    
	    @Test
	    public void testPositiveGetOne()
	    {
	    	repo = new PublicKeywordsRepository();
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	PublicKeyword f = repo.getOne("word1");
	    	
	    	assertTrue(f != null);
	    	assertEquals("word1", f.getWord());
	    }
	    
	    @Test
	    public void testfindByIdPositive()
	    {
	    	repo = new PublicKeywordsRepository();
	    	repo.save(keywords[0]);
	    	repo.save(keywords[1]);
	    	repo.save(keywords[2]);
	    	repo.save(keywords[3]);
	    	repo.save(keywords[4]);
	    	
	    	Optional<PublicKeyword> optF = repo.findById("word2");
	    	
	    	assertTrue(optF.isPresent());
	    }
	    
	    @Test
	    public void testfindByIdNegative()
	    {
	    	repo = new PublicKeywordsRepository();
	    	
	    	Optional<PublicKeyword> optF = repo.findById("word2");
	    	
	    	assertTrue(optF.isEmpty());
	    }
}
