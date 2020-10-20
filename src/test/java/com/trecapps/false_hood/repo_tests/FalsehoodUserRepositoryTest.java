package com.trecapps.false_hood.repo_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.trecapps.false_hood.repos.FalsehoodUserRepository;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserRepo;

public class FalsehoodUserRepositoryTest {

	FalsehoodUserRepo repo;
	
	static FalsehoodUser users[] = new FalsehoodUser[5];
	
    @BeforeClass
    static public void initializeArray()
    {
        users[0] = new FalsehoodUser(null, 25, "user1@gmail.com");
        
        users[1] = new FalsehoodUser(null, 25, "user2@outook.com");
        
        users[2] = new FalsehoodUser(null, 25, "user3@hotmail.com");
        
        users[3] = new FalsehoodUser(null, 25, "user4@yahoo.com");
        
        users[4] = new FalsehoodUser(null, 25, "user5@protonmail.com");
    }

    @Test
    public void emptyTest()
    {
        repo = new FalsehoodUserRepository();
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
        repo = new FalsehoodUserRepository();

        FalsehoodUser f = repo.save(users[0]);

        assertTrue(f != null);
        assertEquals(1, repo.count());
    }
    
    @Test
    public void idAssigned()
    {
    	repo = new FalsehoodUserRepository();
    	
    	FalsehoodUser f = repo.save(users[0]);
    	
    	assertTrue(f.getUserId() != null);
    }
    
    @Test
    public void assignall()
    {
    	repo = new FalsehoodUserRepository();
    	
    	repo.save(users[0]);
    	repo.save(users[1]);
    	repo.save(users[2]);
    	repo.save(users[3]);
    	repo.save(users[4]);
    	
    	assertEquals(5, repo.count());
    }
    
    @Test
    public void delete()
    {
    	repo = new FalsehoodUserRepository();
    	
    	repo.save(users[0]);
    	repo.save(users[1]);
    	repo.save(users[2]);
    	repo.save(users[3]);
    	repo.save(users[4]);
    	
    	assertTrue(repo.existsById(users[2].getUserId()));
    	
    	repo.delete(users[2]);
    	
    	assertFalse(repo.existsById(users[2].getUserId()));
    }
    
    @Test
    public void deleteByList()
    {
    	repo = new FalsehoodUserRepository();
    	
    	repo.save(users[0]);
    	repo.save(users[1]);
    	repo.save(users[2]);
    	repo.save(users[3]);
    	repo.save(users[4]);
    	
    	List<FalsehoodUser> remover = new ArrayList<FalsehoodUser>();
    	remover.add(users[2]);
    	remover.add(users[4]);
    	
    	repo.deleteAll(remover);
    	
    	assertEquals(3, repo.count());
    	assertFalse(repo.existsById(users[2].getUserId()));
    }
    
    @Test
    public void deleteAll()
    {
    	repo = new FalsehoodUserRepository();
    	
    	repo.save(users[0]);
    	repo.save(users[1]);
    	repo.save(users[2]);
    	repo.save(users[3]);
    	repo.save(users[4]);
    	
    	assertEquals(5, repo.count());
    	
    	repo.deleteAll();
    	
    	assertEquals(0, repo.count());
    }
    
    @Test
    public void testNegativeGetOne()
    {
    	repo = new FalsehoodUserRepository();
    	
    	FalsehoodUser f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    }
    
    @Test
    public void testPositiveGetOne()
    {
    	repo = new FalsehoodUserRepository();
    	repo.save(users[0]);
    	repo.save(users[1]);
    	repo.save(users[2]);
    	repo.save(users[3]);
    	repo.save(users[4]);
    	
    	FalsehoodUser f = repo.getOne(1l);
    	
    	assertTrue(f != null);
    	assertEquals(1l, f.getUserId().longValue());
    }
    
    @Test
    public void testfindByIdPositive()
    {
    	repo = new FalsehoodUserRepository();
    	repo.save(users[0]);
    	repo.save(users[1]);
    	repo.save(users[2]);
    	repo.save(users[3]);
    	repo.save(users[4]);
    	
    	Optional<FalsehoodUser> optF = repo.findById(2l);
    	
    	assertTrue(optF.isPresent());
    }
    
    @Test
    public void testfindByIdNegative()
    {
    	repo = new FalsehoodUserRepository();
    	
    	Optional<FalsehoodUser> optF = repo.findById(2l);
    	
    	assertTrue(optF.isEmpty());
    }
}
