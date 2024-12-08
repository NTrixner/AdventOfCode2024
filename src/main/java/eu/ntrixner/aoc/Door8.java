package eu.ntrixner.aoc;

import eu.ntrixner.aoc.utils.Bounds2DInt;
import eu.ntrixner.aoc.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Door8 {

    public static void main(String[] args) throws IOException {
        char[][] matrix = Utils.readCharsFromFile("src/main/resources/Input8.txt");
        Map<Character, List<Point>> frequencies = new HashMap<>();
        Bounds2DInt bounds = new Bounds2DInt(0, matrix[0].length, 0, matrix.length);
        //Find all unique frequencies
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (matrix[y][x] != '.') {
                    if (!frequencies.containsKey(matrix[y][x])) {
                        frequencies.put(matrix[y][x], new ArrayList<>());
                    }
                    frequencies.get(matrix[y][x]).add(new Point(x, y));
                }
            }
        }
        Map<Character, Set<Pair<Point, Point>>> frequencyAntennaPairs = new HashMap<>();

        //Find all pairs of antennas for frequencies
        for (Map.Entry<Character, List<Point>> frequency : frequencies.entrySet()) {
            char f = frequency.getKey();
            List<Point> antennas = frequency.getValue();
            Set<Pair<Point, Point>> antennaPairs = new HashSet<>();
            for (int i = 0; i < antennas.size(); i++) {
                for (int j = i; j < antennas.size(); j++) {
                    if (i != j) {
                        antennaPairs.add(Pair.of(antennas.get(i), antennas.get(j)));
                    }
                }
            }
            frequencyAntennaPairs.put(f, antennaPairs);
        }

        List<Triple<Double, Double, Double>> lineFormulas = new ArrayList<>();

        for (Map.Entry<Character, Set<Pair<Point, Point>>> fap : frequencyAntennaPairs.entrySet()) {
            for (Pair<Point, Point> antennaPair : fap.getValue()) {
                lineFormulas.add(Utils.lineFormula(antennaPair.getLeft(), antennaPair.getRight()));
            }
        }

        Set<Point> antinodes = new HashSet<>();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                for (Triple<Double, Double, Double> line : lineFormulas) {
                    if (line.getLeft() * x + line.getMiddle() * y + line.getRight() == 0) {
                        antinodes.add(new Point(x, y));
                        break;
                    }
                }
            }
        }
        System.out.println(antinodes.size());
    }

    public static void main1(String[] args) throws IOException {
        char[][] matrix = Utils.readCharsFromFile("src/main/resources/Input8.txt");
        Map<Character, List<Point>> frequencies = new HashMap<>();
        Bounds2DInt bounds = new Bounds2DInt(0, matrix[0].length, 0, matrix.length);
        //Find all unique frequencies
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (matrix[y][x] != '.') {
                    if (!frequencies.containsKey(matrix[y][x])) {
                        frequencies.put(matrix[y][x], new ArrayList<>());
                    }
                    frequencies.get(matrix[y][x]).add(new Point(x, y));
                }
            }
        }
        Map<Character, Set<Pair<Point, Point>>> frequencyAntennaPairs = new HashMap<>();

        //Find all pairs of antennas for frequencies
        for (Map.Entry<Character, List<Point>> frequency : frequencies.entrySet()) {
            char f = frequency.getKey();
            List<Point> antennas = frequency.getValue();
            Set<Pair<Point, Point>> antennaPairs = new HashSet<>();
            for (int i = 0; i < antennas.size(); i++) {
                for (int j = 0; j < antennas.size(); j++) {
                    if (i != j) {
                        antennaPairs.add(Pair.of(antennas.get(i), antennas.get(j)));
                    }
                }
            }
            frequencyAntennaPairs.put(f, antennaPairs);
        }

        Set<Point> antinodes = new HashSet<>();
        for (Map.Entry<Character, Set<Pair<Point, Point>>> fap : frequencyAntennaPairs.entrySet()) {
            Set<Pair<Point, Point>> antennaPairs = fap.getValue();
            for (Pair<Point, Point> antennaPair : antennaPairs) {
                Vector2D a = new Vector2D(antennaPair.getLeft().x, antennaPair.getLeft().y);
                Vector2D b = new Vector2D(antennaPair.getRight().x, antennaPair.getRight().y);

                double dist = a.distance(b);
                Vector2D dir = a.subtract(b).normalize();
                Vector2D antinode = a.add(dist, dir);
                if (bounds.isInBounds((int) Math.round(antinode.getX()), (int) Math.round(antinode.getY())))
                    antinodes.add(new Point((int) Math.round(antinode.getX()), (int) Math.round(antinode.getY())));
            }
        }
        System.out.println(antinodes.size());
    }
}
