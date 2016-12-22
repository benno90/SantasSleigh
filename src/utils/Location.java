/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.apache.commons.math3.ml.clustering.Clusterable;


/**
 *
 * @author benno
 */
public class Location implements Clusterable{

    private final double longitudeInRads;
    private final double latitudeInRads;
    
    private final double[] R;
    // unit vector that is aligned with constant longitude
    private final double[] SOUTH;
    // unit vector that is aligned with contant latitude
    private final double[] EAST;

    //private final double X;  ->  R[0]
    //private final double Y;  ->  R[1];
    //private final double Z;  ->  R[2];

    public Location(double longitude, double latitude) {

        latitudeInRads = (Math.PI / 180.0) * latitude;
        longitudeInRads = (Math.PI / 180.0) * longitude;
        
        R = new double[3];
        SOUTH = new double[3];
        EAST = new double[3];

        // Cartesian coordinates, normalized for a sphere of diameter 1.0
        R[0] = 0.5 * Math.cos(latitudeInRads) * Math.sin(longitudeInRads);
        R[1] = 0.5 * Math.cos(latitudeInRads) * Math.cos(longitudeInRads);
        R[2] = 0.5 * Math.sin(latitudeInRads);
        
        // unit vector that points along constant latiude
        SOUTH[0] = Math.sin(latitudeInRads) * Math.sin(longitudeInRads);
        SOUTH[1] = Math.sin(latitudeInRads) * Math.cos(longitudeInRads);
        SOUTH[2] = -Math.cos(latitudeInRads);
        
        EAST[0] = Math.cos(longitudeInRads);
        EAST[1] = -Math.sin(longitudeInRads);
        EAST[2] = 0.0;
        
 
    }

    public double X() {
        return R[0];
    }

    public double Y() {
        return R[1];
    }

    public double Z() {
        return R[2];
    }

    public double getLongitude() {
        return longitudeInRads;
    }
    
    public double getLatitude() {
        return latitudeInRads;
    }
    
    public double getLongitudeInDeg() {
        return longitudeInRads*180.0/Math.PI;
    }

    public double getLatitudeInDeg() {
        return latitudeInRads*180.0/Math.PI;
    }

    @Override
    public double[] getPoint() {
        return R;
    }
    
    public double[] getSOUTH() {
        return SOUTH;
    }
    
    public double[] getEAST() {
        return EAST;
    }
}
