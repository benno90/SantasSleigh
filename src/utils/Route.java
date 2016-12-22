/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.List;

/**
 * Wrapper class for one tour e.g. a list of Gifts.
 * @author benno
 */
public class Route {
    
    private List<Gift> tour;
    private double totalWeight;
    private double wrw;
    private final int ID;
    
    public Route(List<Gift> l, double tw, double wrw, int id) {
        tour = l;
        totalWeight = tw;
        this.wrw = wrw;
        this.ID = id;
    }
    
    public Route(List<Gift> l, double tw, int id) {
        tour = l;
        totalWeight = tw;
        wrw = utils.Utils.WRW(tour, totalWeight);
        this.ID = id;
    }
    
    public Route(List<Gift> l, int id) {
        tour = l;
        totalWeight = utils.Utils.totalWeight(l);
        wrw = utils.Utils.WRW(tour, totalWeight);
        this.ID = id;
    }
    
    public List<Gift> getTour() {
        return tour;
    }
    
    public double getWRW() {
        return wrw;
    }
    
    public int getID() {
        return ID;
    }
    
    
    
}