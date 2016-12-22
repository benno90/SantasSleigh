package utils;

/**
 *
 * @author benno
 */
public class GlobalConstants {

    public static final double EARTH_RADIUS     = 6371.0;    // average earth radius in [km]
    public static final int NUMBER_OF_GIFTS     = 100000;
    public static final double SLEIGH_WEIGHT    = 10.0;
    public static final double MAX_LOAD         = 1000.0;

    public static final Gift NORTH_POLE         = new Gift(0, 0.0, 90.0, 0.0);

    public static double TOTAL_WEIGHT           = 1409839.09802072; // in [kg] - from octave
    public static double MEAN_WEIGHT            = 14.0983909802072; // in [kg] - from octave
    public static double MEDIAN_WEIGHT          = 10.0130127907000; // in [kg] - from octave

}
