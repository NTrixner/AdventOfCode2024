package eu.ntrixner.aoc;

import eu.ntrixner.aoc.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Door11 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/Input11.txt"));

        for (String line : lines) {
            String[] split = line.split(" ");
            List<Long> stones = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
            Map<Pair<Long, Integer>, Long> counts = new HashMap<>();
            int depth = 75;
            for (Long stone : stones) {
                blinkRecursively(stone, depth, counts);
            }

            long count = 0;
            for (Long stone : stones) {
                count += counts.get(Pair.of(stone, depth));
            }

            System.out.println(count);

        }
    }

    private static long blinkRecursively(Long stone, int depth, Map<Pair<Long, Integer>, Long> counts) {
        long newNum = 0;
        if (depth == 0) {
            newNum = 1;
        } else if(counts.containsKey(Pair.of(stone, depth))) {
            return counts.get(Pair.of(stone, depth));
        }else if (stone == 0) {
            newNum = blinkRecursively(1L, depth - 1, counts);
        } else if (String.valueOf(stone).length() % 2 == 0) {
            String numS = String.valueOf(stone);
            long num1 = Long.parseLong(numS.substring(0, numS.length() / 2));
            newNum += blinkRecursively(num1, depth - 1, counts);
            long num2 = Long.parseLong(numS.substring(numS.length() / 2));
            newNum += blinkRecursively(num2, depth - 1, counts);
        } else {
            newNum = blinkRecursively(stone * 2024, depth - 1, counts);
        }
        counts.put(Pair.of(stone, depth), newNum);
        return newNum;
    }

    private static List<Long> blink(List<Long> stones) {
        List<Long> newStones = new ArrayList<>();
        for (int i = 0; i < stones.size(); i++) {
            Long num = stones.get(i);
            if (num == 0) {
                newStones.add(1L);
            } else if (String.valueOf(num).length() % 2 == 0) {
                String numS = String.valueOf(num);
                newStones.add(Long.parseLong(numS.substring(0, numS.length() / 2)));
                newStones.add(Long.parseLong(numS.substring(numS.length() / 2)));
            } else {
                newStones.add(num * 2024);
            }
        }
        return newStones;
    }


    public static void main1(String[] args) throws IOException {
        char[][] map = Utils.readCharsFromFile("src/main/resources/Input10.txt");
    }

}
