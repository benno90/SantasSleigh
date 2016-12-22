/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.PrintWriter;

/**
 * @author ashraf
 *
 */
public class CSVwriter {

    //Delimiter used in CSV file
    private static final String DELIMITER = ",";
    private static final String NEW_LINE = "\n";

    //CSV file header
    private static final String FILE_HEADER = "#iteration, temperature, tour_length";

    private static PrintWriter writer;

    public static void openCSV(File file) {

        try {
            writer = new PrintWriter(file);
            //Write the CSV file header
            //writer.append(FILE_HEADER);

            //Add a new line separator after the header
            //writer.append(NEW_LINE);
            //writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in openCSV --------------");
            e.printStackTrace();
        }
    }

    public static void closeCSV() {
        try {
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("------------- error in closeCSV -----------------");
            e.printStackTrace();
        }
    }

    public static void writeGift(int id, double latitude, double longitude, int color, Object[] nn) {
        if (nn == null) {
            writeLocation(id, latitude, longitude, color);
        } else {
            writeLocationWithNeighbours(id, latitude, longitude, color, nn);
        }
    }

    public static void writeLocation(int id, double latitude, double longitude, int color) {
        try {
            writer.printf("%d" + DELIMITER + "%.2f" + DELIMITER + "%.2f" + DELIMITER + "%d%n", id, longitude, latitude, color);
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }

    public static void writeLocationWithNeighbours(int id, double latitude, double longitude, int color, Object[] nn) {
        try {
            writer.printf("%d" + DELIMITER + "%.2f" + DELIMITER + "%.2f" + DELIMITER + "%d" + DELIMITER, id, longitude, latitude, color);
            for (int i = 0; i < nn.length; i++) {
                Gift g = (Gift) nn[i];
                writer.printf("%.2f" + DELIMITER + "%.2f" + DELIMITER, g.getLongitudeInDeg(), g.getLatitudeInDeg());
            }
            writer.printf("%n");
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }

    public static void writePoint(int id, double x, double y, double z) {
        try {
            writer.printf("%d" + DELIMITER + "%.4f" + DELIMITER + "%.4f" + DELIMITER + "%.4f%n", id, x, y, z);
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }

    public static void writeNumber(int number) {
        try {
            writer.printf("%d%n", number);
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }

    public static void writeXY(double x, double y) {
        try {
            writer.printf("%.2f" + DELIMITER + "%.2f%n", x, y);
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }

    public static void writeSolution(int giftID, int tripID) {
        try {
            writer.printf("%d" + DELIMITER + "%d%n", giftID, tripID);
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }
    
    public static void writeHeader(String s) {
        try {
            writer.printf("%s%n",s);
            writer.flush();
        } catch (Exception e) {
            System.out.println("------------- error in writeCSV -----------------");
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {
        /*File file = new File("tsp/TSP_csv/test.csv");
        CSVwriter.openCSV(file);
        CSVwriter.write(3, 10, 10);
        CSVwriter.write(5, 13, 20);
        CSVwriter.write(7, 23, 30);
        CSVwriter.write(9, 44, 40);
        CSVwriter.write(11, 55, 50);
        CSVwriter.closeCSV();*/
    }
}
