/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author benno
 */
public class Test {
    
    public static void main(String[] args) throws IOException {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        
        String pathToInstance = "instance/gifts.csv";
        Instance instance = Instance.load(Paths.get(pathToInstance));
    }
    
}
