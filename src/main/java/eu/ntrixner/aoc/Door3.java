package eu.ntrixner.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Door3 {
    public static final String PATTERN = "(mul\\(\\d{1,3},\\d{1,3}\\))|do\\(\\)|don't\\(\\)";

    public static void main(String[] args) throws IOException {
        Pattern pattern = Pattern.compile(PATTERN);
        File f = new File("src/main/resources/Input3.txt");
        String s = Files.readString(f.toPath());
        ArrayList<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(s);
        boolean work = true;
        while (matcher.find()) {
            String x = matcher.group();
            if("do()".equals(x))
                work = true;
            else if("don't()".equals(x))
                work = false;
            else if(work)
                matches.add(matcher.group());
        }

        int sum = 0;
        for (String m : matches) {
            int num1 = Integer.parseInt(m.substring(m.indexOf('(') + 1, m.indexOf(',')));
            int num2 = Integer.parseInt(m.substring(m.indexOf(',') + 1, m.indexOf(')')));
            sum += num1 * num2;
        }

        System.out.println(sum);
    }

    public static final String PATTERN1 = "(mul\\(\\d{1,3},\\d{1,3}\\))";
    public static void main1(String[] args) throws IOException {
        Pattern pattern = Pattern.compile(PATTERN1);
        File f = new File("src/main/resources/Input3.txt");
        String s = Files.readString(f.toPath());
        ArrayList<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        int sum = 0;
        for (String m : matches) {
            int num1 = Integer.parseInt(m.substring(m.indexOf('(') + 1, m.indexOf(',')));
            int num2 = Integer.parseInt(m.substring(m.indexOf(',') + 1, m.indexOf(')')));
            sum += num1 * num2;
        }

        System.out.println(sum);
    }


}
