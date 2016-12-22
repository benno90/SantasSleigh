/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clustering;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/**
 *
 * @author benno
 */
public class HaversineDistance implements DistanceMeasure {

    @Override
    public double compute(double[] a, double[] b) throws DimensionMismatchException {
        return utils.Utils.HaversineDistance(a, b);
    }
    
}
