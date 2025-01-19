package it.matteopippi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {

        String genericInput = readInstructions();
        String[] lines = genericInput.split("\\n");
        Long result = 0L;
        for (String line : lines) {
            long expectedResult = Long.parseLong(line.substring(0, line.indexOf(":")));
            List<Long> numbers = Arrays.stream(line.substring(line.indexOf(":") + 2).split(" ")).map(Long::valueOf).collect(Collectors.toList());

            Set<Long> innerResults = new HashSet<>();
            generateAllPossibleResults(numbers, 0, numbers.get(0), innerResults);
            if (innerResults.contains(expectedResult)) {
                System.out.println("Found match i will add " + expectedResult);
                result += expectedResult;
            }
        }
        System.out.println(result);
    }

    private static void generateAllPossibleResults(List<Long> numbers, int index, Long currentValue, Set<Long> results) {
        if (index == numbers.size() - 1) {
            results.add(currentValue);
            return;
        }

        Long nextNumber = numbers.get(index + 1);
        generateAllPossibleResults(numbers, index + 1, currentValue + nextNumber, results);
        generateAllPossibleResults(numbers, index + 1, currentValue * nextNumber, results);
    }


    private static String readInstructions() throws IOException {
        Class<Main> clazz = Main.class;
        try (
                InputStream inputStream = clazz.getResourceAsStream("/input.txt")
        ) {
            return readFromInputStream(inputStream);
        }
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}