package com.trecapps.false_hood.repo_tests;


import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.falsehoods.FalsehoodRepo;
import com.trecapps.false_hood.falsehoods.MediaOutlet;
import com.trecapps.false_hood.publicFigure.PublicFigure;
import com.trecapps.false_hood.repos.FalsehoodRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FalsehoodRepositoryTest {

    FalsehoodRepo repo;

    static Falsehood falsehoods[] = new Falsehood[5];

    @BeforeClass
    static public void initializeArray()
    {
        falsehoods[0] = new Falsehood(BigInteger.valueOf(0),
                new MediaOutlet(0, (short) 1998, "Fox News"),
                Falsehood.SUBMITTED,
                (byte)1,
                null,
                Falsehood.LIE,
                new PublicFigure(),
                null,
                "Hannity",
                new Date(Calendar.getInstance().getTime().getTime()),
                "");
    }

    @Test
    public void emptyTest()
    {
        repo = new FalsehoodRepository();
        boolean exceptionCaught = false;
        try {
            Falsehood f = repo.save(null);
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
}
