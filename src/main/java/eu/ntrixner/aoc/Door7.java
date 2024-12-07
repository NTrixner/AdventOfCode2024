package eu.ntrixner.aoc;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.List;
import java.util.*;

public class Door7 {


    public static enum Operation {
        ADD, MULT, CONCAT
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/Input7.txt"));
        long maxPerms = lines.stream().mapToLong(line -> line.chars().filter(c -> c == ' ').count()).max().orElse(0);
        Map<Long, List<List<Operation>>> perms = getPermutations(maxPerms);
        long count = 0;
        long total = 0;
        for (String line : lines) {
            String resString = line.substring(0, line.indexOf(":"));
            String inputString = line.substring(line.indexOf(":") + 1).trim();
            String[] inputStrings = inputString.split(" ");
            long[] inputs = Arrays.stream(inputStrings).mapToLong(Long::parseLong).toArray();
            long res = Long.parseLong(resString);
            List<List<Operation>> permsList = perms.get(inputs.length - 1L);
            if(checkResultWithPerms(inputs, res, permsList)) {
                count++;
                total += res;
            }

        }

        System.out.println(count + " worked, total result is " + total);
    }

    public static boolean checkResultWithPerms(long[] inputs, long result, List<List<Operation>> perms) {
        for(List<Operation> perm : perms) {
            long partialRes = inputs[0];
            for(long i = 1; i < inputs.length; i++) {
                long operand = inputs[(int) i];
                partialRes = switch (perm.get((int) (i - 1))) {
                    case ADD -> partialRes + operand;
                    case MULT -> partialRes * operand;
                    case CONCAT -> Long.parseLong(partialRes + "" + operand);
                };
            }
            if(partialRes == result)
                return true;
        }
        return false;
    }


    private static Map<Long, List<List<Operation>>> getPermutations(long maxPerms) {
        Map<Long, List<List<Operation>>> permMap = new HashMap<>();
        List<List<Operation>> firstPerm = new ArrayList<>();
        firstPerm.add(Arrays.asList(Operation.ADD));
        firstPerm.add(Arrays.asList(Operation.MULT));
        firstPerm.add(Arrays.asList(Operation.CONCAT));
        permMap.put(1L, firstPerm);
        for (long i = 2; i <= maxPerms; i++) {
            List<List<Operation>> smallerPerms = permMap.get(i - 1);
            List<List<Operation>> largerPerms = new ArrayList<>();
            for (Operation operation : Operation.values()) {
                for(List<Operation> smallerPerm : smallerPerms) {
                    List<Operation> newPerm = new ArrayList<>(smallerPerm);
                    newPerm.add(operation);
                    largerPerms.add(newPerm);
                }
            }
            permMap.put(i, largerPerms);
        }

        return permMap;
    }
}
