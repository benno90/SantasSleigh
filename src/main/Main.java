/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import metaheuritics.NearestNeighbour;
import utils.Instance;
import utils.Route;
import utils.Solution;
import utils.SolutionWriter;

/**
 * Generate a Solution for Santas Stolen Sleigh Problem.
 * 
 *
 * Submissionfiles are written to SantaChallenge/submissionFiles/<solutionName>.csv
 * 
 * The result can be plotted with the gnuplot file SantaChallenge/plot.gp
 * (Files in SantaChallenge/gnuplot/solutions/<solutionName>/ -> one .csv per tour)
 * 
 * @author benno
 */
public class Main {

    public static void main(String[] args) throws IOException {

        /* ---------- output -------------------------*/
        boolean writeSolution = true;
        String solutionName = "test";

        /* ----------- data -------------------------- */
        String pathToInstance = "instance/gifts.csv";
        Instance instance = Instance.load(Paths.get(pathToInstance));

        /* -------------- agorithm ---------------------*/
        int NN = 50;            // neighbourhood-size;
        int N = 10000;         // number of tours to find

        double alpha = 1.0;     // distance exponent
        double beta = 0.3;      // weight exponent
        double gamma = 0.1;    // south exponent
        double delta = -0.02;     // east exponent

        /* ----------------------------------------------*/
        System.out.println("thread started.");
        NearestNeighbour nn = new NearestNeighbour(instance, NN, N, alpha, beta, gamma, delta);
        Thread t = new Thread(nn);
        try {
            t.start();
            t.join();
        } catch (InterruptedException ex) {
            System.err.println("thread was interupted: ");
        }
        System.out.println("thread finished.");
        Solution sol = nn.getSolution();

        System.out.println("\n--------- validity test ------------\n");
        utils.Utils.checkValidity(sol.getRoutes());
        System.out.println("\n------------------------------------\n");

        System.out.println("total WRW: " + sol.getTotalWRW());

        if (writeSolution) {

            List<Route> solutionList = sol.getRoutes();
            System.out.println("Write solution file");
            SolutionWriter.writeSubmissionFile(solutionList, solutionName);
            SolutionWriter.writeSolution(solutionList, solutionName);

        }
    }

}



// old stuff

/*

        NearestNeighbour[] nn = new NearestNeighbour[numberOfThreads];
        Thread[] threads = new Thread[numberOfThreads];
        // create ants and start threads
        System.out.println("Waiting for threads to finish...");
        for (int i = 0; i < numberOfThreads; i++) {
            //beta = -3.0 + 6.0 / 100.00 * ((double) (i + 1));
            nn[i] = new NearestNeighbour(tree, NN, N, alpha, beta, gamma);
            threads[i] = new Thread(nn[i]);
            threads[i].start();
        }
        // wait for all threads to finish
        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                System.err.println("thread was interupted: ");
            }
        }

        double[] solns = new double[numberOfThreads];

        System.out.println("Evaluate solutions...");

        for (int i = 0; i < numberOfThreads; i++) {
            double WRW = 0;
            List<Route> route = nn[i].getSolution();
            for (int j = 0; j < route.size(); j++) {
                WRW += route.get(j).getWRW();
            }
            solns[i] = WRW;
        }

        final String path = "gnuplot/paramTest.csv";
        final File f = new File(path);
        CSVwriter.openCSV(f);
        for (int i = 0; i < numberOfThreads; i++) {
            CSVwriter.writeXY(-3.0 + 6.0 / 100.00 * ((double) (i + 1)), solns[i]);
        }

        System.out.println("Write solution file");
        SolutionWriter.writeSubmissionFile(testSol, "first");

        double WRW = 0;
        for (int i = 0; i < testSol.size(); i++) {
            WRW += testSol.get(i).getWRW();
        }
        System.out.println("TOTAL WRW: " + WRW);

        SolutionWriter.writeSolution(testSol, "test");
        /* ---------------------------------------------*/
 /*
        double WRW = 0;
        for(int i = 0; i < solution.size(); i++) {
            WRW += solution.get(i).getWRW();
        }
        
        System.out.println("Number of routes generated: " + solution.size());
        System.out.println("Total WRW: " + WRW);
        
        if(writeSolution) {
            SolutionWriter.writeSolution(solution, solutionName);
        }*/