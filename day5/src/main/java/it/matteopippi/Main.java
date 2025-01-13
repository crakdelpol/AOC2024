package it.matteopippi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        Class<Main> clazz = Main.class;
        try (
                InputStream inputStream = clazz.getResourceAsStream("/input.txt")
                ){
            String data = readFromInputStream(inputStream);
            String[] split = data.split("\n");
            boolean rulesEnd = false;
            List<Rule> rules = new ArrayList<>();
            List<Pages> pagesToUpdate = new ArrayList<>();
            for (String line : split) {

                if(line.isEmpty()) {
                    rulesEnd = true;
                    continue;
                }

                if(!rulesEnd){
                    String[] singleRule = line.split("\\|");
                    rules.add(new Rule(Integer.valueOf(singleRule[0]), Integer.valueOf(singleRule[1])));
                } else {
                    List<Integer> collect = Arrays.stream(line.split(",")).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
                    pagesToUpdate.add(new Pages(collect));
                }
            }

            int total = 0;

            for (Pages pageToUpdateN : pagesToUpdate) {
                boolean validPage = true;
                List<Rule> validRules = new ArrayList<>();
                for (Rule rule : rules) {
                    if(new HashSet<>(pageToUpdateN.pages).containsAll(Arrays.asList(rule.firstNumber, rule.secondNumber))){
                        validRules.add(rule);
                    }
                }
                for (int i = 0; i < pageToUpdateN.pages.size(); i++) {

                    int finalI = i;
                    List<Integer> numberToBeAfter = validRules
                            .stream()
                            .filter(rule -> Objects.equals(rule.firstNumber, pageToUpdateN.pages.get(finalI)))
                            .map(rule -> rule.secondNumber)
                            .collect(Collectors.toList());

                    List<Integer> numberToBeBefore = validRules
                            .stream()
                            .filter(rule -> Objects.equals(rule.secondNumber, pageToUpdateN.pages.get(finalI)))
                            .map(rule -> rule.firstNumber)
                            .collect(Collectors.toList());

                    List<Integer> numbersBefore = pageToUpdateN.pages.subList(0, finalI + 1);
                    List<Integer> numbersAfter = pageToUpdateN.pages.subList(finalI + 1, pageToUpdateN.pages.size());

                    if(!(new HashSet<>(numbersBefore).containsAll(numberToBeBefore) && new HashSet<>(numbersAfter).containsAll(numberToBeAfter))){
                        validPage = false;
                    }
                }
                if(validPage){
                    Integer numberToAdd = pageToUpdateN.pages.get(pageToUpdateN.pages.size() / 2 );
                    total += numberToAdd;
                }
            }
            System.out.println(total);
            System.out.println("Execution time: " + ( System.currentTimeMillis() - start) + "ms");
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

    private static class Rule {

        public Rule(Integer firstNumber, Integer secondNumber) {
            this.firstNumber = firstNumber;
            this.secondNumber = secondNumber;
        }

        Integer firstNumber;
        Integer secondNumber;

    }

    private static class Pages {
        List<Integer> pages;

        public Pages(List<Integer> pages) {
            this.pages = pages;
        }
    }
}