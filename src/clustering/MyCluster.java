/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import java.util.List;

/**
 *
 * @author benno
 */
public class MyCluster<T> implements Comparable<MyCluster<T>>{
    
    private List<T> cluster;
    
    private int color = 0;
    
    public MyCluster(List<T> l) {
        cluster = l;
    }

    @Override
    public int compareTo(MyCluster<T> other) {
        return Integer.compare(this.cluster.size(), other.cluster.size());
    }
    
    public List<T> getCluster() {
        return cluster;
    }
    
    public int getSize() {
        return cluster.size();
    }
    
    public void setColor(int c) {
        color = c;
    }
    
    public int getColor() {
        return color;
    }
    
    
}
