package utils;

import static utils.GlobalConstants.*;
import static java.lang.Math.*;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author benno
 */
public class Utils {

    public static double HaversineDistance(Gift g1, Gift g2) {
        double dX = g1.X() - g2.X();
        double dY = g1.Y() - g2.Y();
        double dZ = g1.Z() - g2.Z();

        double r = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

        return 2 * EARTH_RADIUS * Math.asin(r);
    }

    public static double HaversineDistance(double[] p1, double[] p2) {
        double dX = p1[0] - p2[0];
        double dY = p1[1] - p2[1];
        double dZ = p1[2] - p2[2];

        double r = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

        return 2 * EARTH_RADIUS * Math.asin(r);
    }

    /**
     *
     * @param p1 latitude 1
     * @param p2 latitude 2
     * @param l1 longitude 1
     * @param l2 longitude 2
     * @return Haversine Distance
     */
    public static double primitveHaversine(double p1, double p2, double l1, double l2) {
        return 2.0 * EARTH_RADIUS * asin(sqrt(pow(sin((p2 - p1) / 2.0), 2) + cos(p1) * cos(p2) * pow(sin((l2 - l1) / 2.0), 2)));
    }

    /**
     * Calculates the weighted reindeer weariness for a tour.
     *
     * @param list Ordered list of the visited gifts. The list must contatain
     * all visited gifts in order. The first point must be the north pole with
     * zero weight.
     * @return
     */
    public static double WRW(List<Gift> list) {
        return WRW(list, totalWeight(list));
    }

    public static double totalWeight(List<Gift> list) {
        double weight = 0;
        for (int i = 0; i < list.size(); i++) {
            weight += list.get(i).getWeigth();
        }
        return weight;
    }

    public static double WRW(List<Gift> list, double totalWeight) {
        Iterator<Gift> it = list.iterator();
        double weight = totalWeight + SLEIGH_WEIGHT;
        double wrw = 0;
        Gift current = it.next();
        Gift next = null;
        // the fisrt location must be the northpole
        assert current == NORTH_POLE;
        while (it.hasNext()) {
            next = it.next();
            weight -= current.getWeigth();
            //wrw += HaversineDistance(list.get(i), list.get(i + 1))*weight;
            wrw += HaversineDistance(current, next) * (weight);
            current = next;
        }
        weight -= current.getWeigth();
        wrw += HaversineDistance(next, NORTH_POLE) * weight;
        return wrw;
    }

    public static void checkValidity(List<Route> testSol) {
        System.out.println("testing solution validity: ");
        long indexSum = 0;
        int numberOfGifts = 0;
        double totalWeight = 0.0;
        List<Gift> r;
        for (int i = 0; i < testSol.size(); i++) {
            r = testSol.get(i).getTour();
            for (int j = 0; j < r.size(); j++) {
                indexSum += r.get(j).getID();
                totalWeight += r.get(j).getWeigth();
                numberOfGifts++;
            }
            numberOfGifts--; // subtract northPole
        }

        long v = (long) NUMBER_OF_GIFTS;
        System.out.println("#Tours: " + testSol.size());
        System.out.println("expected indexSum: " + (v * v + v) / 2L);
        System.out.println("actual indexSum: " + indexSum);
        System.out.println("expected totalWeigth: " + TOTAL_WEIGHT);
        System.out.println("actual totalWeight: " + totalWeight);
        System.out.println("expected number of Gifts: " + NUMBER_OF_GIFTS);
        System.out.println("actual number of Gifts: " + numberOfGifts);
    }
}
