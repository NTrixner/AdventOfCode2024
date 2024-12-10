package eu.ntrixner.aoc;

import eu.ntrixner.aoc.utils.Utils;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collector;

public class Door10 {

    public static void main(String[] args) throws IOException {
        char[][] map = Utils.readCharsFromFile("src/main/resources/Input10.txt");
        List<Point> trailheads = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == '0')
                    trailheads.add(new Point(x, y));
            }
        }
        List<Long> trailHeadVals = new ArrayList<>();
        for(Point trailHead : trailheads) {
            trailHeadVals.add(followUniqueTrail(map, trailHead, '0'));
        }

        System.out.println(trailHeadVals.stream().mapToInt(Long::intValue).sum());
    }


    private static long followUniqueTrail(char[][] map, Point pos, char current) {
        if (current == '9')
            return 1;
        long sum = 0;
        for (Point newPos : getIncreasingNeighbors(map, pos, current)) {
           sum += followUniqueTrail(map, newPos, map[newPos.y][newPos.x]);
        }
        return sum;
    }

    public static void main1(String[] args) throws IOException {
        char[][] map = Utils.readCharsFromFile("src/main/resources/Input10.txt");
        List<Point> trailheads = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] == '0')
                    trailheads.add(new Point(x, y));
            }
        }
        List<Long> trailHeadVals = new ArrayList<>();
        for(Point trailHead : trailheads) {
            trailHeadVals.add(followTrail(map, trailHead, '0').stream().filter(p -> map[p.y][p.x] == '9').count());
        }

        System.out.println(trailHeadVals.stream().mapToInt(Long::intValue).sum());
    }


    private static Set<Point> followTrail(char[][] map, Point pos, char current) {
        if (current == '9')
            return Set.of(pos);
        Set<Point> neighbors = new HashSet<>();
        for (Point newPos : getIncreasingNeighbors(map, pos, current)) {
            neighbors.addAll(followTrail(map, newPos, map[newPos.y][newPos.x]));
        }
        return neighbors;
    }

    private static List<Point> getIncreasingNeighbors(char[][] map, Point pos, char current) {
        List<Point> neighbors = new ArrayList<>();
        //TOP
        if(pos.y > 0 && current - map[pos.y - 1][pos.x] == -1)
            neighbors.add(new Point( pos.x, pos.y - 1));

        //BOTTOM
        if(pos.y < map.length - 1 && current - map[pos.y + 1][pos.x] == -1)
            neighbors.add(new Point(pos.x, pos.y + 1));

        //LEFT
        if(pos.x > 0 && current - map[pos.y][pos.x -1] == -1)
            neighbors.add(new Point(pos.x - 1, pos.y));

        //RIGHT
        if(pos.x < map[0].length - 1 && current - map[pos.y][pos.x + 1] == -1)
            neighbors.add(new Point(pos.x + 1, pos.y));

        return neighbors;
    }

}
