package eu.ntrixner.aoc;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Door24 {
    public enum Operation {
        AND, OR, XOR;
    }

    public static class Gate {
        public String name;
        public boolean value;
    }

    public static class InputGate extends Gate {
    }

    public static class CalcGate extends Gate implements Cloneable {
        public Gate inputA;
        public String inputAS;
        public Gate inputB;
        public String inputBS;
        public Operation operation;
        public boolean valueSet;

        @Override
        protected CalcGate clone() {
            CalcGate clone = new CalcGate();
            clone.inputA = this.inputA;
            clone.inputAS = this.inputAS;
            clone.inputB = this.inputB;
            clone.inputBS = this.inputBS;
            clone.operation = this.operation;
            clone.valueSet = this.valueSet;
            clone.name = this.name;
            clone.value = this.value;
            return clone;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> values = Files.readAllLines(Path.of("src/main/resources/Input24.txt"));

        List<Gate> gates = getGates(values);
        List<Boolean> inputGatesX = gates.stream()
                .filter(g -> g instanceof InputGate)
                .filter(g -> g.name.startsWith("x"))
                .sorted(Comparator.comparing(g -> g.name))
                .map(g -> g.value)
                .collect(Collectors.toList());
        List<Boolean> inputGatesY = gates.stream()
                .filter(g -> g instanceof InputGate)
                .filter(g -> g.name.startsWith("y"))
                .sorted(Comparator.comparing(g -> g.name))
                .map(g -> g.value)
                .collect(Collectors.toList());

        long x = getNumFromBits(inputGatesX);
        long y = getNumFromBits(inputGatesY);
        long targetZ = x + y;
        long z = 0;
        System.out.println("x: " + x + " + y: " + y + " = z: " + targetZ);
        List<CalcGate> unlinkedGates = new ArrayList<>(gates.stream()
                .filter(l -> l instanceof CalcGate)
                .map(l -> (CalcGate) l).collect(Collectors.toList()));
        while (!unlinkedGates.isEmpty()) {
            unlinkedGates.forEach(ulg -> {
                Gate inputA = gates.stream().filter(g -> g.name.equals(ulg.inputAS)).findFirst().orElse(null);
                Gate inputB = gates.stream().filter(g -> g.name.equals(ulg.inputBS)).findFirst().orElse(null);
                ulg.inputA = inputA;
                ulg.inputB = inputB;
            });
            unlinkedGates.removeIf(g -> g.inputA != null && g.inputB != null);
        }
        List<CalcGate> wrong = new ArrayList<>();
        List<CalcGate> calcGates = gates.stream()
                .filter(c -> c instanceof CalcGate)
                .map(c -> (CalcGate) c)
                .collect(Collectors.toList());
        int bitAmount = inputGatesX.size();
        String ci = null;
        boolean carryWrong = false;
        for (int i = 0; i < bitAmount; i++) {
            String xi = "x" + String.format("%02d", i);
            String yi = "y" + String.format("%02d", i);
            String zi = "z" + String.format("%02d", i);
            List<CalcGate> firstAdder = calcGates.stream()
                    .filter(cg -> (xi.equals(cg.inputA.name) && yi.equals(cg.inputB.name))
                            || (xi.equals(cg.inputB.name) && yi.equals(cg.inputA.name)))
                    .collect(Collectors.toList());
            if (firstAdder.size() != 2) {
                // WRONG!
                System.out.println("Issue with first adder at index " + i);
                wrong.addAll(firstAdder);
                continue;
            }
            if (firstAdder.stream().noneMatch(c -> c.operation == Operation.XOR)) {
                //WRONG
                System.out.println("Issue with first adder at index " + i);
                wrong.addAll(firstAdder);
                continue;
            }
            if (firstAdder.stream().noneMatch(c -> c.operation == Operation.AND)) {
                //WRONG
                System.out.println("Issue with first adder at index " + i);
                wrong.addAll(firstAdder);
                continue;
            }
            CalcGate firstXor = firstAdder.stream().filter(c -> c.operation == Operation.XOR).findFirst().orElse(null);
            CalcGate firstAnd = firstAdder.stream().filter(c -> c.operation == Operation.AND).findFirst().orElse(null);

            if (ci != null) {
                String carry = ci;
                CalcGate c = calcGates.stream().filter(cg -> carry.equals(cg.name)).findFirst().orElse(null);
                List<CalcGate> secondAdder = calcGates.stream()
                        .filter(cg -> carry.equals(cg.inputA.name) && firstXor.name.equals(cg.inputB.name)
                                || carry.equals(cg.inputB.name) && firstXor.name.equals(cg.inputA.name))
                        .collect(Collectors.toList());
                if(carryWrong) {
                    secondAdder = calcGates.stream()
                            .filter(cg -> firstXor.name.equals(cg.inputB.name)
                                    || firstXor.name.equals(cg.inputA.name)
                            && cg.operation == Operation.AND
                            && cg.operation == Operation.XOR)
                            .collect(Collectors.toList());
                }
                if(carryWrong && !(secondAdder.size() == 1||secondAdder.size() == 2)) {
                    // WRONG!
                    System.out.println("Issue with second SIZE adder at index " + i);
                    wrong.add(firstAnd);
                    carryWrong = true;
                    continue;
                } else if (!carryWrong && secondAdder.size() != 2) {
                    // WRONG!
                    System.out.println("Issue with second SIZE wrong carry adder at index " + i);
                    wrong.add(firstAnd);
                    carryWrong = true;
                    continue;
                }
                if (!carryWrong && secondAdder.stream().noneMatch(cg -> cg.operation == Operation.XOR)) {
                    //WRONG
                    System.out.println("Issue with second XOR adder at index " + i);
                    wrong.addAll(secondAdder);
                    carryWrong = true;
                    continue;
                }
                if (secondAdder.stream().noneMatch(cg -> cg.operation == Operation.AND)) {
                    //WRONG
                    System.out.println("Issue with second AND adder at index " + i);
                    carryWrong = true;
                    wrong.addAll(secondAdder);
                    continue;
                }
                CalcGate secondXor = secondAdder.stream().filter(cg -> cg.operation == Operation.XOR).findFirst().orElse(null);
                if(!carryWrong && secondXor == null) {
                    //WRONG
                    System.out.println("Issue with second XOR at index " + i);
                    carryWrong = true;
                    continue;
                }
                if(!carryWrong && !zi.equals(secondXor.name)) {
                    wrong.add(secondXor);
                    wrong.add(calcGates.stream().filter(cg -> zi.equals(cg.name)).findFirst().orElse(null));
                    System.out.println("Issue with result at index " + i);
                    carryWrong = true;
                    continue;
                }
                CalcGate secondAnd = secondAdder.stream().filter(cg -> cg.operation == Operation.AND).findFirst().orElse(null);
                List<CalcGate> cout = calcGates.stream()
                        .filter(cg -> cg.inputA.name.equals(secondAnd.name) && cg.inputB.name.equals(firstAnd.name)
                                || cg.inputB.name.equals(secondAnd.name) && cg.inputA.name.equals(firstAnd.name))
                        .collect(Collectors.toList());
                if(cout == null || cout.size() != 1) {
                    System.out.println("Issue with SIZE carry at index " + i);
                    carryWrong = true;
                    continue;
                }
                if(cout.get(0).operation != Operation.OR) {
                    wrong.add(cout.get(0));
                    System.out.println("Issue with OR carry at index " + i);
                    carryWrong = true;
                    continue;
                }
                ci = cout.get(0).name;
                carryWrong = false;
            }
            else {
                if(firstXor == null) {
                    System.out.println("Issue with initial adder at index " + i);
                }
                if(!"z00".equals(firstXor.name)) {
                    //WRONG
                    wrong.add(firstXor);
                    System.out.println("Issue with initial adder at index " + i);
                    continue;
                }
                ci = firstAnd.name;
                carryWrong = false;
            }

        }

        System.out.println(wrong.stream()
                .sorted(Comparator.comparing(g -> g.name))
                .map(g -> g.name)
                .collect(Collectors.joining(",")));

    }

    private static List<CalcGate> switchGates(List<CalcGate> gates, List<Pair<CalcGate, CalcGate>> switched) {
        List<CalcGate> newGates = gates.stream().map(CalcGate::clone).collect(Collectors.toList());
        for (int i = 0; i < switched.size(); i++) {
            CalcGate sAI = switched.get(i).getLeft();
            CalcGate sBI = switched.get(i).getRight();
            CalcGate sA = newGates.stream().filter(u -> u.name.equals(sAI.name)).findFirst().orElse(null);
            CalcGate sB = newGates.stream().filter(u -> u.name.equals(sBI.name)).findFirst().orElse(null);
            Gate aIa = sA.inputA;
            Gate aIb = sA.inputB;
            sA.inputA = sB.inputA;
            sA.inputB = sB.inputB;
            sB.inputA = aIa;
            sB.inputB = aIb;
        }

        return newGates;
    }

    private static List<Pair<CalcGate, CalcGate>> findSwitches(List<CalcGate> gates, List<List<Pair<CalcGate, CalcGate>>> triedSwitches) {
        List<Pair<CalcGate, CalcGate>> switches = new ArrayList<>();
        do {
            //choose four switches
            switches = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                Pair<CalcGate, CalcGate> s;
                CalcGate a;
                CalcGate b;
                do {
                    int sI = (int) (Math.random() * gates.size());
                    sI = Math.min(sI, gates.size());
                    a = gates.get(sI);
                    do {
                        sI = (int) (Math.random() * gates.size());
                        sI = Math.min(sI, gates.size());
                        b = gates.get(sI);
                    } while (Objects.equals(a, b));
                } while (switches.contains(Pair.of(a, b)));
                switches.add(Pair.of(a, b));
            }

        } while (triedSwitches.contains(switches));
        return switches;
    }

    public static void main1(String[] args) throws IOException {
        List<String> values = Files.readAllLines(Path.of("src/main/resources/Input24.txt"));

        List<Gate> gates = getGates(values);
        List<CalcGate> unlinkedGates = new ArrayList<>(gates.stream().filter(l -> l instanceof CalcGate).map(l -> (CalcGate) l).collect(Collectors.toList()));
        while (!unlinkedGates.isEmpty()) {
            unlinkedGates.forEach(ulg -> {
                Gate inputA = gates.stream().filter(g -> g.name.equals(ulg.inputAS)).findFirst().orElse(null);
                Gate inputB = gates.stream().filter(g -> g.name.equals(ulg.inputBS)).findFirst().orElse(null);
                ulg.inputA = inputA;
                ulg.inputB = inputB;
            });
            unlinkedGates.removeIf(g -> g.inputA != null && g.inputB != null);
        }
        List<CalcGate> uncalcedGates = gates.stream()
                .filter(g -> g instanceof CalcGate)
                .map(g -> (CalcGate) g)
                .filter(cg -> !cg.valueSet)
                .collect(Collectors.toList());

        while (!uncalcedGates.isEmpty()) {
            for (CalcGate cg : uncalcedGates) {
                calcValue(cg);
            }
            uncalcedGates.removeIf(cg -> cg.valueSet);
        }
        List<Gate> sortedGates = gates.stream()
                .filter(g -> g instanceof CalcGate && g.name.startsWith("z"))
                .sorted(Comparator.comparing(g -> g.name))
                .collect(Collectors.toList());
        List<Boolean> bools = sortedGates.stream()
                .map(g -> g.value)
                .toList();

        long bits = getNumFromBits(bools);
        System.out.println(bits);

    }

    private static long getNumFromBits(List<Boolean> bools) {
        long bits = 0;
        for (int i = 0; i < bools.size(); i++)
            if (bools.get(i))
                bits |= 1L << i;
        return bits;
    }

    private static void calcValue(CalcGate endGate) {
        boolean a = false;
        boolean b = false;
        if (endGate.inputA instanceof InputGate iga) {
            a = iga.value;
        } else {
            calcValue((CalcGate) endGate.inputA);
            a = endGate.inputA.value;
        }
        if (endGate.inputB instanceof InputGate igb) {
            b = igb.value;
        } else {
            calcValue((CalcGate) endGate.inputB);
            b = endGate.inputB.value;
        }
        endGate.value = switch (endGate.operation) {
            case AND -> a & b;
            case OR -> a | b;
            case XOR -> a ^ b;
        };
        endGate.valueSet = true;
    }

    private static List<Gate> getGates(List<String> values) {
        boolean input = true;
        List<Gate> gates = new ArrayList<>();
        for (String s : values) {
            if (StringUtils.isEmpty(s)) {
                input = false;
            } else if (input) {
                String[] inputString = s.split(": ");
                InputGate inputGate = new InputGate();
                inputGate.name = inputString[0];
                inputGate.value = StringUtils.equals(inputString[1], "1");
                gates.add(inputGate);
            } else {
                String[] inputString = s.split(" ");
                CalcGate calcGate = new CalcGate();
                calcGate.name = inputString[4];
                calcGate.inputAS = inputString[0];
                calcGate.operation = Operation.valueOf(inputString[1]);
                calcGate.inputBS = inputString[2];
                calcGate.value = false;
                calcGate.valueSet = false;
                gates.add(calcGate);
            }
        }
        return gates;
    }

}
