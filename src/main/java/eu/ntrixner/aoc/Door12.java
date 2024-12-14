package eu.ntrixner.aoc;

import eu.ntrixner.aoc.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Door12 {

    public static final Point[] directions = new Point[]{
            new Point(0, -1),
            new Point(+1, 0),
            new Point(0, +1),
            new Point(-1, 0)
    };

    public static class Region {
        public char c;
        public Set<Point> region = new HashSet<>();

        public int perimeter() {
            int perimeter = 0;
            for (Point p : region) {
                if (!region.contains(new Point(p.x - 1, p.y)))
                    perimeter++;
                if (!region.contains(new Point(p.x + 1, p.y)))
                    perimeter++;
                if (!region.contains(new Point(p.x, p.y - 1)))
                    perimeter++;
                if (!region.contains(new Point(p.x, p.y + 1)))
                    perimeter++;
            }
            return perimeter;
        }

        public boolean neighbors(Point p) {
            if (region.contains(new Point(p.x - 1, p.y)))
                return true;
            if (region.contains(new Point(p.x + 1, p.y)))
                return true;
            if (region.contains(new Point(p.x, p.y - 1)))
                return true;
            if (region.contains(new Point(p.x, p.y + 1)))
                return true;
            return false;
        }

        public int area() {
            return region.size();
        }

        public void swallow(Region r) {
            region.addAll(r.region);
        }

        public int sides() {
            //if region contains one or two points, return 4
            if (region.size() < 3)
                return 4;
            int sides = 0;
            List<Edge> edges = new ArrayList<>();
            for (Point p : region) {
                if (!region.contains(new Point(p.x - 1, p.y)))
                    edges.add(new Edge(Dir.LEFT, Set.of(Pair.of((double) p.x - 0.5, (double) p.y ))));
                if (!region.contains(new Point(p.x + 1, p.y)))
                    edges.add(new Edge(Dir.RIGHT, Set.of(Pair.of((double) p.x + 0.5, (double) p.y))));
                if (!region.contains(new Point(p.x, p.y - 1)))
                    edges.add(new Edge(Dir.UP, Set.of(Pair.of((double) p.x, (double) p.y - 0.5))));
                if (!region.contains(new Point(p.x, p.y + 1)))
                    edges.add(new Edge(Dir.DOWN, Set.of(Pair.of((double) p.x, (double) p.y + 0.5 ))));
            }
            boolean foundNewPair;

            do {
                foundNewPair = false;
                List<Edge> toRemove = new ArrayList<>();
                for(Edge e1 : edges) {
                    for(Edge e2 : edges) {
                        if(e1 != e2) {
                            if(e1.continues(e2) && !toRemove.contains(e1)) {
                                e1.swallow(e2);
                                toRemove.add(e2);
                                foundNewPair = true;
                            }
                        }
                    }
                }
                edges.removeAll(toRemove);
            } while (foundNewPair);

            return edges.size();
        }
    }

    public static class Edge {
        Dir dir;
        Set<Pair<Double, Double>> points = new HashSet<>();

        public Edge(Dir dir, Set<Pair<Double, Double>> points) {
            this.dir = dir;
            this.points.addAll(points);
        }
        public boolean continues(Edge other) {
            if(other.dir != dir) {
                return false;
            }
            if(points.stream().anyMatch(p -> other.points.contains(p))) {
                return true;
            }
            for(var p1 : points) {
                for(var p2 : other.points) {
                    if (dir == Dir.UP || dir == Dir.DOWN) {
                        if(Math.abs(p1.getLeft() - p2.getLeft()) == 1 && Objects.equals(p1.getRight(), p2.getRight()))
                            return true;
                    } else {
                        if(Math.abs(p1.getRight() - p2.getRight()) == 1 && Objects.equals(p1.getLeft(), p2.getLeft()))
                            return true;
                    }
                }
            }
            return false;
        }

        public void swallow(Edge other) {
            this.points.addAll(other.points);
        }
    }

    public static enum Dir {
        UP, DOWN, LEFT, RIGHT
    }

    public static void main(String[] args) throws IOException {
        char[][] map = Utils.readCharsFromFile("src/main/resources/Input12.txt");
        //Grow regions
        Map<Character, List<Region>> regionsMap = new HashMap<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                char c = map[y][x];
                Point p = new Point(x, y);
                if (regionsMap.containsKey(c)) {
                    Optional<Region> regionOp = regionsMap.get(map[y][x])
                            .stream()
                            .filter(r -> r.neighbors(p))
                            .findAny();
                    if (regionOp.isPresent()) {
                        regionOp.get().region.add(p);
                    } else {
                        Region r = new Region();
                        r.region.add(p);
                        r.c = c;
                        regionsMap.get(c).add(r);
                    }
                } else {
                    ArrayList<Region> regions = new ArrayList<>();
                    Region r = new Region();
                    r.region.add(p);
                    r.c = c;
                    regions.add(r);
                    regionsMap.put(c, regions);
                }
            }
        }
        boolean foundNeighbor;
        do {
            foundNeighbor = false;
            //Check if regions neighboring
            for (Map.Entry<Character, List<Region>> entry : regionsMap.entrySet()) {
                List<Region> toRemove = new ArrayList<>();
                for (Region r1 : entry.getValue()) {
                    for (Region r2 : entry.getValue()) {
                        if (r1 != r2 && !toRemove.contains(r1)) {
                            if (r1.region.stream().anyMatch(r2::neighbors)) {
                                r1.swallow(r2);
                                toRemove.add(r2);
                                foundNeighbor = true;
                            }
                        }
                    }
                }
                entry.getValue().removeAll(toRemove);
            }
        } while (foundNeighbor);
        long totalPrice1 = 0;
        long totalPrice2 = 0;
        for (Map.Entry<Character, List<Region>> entry : regionsMap.entrySet()) {
            totalPrice1 += entry.getValue().stream().mapToInt(r -> r.perimeter() * r.area()).sum();
            totalPrice2 += entry.getValue().stream().mapToInt(r -> r.area() * r.sides()).sum();
        }
        System.out.println("General price: " + totalPrice1);
        System.out.println("Discounted price: " + totalPrice2);
    }
}
