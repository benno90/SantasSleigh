/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuning;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import metaheuritics.NearestNeighbour;
import utils.Instance;
import utils.Route;
import utils.Solution;
import utils.SolutionWriter;

/**
 * Algorithmus der optimale Werte für die exponenten beta, gamma, delta sucht.
 * Alpha wird mit 1.0 festgelegt. Zu beginn werden Zufallswerte verwendet. Die
 * einzelnen Parameter-Vektoren werden dann gewichtet (je besser je "schwerer").
 * Dann wird der Schwerpunkt aller Vekoren berechnet und dann werden alle
 * Parameter-Vektoren in Richtung dieses Schwerpunktes verschoben. In der
 * Hoffnung es konvergiert nach einigen Iterationen.
 *
 * BUG IN ZEILE 90: ParameterCoordinate of best solution is null, why?
 * 
 * @author benno
 */
public class ParameterTuning {

    //------------ global paramters -------------------------
    private static final double SCALE_FACTOR = 0.1;         // vectors move SCALE_FACTOR times distance to centre of gravity.
    private static final double RELAXATION_FACTOR = 0.0;    // take RELAXATION_FACTOR of old position.
    private static final int NN = 50;                       // neighbourhood-size;
    private static final int N = 10000;                     // number of tours to find -> ~ >= 2000 to deliver all gifts
    private static final double alpha = 1.0;                // distance exponent

    private static final double SMALL = 0.001;
    private static final boolean verbose = true;

    private static final int numberOfInnerLoops = 5;        // how many inner iterations: calculate centre of gravity and move vectors
    private static final int numberOfOuterLoops = 3;        // how many times to repeat whole procedure

    /* ------------- multi threading -----------------*/
    private static final int numberOfThreads = 3;
    private static final int numberOfRandomSolutions = 9;

    private static void print(String s) {
        if (verbose) {
            System.out.println(s);
        }
    }

    // ------------------------------------------------------
    private static double getRandomPoint(Random rand, double[] range) {
        double r = range[1] - range[0];
        return (rand.nextDouble() * r) + range[0];
    }

    public static void main(String[] args) throws IOException {

        if (numberOfRandomSolutions % numberOfThreads != 0) {
            throw new Error("#threads % #random solutions == 0!");
        }

        /* ----------- data -------------------------- */
        String pathToInstance = "instance/gifts.csv";
        final Instance instance = Instance.load(Paths.get(pathToInstance));


        ArrayList<Solution> solns = new ArrayList<>(numberOfOuterLoops);
        for (int i = 0; i < numberOfOuterLoops; i++) {
            System.out.println("<============================================================== #" + i);
            solns.add(optimize(instance, i));
        }

        double shortest = Double.MAX_VALUE;
        Solution overalBest = null;
        for (int i = 0; i < numberOfOuterLoops; i++) {
            if (solns.get(i).getTotalWRW() < shortest) {
                shortest = solns.get(i).getTotalWRW();
                overalBest = solns.get(i);
            }
        }

        ParameterCoordinate bestCoordinate = overalBest.getParameterCoordinate();
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        System.out.println("      FINISHED");
        System.out.println("\noverall best solution: " + shortest);
        if (bestCoordinate != null) {
            System.out.println("beta:  " + bestCoordinate.getBeta());
            System.out.println("gamma: " + bestCoordinate.getGamma());
            System.out.println("delta: " + bestCoordinate.getDelta());
        }

        List<Route> solutionList = overalBest.getRoutes();
        System.out.println("Write solution file");
        SolutionWriter.writeSubmissionFile(solutionList, "bestSolution");
        SolutionWriter.writeSolution(solutionList, "bestSolution");

    }

    public static Solution optimize(Instance instance, int index) {

        /* -------------- agorithm ---------------------*/
        double beta;      // weight exponent
        double[] betaRange = {0.0, 0.5};
        double gamma;     // south exponent
        double[] southRange = {0.0, 0.25};
        double delta;     // east exponent
        double[] eastRange = {-0.25, 0.25};

        Random rand = new Random();

        /* ------------------------------------------------*/
        // generate random start coordinates
        List<ParameterCoordinate> coordinates = new ArrayList<>(numberOfRandomSolutions);
        for (int i = 0; i < numberOfRandomSolutions; i++) {
            coordinates.add(new ParameterCoordinate(getRandomPoint(rand, betaRange),
                    getRandomPoint(rand, southRange),
                    getRandomPoint(rand, eastRange), 1.0));
        }

        ParameterCoordinate centreOfGravity;
        ParameterCoordinate oldCOG = null;
        Solution bestSol = null;
        ParameterCoordinate bestCoordinate;
        Solution sol;

        for (int i = 0; i < numberOfInnerLoops; i++) {

            sol = TuningLoop(coordinates, numberOfThreads, instance, i);
            centreOfGravity = evaluateCentreOfGravity(coordinates);
            coordinates = moveCoordinates(coordinates, centreOfGravity);
            if (oldCOG != null) {
                double cogShift = length(deltavector(centreOfGravity.getCoordinateVector(), oldCOG.getCoordinateVector(), 1.0));
                print("centre of gravity shift: " + cogShift);
            }
            if (bestSol == null) {
                bestSol = sol;
            }
            if (bestSol.getTotalWRW() > sol.getTotalWRW()) {
                bestSol = sol;
            }
            oldCOG = centreOfGravity;
        }

        print("===============================================");
        print("===============================================");
        print("ParamTuning finished.");
        print("Overall best solution: " + bestSol.getTotalWRW());
        print("\nWrite solution.");

        List<Route> solutionList = bestSol.getRoutes();
        System.out.println("Write solution file");
        SolutionWriter.writeSubmissionFile(solutionList, "sol" + index);
        SolutionWriter.writeSolution(solutionList, "sol" + index);

        return bestSol;

    }

    private static Solution TuningLoop(List<ParameterCoordinate> coordinates, int numberOfThreads, Instance instance, int loopNr) {

        print("\n**********************************************");
        print("           main tuning loop #" + loopNr);
        print("************************************************");

        int numberOfSolutions = coordinates.size();

        NearestNeighbour[] nn = new NearestNeighbour[numberOfSolutions];
        Thread[] threads = new Thread[numberOfSolutions];

        for (int i = 0; i < numberOfSolutions; i = i + numberOfThreads) {
            for (int j = i; j < numberOfThreads + i; j++) {
                System.out.print("" + (j + 1) + " ");
                ParameterCoordinate c = coordinates.get(j);
                nn[j] = new NearestNeighbour(instance, NN, N, alpha, c.getBeta(), c.getGamma(), c.getDelta());
                threads[j] = new Thread(nn[j]);
                threads[j].start();
            }
            for (int j = i; j < numberOfThreads + i; j++) {
                try {
                    threads[j].join();
                } catch (InterruptedException ex) {
                    System.err.println("thread was interupted: ");
                }
            }
            print(" of " + coordinates.size() + " threads finished.");
        }

        double maxSolution = 0.0;
        double minSolution = Double.MAX_VALUE;
        double sol;
        int indexOfbestSolution = 0;
        for (int i = 0; i < coordinates.size(); i++) {
            sol = nn[i].getSolution().getTotalWRW();
            if (sol > maxSolution) {
                maxSolution = sol;
            }
            if (sol < minSolution) {
                minSolution = sol;
                indexOfbestSolution = i;
            }
        }

        print("best solution: " + minSolution);
        print("coordinates of best solution:");
        ParameterCoordinate best = coordinates.get(indexOfbestSolution);
        print("beta  = " + best.getBeta());
        print("gamma = " + best.getGamma());
        print("delta = " + best.getDelta());

        double range = maxSolution - minSolution;

        for (int i = 0; i < coordinates.size(); i++) {
            sol = nn[i].getSolution().getTotalWRW();
            sol = ((sol - minSolution + SMALL) / range);      // sol normalized to range [SMALL ... 1];
            sol = (1.0 / sol);                                // invert: short solustions are better -> range [1 .... SMALL^-1]
            coordinates.get(i).setWeight(sol);
        }

        nn[indexOfbestSolution].getSolution().setParameterCoordinate(best);
        return nn[indexOfbestSolution].getSolution();
    }

    private static ParameterCoordinate evaluateCentreOfGravity(List<ParameterCoordinate> coordinates) {
        int numberOfCoordinates = coordinates.size();
        double betaCentre = 0;
        double gammaCentre = 0;
        double deltaCentre = 0;
        ParameterCoordinate c;
        double weight;
        double totalWeight = 0;
        for (int i = 0; i < numberOfCoordinates; i++) {
            c = coordinates.get(i);
            weight = c.getWeight();
            totalWeight += weight;
            betaCentre += c.getBeta() * weight;
            gammaCentre += c.getGamma() * weight;
            deltaCentre += c.getDelta() * weight;
        }
        betaCentre = betaCentre / totalWeight;
        gammaCentre = gammaCentre / totalWeight;
        deltaCentre = deltaCentre / totalWeight;
        return new ParameterCoordinate(betaCentre, gammaCentre, deltaCentre, 0);
    }

    private static List<ParameterCoordinate> moveCoordinates(List<ParameterCoordinate> coordinates, ParameterCoordinate cog) {
        int numberOfCoordinates = coordinates.size();
        final double[] centreOfGravity = {cog.getBeta(), cog.getDelta(), cog.getGamma()};
        double[] coord = new double[3];
        double[] delta = new double[3];
        double[] newCoord = new double[3];
        ParameterCoordinate c;
        List<ParameterCoordinate> newCoordinates = new ArrayList<>(numberOfCoordinates);
        for (int i = 0; i < numberOfCoordinates; i++) {
            c = coordinates.get(i);
            coord[0] = c.getBeta();
            coord[1] = c.getGamma();
            coord[2] = c.getDelta();
            delta = deltavector(centreOfGravity, coord, SCALE_FACTOR);

            newCoord[0] = (coord[0] + delta[0]) * (1 - RELAXATION_FACTOR) + RELAXATION_FACTOR * coord[0];
            newCoord[1] = (coord[1] + delta[1]) * (1 - RELAXATION_FACTOR) + RELAXATION_FACTOR * coord[1];
            newCoord[2] = (coord[2] + delta[2]) * (1 - RELAXATION_FACTOR) + RELAXATION_FACTOR * coord[2];
            newCoordinates.add(new ParameterCoordinate(newCoord[0], newCoord[1], newCoord[2], 0.0));
        }

        return newCoordinates;
    }

    // deltavector points towards "next"
    private static double[] deltavector(double[] next, double[] current, double scaleFactor) {
        double[] d = new double[3];
        d[0] = (next[0] - current[0]) * scaleFactor;
        d[1] = (next[1] - current[1]) * scaleFactor;
        d[2] = (next[2] - current[2]) * scaleFactor;
        return d;
    }

    private static double length(double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

}
