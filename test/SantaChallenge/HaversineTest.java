/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SantaChallenge;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static utils.GlobalConstants.*;
import static utils.Utils.HaversineDistance;
import static utils.Utils.primitveHaversine;
import utils.Gift;
import utils.Instance;
import static utils.Utils.HaversineDistance;

/**
 *
 * @author benno
 */
public class HaversineTest {

    private static Gift A;
    private static Gift B;
    private static Gift C;
    private static Gift D;
    private static Gift E;

    private static double AB;
    private static double BC;
    private static double DE;

    private static Instance instance;

    public HaversineTest() {
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        // north pole
        A = new Gift(0, 30, 90, 0);
        // equator
        B = new Gift(0, 30, 0, 0);
        // equator2
        C = new Gift(0, 0, 0, 0);
        // random 
        D = new Gift(0, -132.456, 43.6, 0);
        // random2
        E = new Gift(0, 67.53, -22.556, 0);

        // distances
        AB = EARTH_RADIUS * 2.0 * Math.PI * 0.25;
        BC = EARTH_RADIUS * 2.0 * Math.PI / 12.0;
        double p1 = D.getLatitude();
        double p2 = E.getLatitude();
        double l1 = D.getLongitude();
        double l2 = E.getLongitude();
        DE = primitveHaversine(p1, p2, l1, l2);

        // instance
        String pathToInstance = "instance/gifts.csv";
        instance = Instance.load(Paths.get(pathToInstance));
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() {
        assertEquals(HaversineDistance(A, B), AB, 1e-8);
    }
    
        @Test
    public void test2() {
        assertEquals(HaversineDistance(B, C), BC, 1e-8);
    }
    
        @Test
    public void test3() {
        assertEquals(HaversineDistance(D, E), DE, 1e-8);
    }

}
