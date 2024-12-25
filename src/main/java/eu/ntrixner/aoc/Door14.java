package eu.ntrixner.aoc;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Door14 {

    public static final long MAP_X = 101;
    public static final long MAP_Y = 103;


    public static class Robot {
        Point pos;
        Point vel;

        public Robot(String inputLine) {
            String px = inputLine.substring(2, inputLine.indexOf(","));
            String py = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf(" "));
            inputLine = inputLine.substring(inputLine.indexOf(" ") + 1);
            String vx = inputLine.substring(2, inputLine.indexOf(","));
            String vy = inputLine.substring(inputLine.indexOf(",")+1);

            this.pos = new Point(Integer.parseInt(px), Integer.parseInt(py));
            this.vel = new Point(Integer.parseInt(vx), Integer.parseInt(vy));
        }

        public void move() {
            this.pos = new Point((int) ((pos.x + MAP_X + vel.x) % MAP_X), (int) ((pos.y + MAP_Y + vel.y) % MAP_Y));
        }

        public int quadrant() {
            if(pos.x < MAP_X / 2) {
                if(pos.y < MAP_Y / 2) {
                    return 1;
                } else if(pos.y > MAP_Y/2) {
                    return 2;
                }
            } else if(pos.x > MAP_X/2) {
                if(pos.y < MAP_Y / 2) {
                    return 3;
                } else if(pos.y > MAP_Y/2) {
                    return 4;
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/Input14.txt"));
        List<Robot> robots = lines.stream().map(Robot::new).toList();
        int[][] test = new int[(int) MAP_Y][(int) MAP_X];

        robots.forEach(r -> {
            test[r.pos.y][r.pos.x]++;
        });
        int i = 0;
        while(true) {
            robots.forEach(Robot::move);
            i++;

            //Only check if all robots are in unique positions
            if(robots.stream().map(r -> r.pos).distinct().count() == robots.size()) {
                for (int[] ints : test) {
                    Arrays.fill(ints, 0);
                }
                robots.forEach(r -> {
                    test[r.pos.y][r.pos.x]++;
                });
                for(int[] ints : test) {
                    for(int j : ints) {
                        System.out.print((j == 0 ? " " : j) + " ");
                    }
                    System.out.println();
                }
                System.out.println("Seconds: " + i);
                System.in.read();
            }
        }
    }

    public static void main1(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/Input14.txt"));
        List<Robot> robots = lines.stream().map(Robot::new).toList();
        for(int i = 0; i < 100; i++) {
            robots.forEach(Robot::move);
        }
        int[][] test = new int[(int) MAP_Y][(int) MAP_X];
        for (int[] ints : test) {
            Arrays.fill(ints, 0);
        }
        robots.forEach(r -> {
            test[r.pos.y][r.pos.x]++;
        });
        for(int[] ints : test) {
            for(int i : ints) {
                System.out.print(i + " ");
            }
            System.out.println();
        }

        long q1 = robots.stream().map(Robot::quadrant).filter(q -> q == 1).count();
        long q2 = robots.stream().map(Robot::quadrant).filter(q -> q == 2).count();
        long q3 = robots.stream().map(Robot::quadrant).filter(q -> q == 3).count();
        long q4 = robots.stream().map(Robot::quadrant).filter(q -> q == 4).count();
        System.out.println("Safety factor: " + (q1 * q2 * q3 * q4));
    }

}
