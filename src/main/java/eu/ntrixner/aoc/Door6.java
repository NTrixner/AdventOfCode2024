package eu.ntrixner.aoc;

import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Door6 {

    public static final List<Character> dirChar = List.of('^', '>', 'v', '<');
    public static final List<Point> dirs = List.of(new Point(0, -1), new Point(+1, 0), new Point(0, +1), new Point(-1, 0));

    public static class Guard {
        char[][] map;
        public int x, y, height, width, dir;
        public boolean outside;
        public List<Pair<Integer, Point>> history;

        public Guard(char[][] map, int x, int y, int dir){
            this.map = Utils.deepCopy(map);
            this.height = map.length;
            this.width = map[0].length;
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.outside = false;
            this.history = new ArrayList<>();
            history.add(Pair.of(dir, new Point(x, y)));
        }

        public void step()
        {
            int newX = x + dirs.get(dir).x;
            int newY = y + dirs.get(dir).y;
            if(newX < 0 || newX >= width || newY < 0 || newY >= height) {
                outside = true;
                return;
            }
            while(map[newY][newX] == '#'){
                dir = (dir + 1) % 4;
                newX = x + dirs.get(dir).x;
                newY = y + dirs.get(dir).y;
            }
            x = newX;
            y = newY;
            history.add(Pair.of(dir, new Point(x, y)));
        }

        public boolean hasLooped() {
            return history.size() > 1 && history.subList(1, history.size() - 1).contains(Pair.of(dir, new Point(x, y)));
        }
    }

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

        Guard g = new Guard(map, cx, cy, dirI);
        Set<Point> obstacles = new HashSet<>();
        while(!g.outside) {
            int oldX = g.x;
            int oldY = g.y;
            int oldDir = g.dir;

            g.step();

            if(wouldLoop(map, g.x, g.y, oldX, oldY, oldDir))
                obstacles.add(new Point(g.x, g.y));
        }

        System.out.println("Obstacles: " + obstacles.size());
        System.out.println("CoveredTiles: " + (g.history.stream().map(Pair::getRight).collect(Collectors.toSet()).size()));
        System.out.println("Checks took " + (System.currentTimeMillis() - ms) + "ms");
    }

    public static boolean wouldLoop(char[][] map, int obstacleX, int obstacleY, int startX, int startY, int startDir) {
        Guard g = new Guard(map, startX, startY, startDir);
        g.map[obstacleY][obstacleX] = '#';
        while(!g.outside) {
            g.step();
            if(g.hasLooped())
                return true;
        }
        return false;
    }
}
