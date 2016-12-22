/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Collections;
import java.util.List;
import net.sf.javaml.core.kdtree.KDTree;

/**
 *
 * @author benno
 */
public class KDTreeBuilder {
    
    private static int numberOfInsertedPoints;
    private static int maxLevel;

    private static final CartesianComparator comp[] = {
        new CartesianComparator(0),         // x
        new CartesianComparator(1),         // y
        new CartesianComparator(2),};       // z

    private static final int K = 3;         // 3D Tree

    public static KDTree buildTree(List<Gift> list) {
        numberOfInsertedPoints = 0;
        maxLevel = 0;
        int level = 0;
        KDTree tree = new KDTree(K);
        buildRecursive(tree, list, level);
        //System.out.println("Number Of Points inserted: " + numberOfInsertedPoints);
        //System.out.println("Maximal Depth of tree: " + maxLevel);
        return tree;

    }

    private static void buildRecursive(KDTree tree, List<Gift> list, int level) {
        int axis = level % K;
        Collections.sort(list, comp[axis]);
        
        if(level >= maxLevel) {
            maxLevel = level;
        }

        if (list.size() > 0) {
            int medianIndex = list.size() / 2;
            Gift g = list.get(medianIndex);
            tree.insert(g.getPoint(), g);
            numberOfInsertedPoints++;

            if ((medianIndex - 1) >= 0) {
                List<Gift> less = list.subList(0, medianIndex);
                if (less.size() > 0) {
                    buildRecursive(tree, less, level + 1);
                }
            }
            if ((medianIndex + 1) <= (list.size() - 1)) {
                List<Gift> more = list.subList(medianIndex + 1, list.size());
                if (more.size() > 0) {
                    buildRecursive(tree, more, level + 1);
                }
            }
        }

    }

}
