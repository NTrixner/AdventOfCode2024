package eu.ntrixner.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public class Door9 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/Input9.txt"));
        long checksum = 0;
        for (String line : lines) {
            List<Integer> decoded = decode(line);
            int highestId = decoded.stream().max(Integer::compareTo).orElse(-1);
            List<Integer> defragged = defragBlocks(decoded, highestId);
            System.out.println(defragged.stream().map(i -> i == -1 ? '.' : i + "").collect(Collector.of(
                    StringBuilder::new,
                    StringBuilder::append,
                    StringBuilder::append,
                    StringBuilder::toString)));
            checksum += getChecksum(defragged);
        }
        System.out.println(checksum);
    }

    public static void main1(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/Input9.txt"));
        long checksum = 0;
        for (String line : lines) {
            List<Integer> decoded = decode(line);
            List<Integer> defragged = defrag(decoded);
            System.out.println(defragged.stream().map(i -> i == -1 ? '.' : i + "").collect(Collector.of(
                    StringBuilder::new,
                    StringBuilder::append,
                    StringBuilder::append,
                    StringBuilder::toString)));
            checksum += getChecksum(defragged);
        }
        System.out.println(checksum);
    }

    private static long getChecksum(List<Integer> defragged) {
        long checksum = 0;
        for (int i = 0; i < defragged.size(); i++) {
            if (defragged.get(i) != -1) {
                checksum += defragged.get(i) * i;
            }
        }
        return checksum;
    }

    private static List<Integer> defrag(List<Integer> decoded) {

        int x = 0;
        while (x < decoded.size()) {
            int i = 0;
            int j = decoded.size() - 1;
            while (i < decoded.size() && decoded.get(i) != -1) {
                i++;
            }
            while (j >= 0 && decoded.get(j) == -1) {
                j--;
            }
            Collections.swap(decoded, i, j);
            x++;
        }
        return decoded;
    }

    private static List<Integer> defragBlocks(List<Integer> decoded, int highestId) {

        int x = highestId;
        while (x >= 0) {
            // Get Block size
            int blockStart = decoded.indexOf(x);
            int blockEnd = decoded.lastIndexOf(x);
            int blockSize = blockEnd - blockStart + 1;

            //Check if there's a free space
            int i = 0;
            int freeSize = 0;
            int freeStart = 0;
            int freeEnd = 0;

            while (i < blockStart && freeSize < blockSize) {
                while (i < decoded.size() && decoded.get(i) != -1) {
                    i++;
                }
                freeStart = i;
                while (i < decoded.size() && decoded.get(i) == -1) {
                    i++;
                }
                freeEnd = i - 1;
                freeSize = freeEnd - freeStart + 1;
            }

            if (freeSize >= blockSize && freeStart < blockStart) {
                for (int y = 0; y < blockSize; y++) {
                    Collections.swap(decoded, blockStart + y, freeStart + y);
                }
            }
            x--;
        }
        return decoded;
    }

    private static List<Integer> decode(String line) {
        List<Integer> decoded = new ArrayList<>();
        boolean isFile = true;
        int id = 0;
        for (char c : line.toCharArray()) {
            int num = Integer.parseInt(c + "");
            for (int i = 0; i < num; i++) {
                if (!isFile) {
                    decoded.add(-1);
                } else {
                    decoded.add(id);
                }
            }
            if (isFile)
                id++;
            isFile = !isFile;
        }
        return decoded;
    }


}
