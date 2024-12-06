package eu.ntrixner.aoc;

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
}
