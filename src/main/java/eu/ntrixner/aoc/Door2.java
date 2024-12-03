package eu.ntrixner.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Door2 {
    public static void main(String[] args) throws IOException {
        File f = new File("src/main/resources/Input2.txt");
        long num = Files.lines(f.toPath()).filter(line -> {
                    String[] split = line.split(" ");
                    int[] nums = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
                    for(int x = 0; x < nums.length; x++) {
                        boolean works = true;
                        int[] leading = Arrays.copyOfRange(nums, 0, x);
                        int[] trailing = Arrays.copyOfRange(nums, x + 1, nums.length);
                        int[] subArr = IntStream.concat(Arrays.stream(leading), Arrays.stream(trailing)).toArray();
                        int sign = 0;
                        for (int i = 1; i < subArr.length; i++) {
                                int diff = subArr[i] - subArr[i - 1];
                                if (Math.abs(sign - Integer.signum(diff)) >= 2 || Integer.signum(diff) == 0) {
                                    works = false;
                                }
                                sign = Integer.signum(diff);
                                if (Math.abs(diff) > 3)
                                    works = false;
                        }
                        if(works)
                            return true;
                    }
                    return false;
                })
                .count();

        System.out.println(num);
    }

    public static void main1(String[] args) throws IOException {
        File f = new File("src/main/resources/Input2.txt");
        long num = Files.lines(f.toPath()).filter(line -> {
                    String[] split = line.split(" ");
                    int[] nums = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
                    int sign = 0;
                    for (int i = 1; i < nums.length; i++) {
                        int diff = nums[i] - nums[i - 1];
                        if (Math.abs(sign - Integer.signum(diff)) >= 2 || Integer.signum(diff) == 0) {
                            return false;
                        }
                        sign = Integer.signum(diff);
                        if (Math.abs(diff) > 3)
                            return false;
                    }
                    return true;
                })
                .count();

        System.out.println(num);
    }


}
