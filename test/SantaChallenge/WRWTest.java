/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SantaChallenge;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.Gift;
import static utils.GlobalConstants.EARTH_RADIUS;
import static utils.GlobalConstants.NORTH_POLE;
import utils.Instance;
import static utils.Utils.HaversineDistance;
import static utils.Utils.WRW;
import static utils.Utils.primitveHaversine;
import static utils.Utils.HaversineDistance;
import static utils.Utils.WRW;

/**
 *
 * @author benno
 */
public class WRWTest {

    private static Gift A;
    private static Gift B;
    private static Gift C;
    private static Gift D;
    private static Gift E;

    private static double AB;
    private static double BC;
    private static double DE;

    private static double wrwAB;
    private static double wrwABC;
    private static double wrwDE;

    private static Instance instance;

    @BeforeClass
    public static void setUpClass() throws IOException {
        // north pole
        A = NORTH_POLE;
        // equator
        B = new Gift(0, 30, 0, 10);
        // equator2
        C = new Gift(0, 0, 0, 5);
        // random 
        D = new Gift(0, -132.456, 43.6, 7.7);
        // random2
        E = new Gift(0, 67.53, -22.556, 3.23);

        // distances
        AB = EARTH_RADIUS * 2.0 * Math.PI * 0.25;
        wrwAB = AB * 20 + AB * 10;
        BC = EARTH_RADIUS * 2.0 * Math.PI / 12.0;
        wrwABC = AB * 25 + BC * 15 + AB * 10;
        double p1 = D.getLatitude();
        double p2 = E.getLatitude();
        double l1 = D.getLongitude();
        double l2 = E.getLongitude();
        DE = primitveHaversine(p1, p2, l1, l2);
        double AD = primitveHaversine(90.0, p1, 0.0, l1);
        double EA = primitveHaversine(p2, 90.0, l2, 0.0);
        wrwDE = 20.93 * AD + 13.23 * DE + 10 * EA;
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
        LinkedList<Gift> ll = new LinkedList<>();
        ll.addFirst(B);
        ll.addFirst(A);
        assertEquals(WRW(ll), wrwAB, 1e-8);
        assertEquals(WRW(ll, 10), wrwAB, 1e-8);

        ll.clear();
        ll.addFirst(C);
        ll.addFirst(B);
        ll.addFirst(A);
        assertEquals(WRW(ll), wrwABC, 1e-8);
        assertEquals(WRW(ll, 15), wrwABC, 1e-8);

        ll.clear();
        ll.addFirst(E);
        ll.addFirst(D);
        ll.addFirst(A);
        assertEquals(WRW(ll), wrwDE, 1e-8);
        //assertEquals(WRW(ll, 10.93), wrwDE, 1e-8);
    }

    @Test
    public void test2() {

        Gift[] gifts = instance.getGifts().toArray(new Gift[instance.getGifts().size()]);
        ArrayList<Gift> list = new ArrayList<>(Arrays.asList(gifts));
        Collections.sort(list);
        LinkedList<Gift> ll = new LinkedList<>();
        Iterator<Gift> it = list.iterator();
        double totalWRW = 0;
        while(it.hasNext()) {
            ll.clear();
            ll.addFirst(it.next());
            ll.addFirst(NORTH_POLE);
            totalWRW += WRW(ll);
        }
        System.out.println("totalWRW: " + totalWRW);
    }
}
