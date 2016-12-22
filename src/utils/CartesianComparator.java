/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Comparator;

/**
 *
 * @author benno
 */
public class CartesianComparator implements Comparator<Gift> {

    int coordinate; 
    // 0 -> x
    // 1 -> y
    // 2 -> z
    
    public CartesianComparator(int c) {
        this.coordinate = c;
    }
    
    @Override
    public int compare(Gift g1, Gift g2) {
        
        return Double.compare(g1.getPoint()[coordinate], g2.getPoint()[coordinate]);
    }
    
}
