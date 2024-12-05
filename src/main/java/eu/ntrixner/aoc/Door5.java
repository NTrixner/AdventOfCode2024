package eu.ntrixner.aoc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Door5 {
    public static void main(String[] args) throws IOException {
        File f = new File("src/main/resources/Input5.txt");
        List<String> lines = Files.readAllLines(f.toPath());
        List<Pair<Integer, Integer>> rules = new ArrayList<>();
        boolean rulesDone = false;
        int count = 0;
        for (String line : lines) {
            if (!StringUtils.isEmpty(line) && !rulesDone) {
                //Read rule
                rules.add(readRule(line));
            } else if (!StringUtils.isEmpty(line) && rulesDone) {
                //Check rules, if valid, get median
                count += checkInverseSortAndGetMedian(line, rules);
            } else if (StringUtils.isEmpty(line) && !rulesDone) {
                rulesDone = true;
            }
        }

        System.out.println(count);
    }

    public static void main1(String[] args) throws IOException {
        File f = new File("src/main/resources/Input5.txt");
        List<String> lines = Files.readAllLines(f.toPath());
        List<Pair<Integer, Integer>> rules = new ArrayList<>();
        boolean rulesDone = false;
        int count = 0;
        for (String line : lines) {
            if (!StringUtils.isEmpty(line) && !rulesDone) {
                //Read rule
                rules.add(readRule(line));
            } else if (!StringUtils.isEmpty(line) && rulesDone) {
                //Check rules, if valid, get median
                count += checkAndGetMedian(line, rules);
            } else if (StringUtils.isEmpty(line) && !rulesDone) {
                rulesDone = true;
            }
        }

        System.out.println(count);
    }

    private static int checkInverseSortAndGetMedian(String line, List<Pair<Integer, Integer>> rules) {
        List<Integer> numbers = Arrays.stream(line.split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
        int median = 0;
        boolean isValid = isValid(numbers, rules);
        if(!isValid) {
            //SORT
            while(!isValid(numbers, rules)) {
                for(Pair<Integer, Integer> rule : rules) {
                    if(!isValid(numbers, List.of(rule))) {
                        Collections.swap(numbers, numbers.indexOf(rule.getLeft()), numbers.indexOf(rule.getRight()));
                    }
                }
            }
            //GET MEDIAN
            median = numbers.get((int)Math.floor(numbers.size() / 2f));
        }
        return median;
    }

    private static int checkAndGetMedian(String line, List<Pair<Integer, Integer>> rules) {
        List<Integer> numbers = Arrays.stream(line.split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList();
        int median = 0;
        boolean isValid =  isValid(numbers, rules);
        if(isValid) {
            median = numbers.get((int)Math.floor(numbers.size() / 2f));
        }
        return median;
    }

    private static boolean isValid(List<Integer> numbers, List<Pair<Integer, Integer>> rules) {
        boolean isValid = true;
        rules = rules
                .stream()
                .filter(p -> numbers.contains(p.getLeft()) && numbers.contains(p.getRight()))
                .toList();
        for (int i = 0; i < numbers.size(); i++) {
            if (i < numbers.size() - 1) {
                List<Integer> futurePages = numbers.subList(i + 1, numbers.size());
                int currentNum = numbers.get(i);
                boolean isNumberValid = rules
                        .stream()
                        .filter(p -> p.getRight().equals(currentNum))
                        .noneMatch(p -> futurePages.contains(p.getLeft()));
                isValid = isValid
                        && isNumberValid;
            }
        }
        return isValid;
    }

    private record isValid(boolean isValid, List<Integer> numbers) {
    }

    private static Pair<Integer, Integer> readRule(String line) {
        String[] split = line.split("\\|");
        return Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }


}
