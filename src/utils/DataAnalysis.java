/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author benno
 */
public class DataAnalysis {

    public static void main(String[] args) throws IOException {

        String pathToInstance = "instance/central_asia.csv";
        Instance instance = Instance.load(Paths.get(pathToInstance));

        Gift[] gifts = instance.getGifts().toArray(new Gift[instance.getGifts().size()]);
        final ArrayList<Gift> points = new ArrayList<>(Arrays.asList(gifts));
        final int N = points.size();
        System.out.println("Number of Points loaded: " + N);

        Gift current;
        Gift next;
        double dist;
        double tempDist;
        double[] nearestNeighbourDist = new double[N];
        double totalDist = 0;
        for (int i = 0; i < N; i++) {
            current = points.get(i);
            dist = Double.MAX_VALUE;
            for (int j = 0; j < N; j++) {
                if (j != i) {
                    next = points.get(j);
                    tempDist = utils.Utils.HaversineDistance(current, next);
                    if (tempDist < dist) {
                        dist = tempDist;
                    }
                }
            }
            nearestNeighbourDist[i] = dist;
            totalDist += dist;
        }
        System.out.println("Average Nearest Neighbour Distance: " + totalDist / N);

    }

}
