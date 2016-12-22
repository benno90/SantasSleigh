/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 * Write submission files and .csv files for gnuplot
 * @author benno
 */
public class SolutionWriter {

    public static void writeSolution(List<Route> list, String name) {

        final String path = "gnuplot/solutions/" + name;

        final File f = new File(path);

        // delete and reopen plot folder
        try {
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

        System.out.println("WRITE SOLUTION");

        List<Gift> tour;
        Gift g;
        for (int i = 0; i < list.size(); i = i + 1) {
            tour = list.get(i).getTour();
            CSVwriter.openCSV(new File(path + "/" + i + ".csv"));
            CSVwriter.writeNumber(tour.size());
            for (int j = 0; j < tour.size(); j++) {
                g = tour.get(j);
                CSVwriter.writeGift(g.getID(), g.getLatitudeInDeg(), g.getLongitudeInDeg(), 0, g.getNeighbours());
            }
            CSVwriter.closeCSV();
        }

    }

    public static void writeSubmissionFile(List<Route> routes, String name) {
        final String path = "submissionFiles";
        final File f = new File(path);

        // delete and reopen plot folder
        try {
            if (f.exists()) {
                //FileUtils.cleanDirectory(f); //clean out directory (this is optional -- but good know)
                //FileUtils.forceDelete(f); //delete directory
                //FileUtils.forceMkdir(f); //create directory
            } else {
                FileUtils.forceMkdir(f); //create directory
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("WRITE SOLUTION");
        CSVwriter.openCSV(new File(path + "/" + name + ".csv"));
        CSVwriter.writeHeader("GiftId,TripId");

        List<Gift> tour;
        Gift g;
        int tripID;
        for (int i = 0; i < routes.size(); i++) {
            tour = routes.get(i).getTour();
            tripID = routes.get(i).getID();
            for (int j = 1; j < tour.size(); j++) {
                g = tour.get(j);
                CSVwriter.writeSolution(g.getID(), tripID);
            }
        }
        CSVwriter.closeCSV();
    }

}
