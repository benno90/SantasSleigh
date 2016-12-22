/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import net.sf.javaml.core.kdtree.KDTree;

/**
 *
 * @author benno
 */
public class KdTreeTest {

    public static void main(String[] args) throws IOException {
        //KDTree tree = new KDTree(3);
        //Gift g = new Gift(0, 1, 2, 1);
        //tree.insert(g.getPoint(), g);

        String pathToInstance = "instance/gifts.csv";
        Instance instance = Instance.load(Paths.get(pathToInstance));
        Gift[] gifts = instance.getGifts().toArray(new Gift[instance.getGifts().size()]);
        ArrayList<Gift> list = new ArrayList<>(Arrays.asList(gifts));
        
        KDTree tree = KDTreeBuilder.buildTree(list);
    }

}
