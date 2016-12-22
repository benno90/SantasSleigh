/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tuning;

/**
 *
 * @author benno
 */
public class ParameterCoordinate {
    
    private double beta;
    private double gamma;
    private double delta;
    
    private double[] vector;
    
    private double weight;

    public ParameterCoordinate(double beta, double gamma, double delta, double weight) {
        this.beta = beta;
        this.gamma = gamma;
        this.delta = delta;
        this.weight = weight;
        vector = new double[3];
        vector[0] = beta;
        vector[1] = gamma;
        vector[2] = delta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBeta() {
        return beta;
    }

    public double getGamma() {
        return gamma;
    }

    public double getDelta() {
        return delta;
    }

    public double getWeight() {
        return weight;
    }
    
    public double[] getCoordinateVector() {
        return vector;
    }
    
    
}
