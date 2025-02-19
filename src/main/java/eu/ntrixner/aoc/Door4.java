package eu.ntrixner.aoc;

import eu.ntrixner.aoc.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Door4 {
    public static void main(String[] args) throws IOException {
        char[][] matrix = Utils.readCharsFromFile("src/main/resources/Input4.txt");
        int width = matrix[0].length;
        int height = matrix.length;

        //Indices of "a" in matrix, ignoring the edges
        List<Point> ai = new ArrayList<>();
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (matrix[y][x] == 'A')
                    ai.add(new Point(x, y));
            }
        }

        int count = 0;
        for (Point p : ai) {
            int x = p.x;
            int y = p.y;
            char lu = matrix[y - 1][x - 1];
            char ru = matrix[y - 1][x + 1];
            char lb = matrix[y + 1][x - 1];
            char rb = matrix[y + 1][x + 1];
            if (lu == 'M' && rb == 'S' || lu == 'S' && rb == 'M') {
                count += checkCounterDiag(ru, lb);
            }
        }
        System.out.println(count);
    }

    private static int checkCounterDiag(char ru, char lb) {
        if (ru == 'M' && lb == 'S' || ru == 'S' && lb == 'M') {
            return 1;
        }
        return 0;
    }

    public static void main1(String[] args) throws IOException {
        final String MATCH = "XMAS";
        char[][] matrix = Utils.readCharsFromFile("src/main/resources/Input4.txt");
        int width = matrix[0].length;
        int height = matrix.length;

        //Match Index
        int mi = 0;
        int num = 0;

        System.out.println("Left-Right");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }

            }
            System.out.print(" ");
            mi = 0;
        }

        System.out.println();
        System.out.println();
        System.out.println("Right-Left");
        for (int y = 0; y < height; y++) {
            for (int x = width - 1; x >= 0; x--) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }

        System.out.println();
        System.out.println();
        System.out.println("Up-Down");
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }

        System.out.println();
        System.out.println();
        System.out.println("Down-Up");
        for (int x = 0; x < width; x++) {
            for (int y = height - 1; y >= 0; y--) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }

        System.out.println();
        System.out.println();
        System.out.println("Diag \\ down");
        //First half, go from bottom to top
        for (int i = height - 1; i > 0; i--) {
            for (int x = 0, y = i; y < height && x < width; x++, y++) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }
        //Second half, go from left to right
        for (int i = 0; i < width; i++) {
            for (int x = i, y = 0; y < height && x < width; x++, y++) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }

        System.out.println();
        System.out.println();
        System.out.println("Diag \\ up");
        //First half, go left to right
        for (int i = 0; i < width - 1; i++) {
            for (int x = i, y = height - 1; y >= 0 && x >= 0; x--, y--) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }
        //Second half, go from bottom to up
        for (int i = height - 1; i >= 0; i--) {
            for (int x = width - 1, y = i; y >= 0 && x >= 0; x--, y--) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }

        System.out.println();
        System.out.println();
        System.out.println("Diag / down");
        //First half, go from left to right
        for (int i = 0; i < width - 1; i++) {
            for (int x = i, y = 0; y < height && x >= 0; x--, y++) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }
        //Second half, go from top to bottom
        for (int i = 0; i < height; i++) {
            for (int x = width - 1, y = i; y < height && x >= 0; x--, y++) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }


        System.out.println();
        System.out.println();
        System.out.println("Diag / up");
        //First half, top to bottom
        for (int i = 0; i < height - 1; i++) {
            for (int x = 0, y = i; y >= 0 && x < width; x++, y--) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }
        //Second half, go from left to right
        for (int i = 0; i < width; i++) {
            for (int x = i, y = height - 1; y >= 0 && x < width; x++, y--) {
                char c = matrix[y][x];
                System.out.print(c);
                if (c == MATCH.charAt(mi)) {
                    mi++;
                    if (mi == MATCH.length()) {
                        num++;
                        mi = 0;
                    }
                } else if (c == MATCH.charAt(0)) {
                    mi = 1;
                } else {
                    mi = 0;
                }
            }
            System.out.print(" ");
            mi = 0;
        }

        System.out.println();
        System.out.println();
        System.out.println("Number of matches = " + num);
    }


}
