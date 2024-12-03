package eu.ntrixner.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class Door1 {
    public static void main(String[] args) throws IOException {
        File f = new File("src/main/resources/Input1.txt");
        String[] fStrings = Files.readAllLines(f.toPath()).toArray(String[]::new);
        int fLength = fStrings.length;
        int[] l = new int[fLength];
        int[] r = new int[fLength];

        for(int i = 0; i < fStrings.length; i++) {
            String[] lSplit = fStrings[i].split(" ");
            l[i] = Integer.parseInt(lSplit[0]);
            r[i] = Integer.parseInt(lSplit[lSplit.length - 1]);
        }

        Arrays.sort(l);
        Arrays.sort(r);

        int similarity = 0;

        for(int i = 0; i < l.length; i++) {
            int count = 0;
            for(int j = 0; j < r.length; j++) {
                if(l[i] == r[j]) {
                    count++;
                }
            }
            similarity += count * l[i];
        }

        System.out.println(similarity);
    }

    public static void main1(String[] args) throws IOException {
        File f = new File("src/main/resources/Input1.txt");
        String[] fStrings = Files.readAllLines(f.toPath()).toArray(String[]::new);
        int fLength = fStrings.length;
        int[] l = new int[fLength];
        int[] r = new int[fLength];

        for(int i = 0; i < fStrings.length; i++) {
            String[] lSplit = fStrings[i].split(" ");
            l[i] = Integer.parseInt(lSplit[0]);
            r[i] = Integer.parseInt(lSplit[lSplit.length - 1]);
        }

        Arrays.sort(l);
        Arrays.sort(r);

        int diff = 0;

        for(int i = 0; i < l.length; i++) {
            diff += Math.abs(l[i] - r[i]);
        }

        System.out.println(diff);
    }


}
