package metaheuritics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.sf.javaml.core.kdtree.KDTree;
import utils.Gift;
import utils.Route;
import static utils.GlobalConstants.*;
import utils.Instance;
import utils.KDTreeBuilder;
import utils.Solution;
import static utils.Utils.HaversineDistance;

/**
 *
 * @author benno
 */
public class NearestNeighbour implements Runnable {

    // ============== P A R A M E T E R S =============
    private final int NN;                          // neighbourhood-size;
    private final boolean usePseudoDistane;       // weighted mix of distance, direction and weigt
    private final int N;                           // number of tours to find

    // -------------- distance parameters --------------
    private final double alpha;   // distance exponent
    private final double beta;    // weight exponent
    private final double gamma;   // south exponent
    private final double delta;   // east exponent
    private final double SMALL;

    // -------------- output ---------------------------
    private final boolean writeNeighbours;

    // -------------------------------------------------
    //private final Instance instance;
    private final KDTree tree;
    private List<Route> solution;

    // -------------------------------------------------
    /**
     * Produces a solution for the kaggle challenge "Santas stolen sleight" 
     * The algorithm uses a weighted mix of distance, weight and direction to generate
     * a tour. This is repeated until there are no gits left.
     * see https://www.kaggle.com/c/santas-stolen-sleigh
     * 
     * @param instance 
     * @param NN The number of neighbours that are considered when evaluating the next gift.
     * @param N Number of tours. To generate a complete solution - set this value suffciently high.
     * @param alpha Distance exponent.
     * @param beta Heavy gifts first exponent.
     * @param gamma Go south exponent.
     * @param delta Go east exponent.
     */
    public NearestNeighbour(final Instance instance, int NN, int N, double alpha, double beta, double gamma, double delta) {
        this.NN = NN;
        this.N = N;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.delta = delta;

        usePseudoDistane = true;
        SMALL = 1e-5;
        writeNeighbours = true;
        // -> post processing: write all NN neighbours in csv file

        Gift[] gifts = instance.getGifts().toArray(new Gift[instance.getGifts().size()]);
        ArrayList<Gift> Instancelist = new ArrayList<>(Arrays.asList(gifts));
        this.tree = KDTreeBuilder.buildTree(Instancelist);
        solution = new LinkedList<>();
    }

    @Override
    public void run() {
        solve(tree, solution);
    }

    public Solution getSolution() {
        return new Solution(solution);
    }

    public List<Route> solve(KDTree tree, List<Route> solution) {
        
        LinkedList<Gift> tour;
        double weight;              // weight of all items
        Object[] nn;                // potential next gift
        Gift current;               // current gift
        Gift next;                  // next gift
        boolean full;
        int numberOfNeighbours = NN;
        int tourIDcount = 1;
        // -------------------------------

        int i = 0;
        int numberOfPoints = 0;
        while ((i < N) && (numberOfPoints < NUMBER_OF_GIFTS)) {
            i++;
            //System.out.println("main loop #" + i);
            tour = new LinkedList<>();
            current = NORTH_POLE;
            tour.add(NORTH_POLE);
            weight = 0;
            full = false;

            if (numberOfPoints == NUMBER_OF_GIFTS - 1) {
                // there is exactly one point left.
                //System.out.println("TERMINATING LOOP");
                tour.addLast((Gift) tree.nearest(NORTH_POLE.getPoint()));
                numberOfPoints++;
            } else {

                while (!full && (numberOfNeighbours != 0)) {

                    //System.out.println("numberOfNeighbours: " + numberOfNeighbours);
                    if (usePseudoDistane) {
                        nn = tree.nearest(current.getPoint(), numberOfNeighbours);
                        next = evaluateNearest(nn, current, weight);
                        if (writeNeighbours) {
                            current.setNeighbours(nn);
                        }
                    } else {
                        next = (Gift) tree.nearest(current.getPoint());
                        if (writeNeighbours) {
                            Object[] ob = {next};
                            current.setNeighbours(ob);
                        }
                    }
                    if (weight + next.getWeigth() <= MAX_LOAD) {
                        weight += next.getWeigth();
                        tree.delete(next.getPoint());
                        tour.addLast(next);
                        current = next;
                        numberOfPoints++;
                        numberOfNeighbours = Math.min(NN, NUMBER_OF_GIFTS - numberOfPoints);
                    } else {
                        full = true;
                    }
                }
            }
            //numberOfPoints += tour.size() - 1; // because northpole is always first point
            //System.out.println("tour completed. weight = " + weight);
            solution.add(new Route(tour, weight, tourIDcount));
            tourIDcount++;
            //System.out.println("numberOfPoints: " + numberOfPoints);
        }

        return solution;
    }

    private Gift evaluateNearest(Object[] nn, Gift current, double currentWeight) {
        double shortestDistance = Double.MAX_VALUE;
        Gift nearest = null;
        double dist;
        for (int i = 0; i < nn.length; i++) {
            dist = pseudoDistance(currentWeight, current, (Gift) nn[i]);
            if (dist < shortestDistance) {
                shortestDistance = dist;
                nearest = (Gift) nn[i];
            }
        }
        return nearest;
    }

    private double pseudoDistance(double currentWeight, Gift current, Gift next) {
        double A = A(current, next);
        double B = B(currentWeight, next.getWeigth());
        double C = C(current, next, currentWeight);
        double D = D(current, next, currentWeight);
        return Math.pow(A, alpha) * Math.pow(B, beta) * Math.pow(C, gamma) * Math.pow(D,delta);
    }

    /**
     * Distance Factor A.
     *
     * @param g1
     * @param g2
     * @return HaverSineDistance
     */
    private double A(Gift g1, Gift g2) {
        return HaversineDistance(g1, g2);
    }

    /**
     * Weight factor B. Pick heavy weights at the beginning.
     *
     * @param currentWeight Current total weight of the tour
     * @param giftWeight weight of next gift considered
     * @return 2.0 for empty sleigh, 1.0 for full sleigh
     */
    private double B(double currentWeight, double giftWeight) {
        double progress = (currentWeight / MAX_LOAD) * 2 - 1;
        // progress goes from -1 to 1
        return Math.pow(giftWeight, progress);
        // at beginning: giftweight^-1 -> heavy gifts are "closer"
        // at end: giftweight^1 -> light gifts are "closer"
    }

    /**
     * Direction factor C. Go south at the beginning.
     *
     * @param g1
     * @param g2
     * @return 
     */
    private double C(Gift current, Gift next, double currentWeight) {
        double progress = currentWeight / MAX_LOAD;

        double[] SOUTH = current.getSOUTH();
        //double[] EAST = current.getEAST();
        //SOUTH = EAST; // gou east first
        double[] direction = deltavector(next.getPoint(), current.getPoint());
        double dot = dotproduct(SOUTH, direction);
        if (progress <= 0.5) {    // go south only -> pick a number between 0..1 to change direction during tour
            // favour south
            if (dot <= SMALL) {
                dot = SMALL;
            }
            return 1.0 / dot;
        } else { // favour north
            if (dot >= -SMALL) {
                dot = -SMALL;
            }
            return -1.0 / dot;
        }
    }
    
    /**
     * Direction factor C. Go east at the beginning.
     *
     * @param g1
     * @param g2
     * @return 
     */
    private double D(Gift current, Gift next, double currentWeight) {
        double progress = currentWeight / MAX_LOAD;
        double[] EAST = current.getEAST();
        double[] direction = deltavector(next.getPoint(), current.getPoint());
        double dot = dotproduct(EAST, direction);
        if (progress <= 2.0) {    // go east only
            // favour east
            if (dot <= SMALL) {
                dot = SMALL;
            }
            return 1.0 / dot;
        } else { // favour north
            if (dot >= -SMALL) {
                dot = -SMALL;
            }
            return -1.0 / dot;
        }
    }

    private double length(double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    private double dotproduct(double[] v1, double[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
    }

    private double[] deltavector(double[] next, double[] current) {
        double[] d = new double[3];
        d[0] = next[0] - current[0];
        d[1] = next[1] - current[1];
        d[2] = next[2] - current[2];
        double l = length(d);
        d[0] = d[0] / l;
        d[1] = d[1] / l;
        d[2] = d[2] / l;
        return d;
    }

}
