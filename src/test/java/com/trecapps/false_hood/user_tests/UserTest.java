package com.trecapps.false_hood.user_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.trecapps.false_hood.test_obj.FalsehoodApp;
import com.trecapps.false_hood.test_obj.UserTokens;
import com.trecapps.false_hood.users.FalsehoodUser;
import com.trecapps.false_hood.users.FalsehoodUserService;

public class UserTest {

	@Test
	public void addBasicUser()
	{
		FalsehoodApp app = new FalsehoodApp();
		
		FalsehoodUserService userService = app.getUserService();
		
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken1);
		
		assertNotNull(user);
		
		assertEquals(5, user.getCredit());
	}
	
	@Test
	public void addInvalidatedUser()
	{
		FalsehoodApp app = new FalsehoodApp();
		
		FalsehoodUserService userService = app.getUserService();
		
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken2);
		
		assertNull(user);
	}
	
	@Test
	public void addSecondUser()
	{
		FalsehoodApp app = new FalsehoodApp();
		
		FalsehoodUserService userService = app.getUserService();
		
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken3);
		
		assertNotNull(user);
		
		assertEquals(5, user.getCredit());
	}
	
	@Test
	public void addExpiredUser()
	{
		FalsehoodApp app = new FalsehoodApp();
		
		FalsehoodUserService userService = app.getUserService();
		
		FalsehoodUser user = userService.getUserFromToken(UserTokens.userToken4);
		
		assertNull(user);
	}

    @Test
    public void testCredit()
    {
    	FalsehoodApp app = new FalsehoodApp();
		
		FalsehoodUserService userService = app.getUserService();
		
		FalsehoodUser user1 = userService.getUserFromToken(UserTokens.userToken1);
		FalsehoodUser user2 = userService.getUserFromToken(UserTokens.userToken3);
		
		assertEquals(Long.valueOf(2), userService.getUserCountAboveCredibility(4));
		
		userService.adjustCredibility(user1, 10);
		
		assertEquals(Long.valueOf(1), userService.getUserCountAboveCredibility(10));
    }
}
