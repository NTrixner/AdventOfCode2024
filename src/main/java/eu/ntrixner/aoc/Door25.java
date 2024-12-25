package eu.ntrixner.aoc;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Door25 {

    private static int KEY_SIZE = 5;

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/Input25.txt"));

        List<List<String>> keysOrLocks = new ArrayList<>();
        int i = 0;
        for(String line : lines) {
            if(StringUtils.isEmpty(line)) {
                i++;
            } else {
                if(keysOrLocks.size() < i + 1) {
                    keysOrLocks.add(new ArrayList<>());
                }
                keysOrLocks.get(i).add(line);
            }
        }

        List<List<Integer>> keys = new ArrayList<>();
        List<List<Integer>> locks = new ArrayList<>();
        for(List<String> key : keysOrLocks) {
            if(key.get(0).contains(".")) {
                ArrayList<Integer> newKey = new ArrayList<>();
                for(int x = 0; x < key.get(0).length(); x++) {
                    newKey.add(0);
                    for(int y = 1; y < key.size() - 1; y++) {
                        char c = key.get(y).charAt(x);
                        if(c == '#') {
                            newKey.set(x, newKey.get(x) + 1);
                        }
                    }
                }
                keys.add(newKey);
            } else if(key.get(key.size() - 1).contains(".")) {
                ArrayList<Integer> newLock = new ArrayList<>();

                for(int x = 0; x < key.get(0).length(); x++) {
                    newLock.add(0);
                    for(int y = key.size() - 1; y > 0; y--) {
                        char c = key.get(y).charAt(x);
                        if(c == '#') {
                            newLock.set(x, newLock.get(x) + 1);
                        }
                    }
                }
                locks.add(newLock);
            }
        }

        int fits = 0;
        for(int k = 0; k < keys.size(); k++) {
            for(int l = 0; l < locks.size(); l++) {
                List<Integer> key = keys.get(k);
                List<Integer> lock = locks.get(l);
                boolean thisFits = true;
                for(int j = 0; j < key.size(); j++) {
                    if(key.get(j) + lock.get(j) > KEY_SIZE) {
                        thisFits = false;
                        break;
                    }
                }
                if(thisFits) {
                    fits++;
                }
            }
        }

        System.out.println("Fits: " + fits);
    }
}
