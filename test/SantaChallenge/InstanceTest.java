package SantaChallenge;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utils.Gift;

import utils.Instance;
import static utils.GlobalConstants.*;

/**
 *
 * @author benno
 */
public class InstanceTest {

    private static Instance instance;

    /* executes only once "*/
    @BeforeClass
    public static void setUpClass() throws IOException {

        String pathToInstance = "instance/gifts.csv";
        instance = Instance.load(Paths.get(pathToInstance));
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /* executes before each test */
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void testLoad() {
        Gift[] gifts = instance.getGifts().toArray(new Gift[instance.getGifts().size()]);
        ArrayList<Gift> list = new ArrayList<>(Arrays.asList(gifts));
        System.out.println(list.size());
        assertEquals("size",list.size(), NUMBER_OF_GIFTS);
        
        double[] weights = new double[NUMBER_OF_GIFTS];
        double weight = 0;
        for(int i = 0; i < NUMBER_OF_GIFTS; i++) {
            weights[i] = list.get(i).getWeigth();
            weight += weights[i];
        }
        assertEquals(weight, TOTAL_WEIGHT, 1e-8);
        assertEquals(weight/NUMBER_OF_GIFTS, MEAN_WEIGHT, 1e-8);
        //System.out.println("total weigth: " + weight);
        //System.out.println("average weight: " + weight/NUMBER_OF_GIFTS);
        Arrays.sort(weights);
        System.out.println("median weight: " + weights[NUMBER_OF_GIFTS/2 - 1]);
        assertEquals(0.5*(weights[NUMBER_OF_GIFTS/2]+weights[NUMBER_OF_GIFTS/2-1]), MEDIAN_WEIGHT, 1e-8);
        
        
        
    }

}
