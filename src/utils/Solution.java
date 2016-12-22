/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Iterator;
import java.util.List;
import tuning.ParameterCoordinate;

/**
 * Wrapper class for a whole solution e.g. a list of Routes
 * @author benno
 */
public class Solution {
    
    private final List<Route> routes;
    private final double totalWRW;
    private ParameterCoordinate pc;
    
    public Solution(List<Route> sol) {
        this.routes = sol;
        this.totalWRW = evaluateTotalWRW();
        pc = null;
    }
    
    private double evaluateTotalWRW() {
        // accumulate WRW of individual routes
        Iterator<Route> it = routes.iterator();
        double x = 0;
        while(it.hasNext()) {
            x += it.next().getWRW();
        }
        return x;
    }
    
    public double getTotalWRW() {
        return totalWRW;
    }
    
    public List<Route> getRoutes() {
        return routes;
    }
    
    public void setParameterCoordinate(ParameterCoordinate pc) {
        this.pc = pc;
    }
    
    public ParameterCoordinate getParameterCoordinate() {
        return pc;
    }
    
    
}
