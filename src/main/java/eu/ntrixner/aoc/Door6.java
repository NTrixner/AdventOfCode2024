package eu.ntrixner.aoc;

import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Door6 {

    public static final List<Character> dirChar = List.of('^', '>', 'v', '<');
    public static final List<Point> dirs = List.of(new Point(0, -1), new Point(+1, 0), new Point(0, +1), new Point(-1, 0));

    public static void main(String[] args) throws IOException {
        long ms = System.currentTimeMillis();
        char[][] map = Utils.readCharsFromFile("src/main/resources/Input6.txt");
        int height = map.length;
        int width = map[0].length;
        int cx = 0;
        int cy = 0;
        int dirI = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dirChar.contains(map[y][x])) {
                    dirI = dirChar.indexOf(map[y][x]);
                    cx = x;
                    cy = y;
                }
            }
        }

        System.out.println("Preparation took " + (System.currentTimeMillis() - ms) + "ms");
        ms = System.currentTimeMillis();

        List<Point> walkedThrough = getWalkedThrough(cx, cy, height, width, dirI, map);
        walkedThrough = walkedThrough.subList(1, walkedThrough.size());

        Set<Point> walkedThroughSet = Set.copyOf(walkedThrough);

        final int fcx = cx;
        final int fcy = cy;
        final int fDirI = dirI;

        System.out.println("WalkedThrough took " + (System.currentTimeMillis() - ms) + "ms");
        ms = System.currentTimeMillis();
        Set<Point> obstacles = walkedThroughSet.parallelStream().filter(p -> {
                    char[][] withObstacle = Utils.deepCopy(map);
                    withObstacle[p.y][p.x] = 'O';
                    return checkLoop(withObstacle, fcx, fcy, height, width, fDirI);
                })
                .collect(Collectors.toSet());

        System.out.println(obstacles.size());
        System.out.println("Checks took " + (System.currentTimeMillis() - ms) + "ms");
    }


    private static boolean checkLoop(char[][] map, int cx, int cy, int height, int width, int dirI) {
        List<Pair<Integer, Point>> visited = new ArrayList<>();
        while (!notOnMap(cx, cy, height, width)) {
            int newy = cy + dirs.get(dirI).y;
            int newx = cx + dirs.get(dirI).x;
            if (notOnMap(newx, newy, height, width)) {
                break;
            }
            if (visited.contains(Pair.of(dirI, new Point(cx, cy))))
                return true;
            visited.add(Pair.of(dirI, new Point(cx, cy)));

            if (map[newy][newx] == '#' || map[newy][newx] == 'O') {
                dirI = (dirI + 1) % 4;
                newy = cy + dirs.get(dirI).y;
                newx = cx + dirs.get(dirI).x;
            }
            cy = newy;
            cx = newx;
        }
        return false;
    }

    private static List<Point> getWalkedThrough(int cx, int cy, int height, int width, int dirI, char[][] map) {
        List<Point> walkedThrough = new ArrayList<>();

        while (!notOnMap(cx, cy, height, width)) {
            walkedThrough.add(new Point(cx, cy));
            int newy = cy + dirs.get(dirI).y;
            int newx = cx + dirs.get(dirI).x;
            if (notOnMap(newx, newy, height, width)) {
                break;
            }
            if (map[newy][newx] == '#') {
                dirI = (dirI + 1) % 4;
                newy = cy + dirs.get(dirI).y;
                newx = cx + dirs.get(dirI).x;
            }
            cy = newy;
            cx = newx;
        }
        return walkedThrough;
    }

    private static boolean notOnMap(int cx, int cy, int height, int width) {
        return (cy < 0 || cy >= height) || (cx < 0 || cx >= width);
    }

    public static void main1(String[] args) throws IOException {
        char[][] map = Utils.readCharsFromFile("src/main/resources/Input6.txt");
        int height = map.length;
        int width = map[0].length;
        int cx = 0;
        int cy = 0;
        int dirI = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dirChar.contains(map[y][x])) {
                    dirI = dirChar.indexOf(map[y][x]);
                    cx = x;
                    cy = y;
                }
            }
        }

        while (!notOnMap(cx, cy, height, width)) {
            map[cy][cx] = 'X';
            int newy = cy + dirs.get(dirI).y;
            int newx = cx + dirs.get(dirI).x;
            if (notOnMap(newx, newy, height, width)) {
                break;
            }
            if (map[newy][newx] == '#') {
                dirI = (dirI + 1) % 4;
                newy = cy + dirs.get(dirI).y;
                newx = cx + dirs.get(dirI).x;
            }
            cy = newy;
            cx = newx;
        }

        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x] == 'X') {
                    count++;
                }
            }
        }
        System.out.println(count);
    }
}
