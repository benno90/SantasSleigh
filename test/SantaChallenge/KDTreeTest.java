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
import net.sf.javaml.core.kdtree.KDTree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static utils.Utils.HaversineDistance;
import utils.Gift;
import static utils.GlobalConstants.NORTH_POLE;
import utils.Instance;
import static utils.Utils.HaversineDistance;

/**
 *
 * @author benno
 */
public class KDTreeTest {

    private static ArrayList<Gift> list;

    @BeforeClass
    public static void setUpClass() throws IOException {

        String pathToInstance = "instance/gifts.csv";
        Instance instance = Instance.load(Paths.get(pathToInstance));
        Gift[] gifts = instance.getGifts().toArray(new Gift[instance.getGifts().size()]);
        list = new ArrayList<>(Arrays.asList(gifts));
        System.out.println("Instance with " + list.size() + " gifts loaded.");

        KDTree tree = new KDTree(3);
        Gift g;

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
    public void Test1() {

        KDTree tree = new KDTree(3);
        Gift g;
        Gift nearest1 = null;
        double dist = Double.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            g = list.get(i);
            tree.insert(g.getPoint(), g);
            if (HaversineDistance(NORTH_POLE, g) < dist) {
                dist = HaversineDistance(NORTH_POLE, g);
                nearest1 = g;
            }
        }

        System.out.println("KD-Tree initialized.");
        System.out.println("Nearest point distance: " + dist);
        System.out.println("ID of nearest point: " + nearest1.getID());
        System.out.println("longitude nearest point: " + nearest1.getLongitudeInDeg());
        System.out.println("latitude nearest point:  " + nearest1.getLatitudeInDeg());

        Gift nearest2;
        nearest2 = (Gift) tree.nearest(NORTH_POLE.getPoint());

        System.out.println("FROM KD TREE: ");
        System.out.println("ID of nearest point: " + nearest2.getID());
        System.out.println("longitude nearest point: " + nearest2.getLongitudeInDeg());
        System.out.println("latitude nearest point:  " + nearest2.getLatitudeInDeg());

        assertEquals(nearest1.getID(), nearest2.getID());

    }
}
