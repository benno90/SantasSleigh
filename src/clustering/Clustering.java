/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.io.FileUtils;
import utils.CSVwriter;
import utils.Gift;
import utils.Instance;

/**
 * Clustering with DBSCAN Algorithm from apache commons machine learning library
 * Writes clusters to SantaChallenge/gnuplot/plot
 * Can be plotted with SantaChallenge/gnuplot/plot.gp
 * @author benno
 */
public class Clustering {

    public static void main(String[] args) throws IOException {

        // first argument: epsilon
        // second argument: min number of points for cluster
        DBSCANClusterer<Gift> clusterer = new DBSCANClusterer<>(250, 8, new HaversineDistance());

        //String pathToInstance = "instance/south_asia.csv";
        //String pathToInstance = "instance/gifts.csv";
        String pathToInstance = "instance/europe.csv";
        
        Instance instance = Instance.load(Paths.get(pathToInstance));

        Gift[] gifts = instance.getGifts().toArray(new Gift[instance.getGifts().size()]);
        final ArrayList<Gift> originalPoints = new ArrayList<>(Arrays.asList(gifts));
        System.out.println("overall number of locations = " + originalPoints.size());
        final List<Cluster<Gift>> clusters = clusterer.cluster(originalPoints);
        System.out.println("number of clusters = " + clusters.size());

        // delete and reopen plot folder
        try {
            File f = new File("gnuplot/plot");
            if (f.exists()) {
                FileUtils.cleanDirectory(f); //clean out directory (this is optional -- but good know)
                FileUtils.forceDelete(f); //delete directory
                FileUtils.forceMkdir(f); //create directory
            } else {
                FileUtils.forceMkdir(f); //create directory
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fill list of MyCluster<Gift>
        final List<MyCluster<Gift>> myClusters = new ArrayList<>();
        int maxSize = 0;
        int minSize = Integer.MAX_VALUE;
        int size;
        for (int i = 0; i < clusters.size(); i++) {
            myClusters.add(new MyCluster<Gift>(clusters.get(i).getPoints()));
            size = myClusters.get(i).getSize();
            if (size > maxSize) {
                maxSize = size;
            } else if (size < minSize) {
                minSize = size;
            }
        }
        
        // ------------------- coloring -------------------
        Collections.sort(myClusters);

        int range = maxSize - minSize;
        int r;
        int b;
        int gr;
        MyCluster<Gift> mc;

        // colors depending on cluster size
        /*for(int i = 0; i < myClusters.size(); i++) {

            mc = myClusters.get(i);                
            double d1 = (double) mc.getSize() - minSize;
            double linearFactor = d1/range;
            r = (int) (linearFactor * 255);
            b = (int) (-(linearFactor * 255) + 255);
            // 24bit color
            mc.setColor(256*256*r + b);
        }*/
        
        // random colors
        Random rand = new Random();
        for(int i = 0; i < myClusters.size(); i++) {
            r = (int) (rand.nextDouble()*255);
            gr = (int) (rand.nextDouble()*255);
            b = (int) (rand.nextDouble()*255);
            mc = myClusters.get(i);                
            mc.setColor(256*256*r + 256*gr + b);
        }
        
        // -------------------------------------------------
       
        
        // write all clusters & remove noise
        List<Gift> noise = new ArrayList<>(originalPoints);
        Gift g;
        List<Gift> cluster;
        MyCluster<Gift> myc;
        for (int i = 0; i < clusters.size(); i++) {
            myc = myClusters.get(i);
            CSVwriter.openCSV(new File("gnuplot/plot/" + i + ".csv"));
            cluster = myc.getCluster();
            CSVwriter.writeNumber(myc.getSize());
            for (int j = 0; j < cluster.size(); j++) {
                g = cluster.get(j);
                CSVwriter.writeLocation(g.getID(), g.getLatitudeInDeg(), g.getLongitudeInDeg(), myc.getColor());
                noise.remove(g);
            }
            CSVwriter.closeCSV();
        }

        System.out.println("Noise = " + noise.size());

        // write noise
        CSVwriter.openCSV(new File("gnuplot/plot/noise"));
        CSVwriter.writeNumber(noise.size());
        for (int i = 0; i < noise.size(); i++) {
            g = noise.get(i);
            CSVwriter.writeLocation(g.getID(), g.getLatitudeInDeg(), g.getLongitudeInDeg(), 0);
        }
        CSVwriter.closeCSV();

    }
}
