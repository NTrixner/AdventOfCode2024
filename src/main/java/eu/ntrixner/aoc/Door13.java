package eu.ntrixner.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Door13 {

    public static final long COST_A = 3;
    public static final long COST_B = 1;

    public static class ClawMachine {
        public double aX, aY;
        public double bX, bY;
        public long pX, pY;

        public ClawMachine(String lineA, String lineB, String linePrice) {
            lineA = lineA.substring(lineA.indexOf("+") + 1);
            String lineAX = lineA.substring(0, lineA.indexOf(","));
            String lineAY = lineA.substring(lineA.indexOf("+") + 1);
            this.aX = Long.parseLong(lineAX);
            this.aY = Long.parseLong(lineAY);


            lineB = lineB.substring(lineB.indexOf("+") + 1);
            String lineBX = lineB.substring(0, lineB.indexOf(","));
            String lineBY = lineB.substring(lineB.indexOf("+") + 1);
            this.bX = Long.parseLong(lineBX);
            this.bY = Long.parseLong(lineBY);


            linePrice = linePrice.substring(linePrice.indexOf("=") + 1);
            String linePriceX = linePrice.substring(0, linePrice.indexOf(","));
            String linePriceY = linePrice.substring(linePrice.indexOf("=") +1);
            this.pX = Long.parseLong(linePriceX);
            this.pY = Long.parseLong(linePriceY);


        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/Input13.txt"));
        long totalCost = 0;
        for(int i = 0; i < lines.size(); i = i+4) {
            String lineA = lines.get(i);
            String lineB = lines.get(i+1);
            String linePrice = lines.get(i+2);
            ClawMachine cm = new ClawMachine(lineA, lineB, linePrice);
            cm.pX += 10000000000000L;
            cm.pY += 10000000000000L;

            double nB = (cm.aX * cm.pY - cm.aY * cm.pX) / (cm.aX * cm.bY - cm.aY * cm.bX);
            double nA = (cm.bY * cm.pX - cm.bX * cm.pY) / (cm.aX *  cm.bY - cm.aY * cm.bX);

            if((long) nA - nA == 0 && (long) nB - nB == 0) {
                totalCost += (long) (nA * COST_A + nB * COST_B);
            }
        }

        System.out.println("Total cost: " + totalCost);
    }

    public static void main1(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/Input13.txt"));
        long totalCost = 0;
        for(int i = 0; i < lines.size(); i = i+4) {
            String lineA = lines.get(i);
            String lineB = lines.get(i+1);
            String linePrice = lines.get(i+2);
            ClawMachine cm = new ClawMachine(lineA, lineB, linePrice);

            double nB = (cm.aX * cm.pY - cm.aY * cm.pX) / (cm.aX * cm.bY - cm.aY * cm.bX);
            double nA = (cm.bY * cm.pX - cm.bX * cm.pY) / (cm.aX *  cm.bY - cm.aY * cm.bX);

            if((int) nA - nA == 0 && (int) nB - nB == 0) {
                totalCost += (long) (nA * COST_A + nB * COST_B);
            }
        }

        System.out.println("Total cost: " + totalCost);
    }
}
