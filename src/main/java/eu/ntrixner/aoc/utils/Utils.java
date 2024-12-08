package eu.ntrixner.aoc.utils;

import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class Utils {
    private Utils() {
        //EMPTY
    }

    public static char[][] readCharsFromFile(String filePath) throws IOException {
        File f = new File(filePath);
        String[] fStrings = Files.readAllLines(f.toPath()).toArray(String[]::new);
        char[][] matrix = new char[fStrings.length][fStrings[0].length()];

        //Copy into matrix
        for (int y = 0; y < fStrings.length; y++) {
            matrix[y] = fStrings[y].toCharArray();
        }
        return matrix;
    }

    public static  char[][] deepCopy(char[][] matrix) {
        return java.util.Arrays.stream(matrix).map(char[]::clone).toArray($ -> matrix.clone());
    }

    public static Triple<Double, Double, Double> lineFormula(Point a, Point b) {
        double y1 = a.getY();
        double y2 = b.getY();
        double x1 = a.getX();
        double x2 = b.getX();
        double dA = y2 - y1;
        double dB = x1 - x2;
        double dC = y1 * (x2 - x1) - (y2 - y1) * x1;
        return Triple.of(dA, dB, dC);
    }
}
