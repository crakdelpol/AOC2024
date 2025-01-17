package it.matteopippi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static Integer path = 1;

    public static void main(String[] args) throws IOException {

        String input = readInstructions();

        String[][] matrix = extractMatrixFromInput(input);

        AbstractMap.SimpleEntry<Integer, Integer> startingPoint = extractStartingPoint(matrix);

        assert startingPoint != null;
        matrix[startingPoint.getKey()][startingPoint.getValue()] = ".";

        while (true) {
            try {
                move(startingPoint.getKey(), startingPoint.getValue(), new Direction("UP"), matrix);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        System.out.println(path);
    }

    private static void move(Integer currentXCord, Integer currentYCord, Direction direction, String[][] matrix) {

        AbstractMap.SimpleEntry<Integer, Integer> nextCoords = direction.retrieveNewCoords();
        int newXCoord = currentXCord + nextCoords.getKey();
        int newYCoord = currentYCord + nextCoords.getValue();
        String valueInNextCell = matrix[newXCoord][newYCoord];

        if (valueInNextCell != null) {
            if (valueInNextCell.equalsIgnoreCase("X")) {
                move(newXCoord, newYCoord, direction, matrix);
            }
            if (valueInNextCell.equalsIgnoreCase(".")) {
                path++;
                matrix[newXCoord][newYCoord] = "X";
                move(newXCoord, newYCoord, direction, matrix);
            }
            if (valueInNextCell.equalsIgnoreCase("#")) {
                move(currentXCord, currentYCord, direction.nextDirection(), matrix);
            }
        }
    }

    public static class Direction {
        String direction;

        public Direction(String direction) {
            this.direction = direction;
        }

        public Direction nextDirection() {
            switch (direction) {
                case "UP":
                    return new Direction("RIGHT");
                case "RIGHT":
                    return new Direction("DOWN");
                case "DOWN":
                    return new Direction("LEFT");
                case "LEFT":
                    return new Direction("UP");
            }

            throw new RuntimeException("Invalid direction");
        }

        public AbstractMap.SimpleEntry<Integer, Integer> retrieveNewCoords() {

            switch (direction) {
                case "UP":
                    return new AbstractMap.SimpleEntry<>(-1, 0);
                case "RIGHT":
                    return new AbstractMap.SimpleEntry<>(0, 1);
                case "DOWN":
                    return new AbstractMap.SimpleEntry<>(1, 0);
                case "LEFT":
                    return new AbstractMap.SimpleEntry<>(0, -1);
            }
            throw new RuntimeException("Invalid direction");
        }

        @Override
        public String toString() {
            return direction;
        }
    }

    private static AbstractMap.SimpleEntry<Integer, Integer> extractStartingPoint(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            String[] strings = matrix[i];
            for (int j = 0; j < strings.length; j++) {
                if (matrix[i][j].equalsIgnoreCase("^")) {
                    return new AbstractMap.SimpleEntry<>(i, j);
                }
            }
        }
        return null;
    }

    private static String[][] extractMatrixFromInput(String input) {
        String[] split = input.split("\n");
        String[][] matrix = new String[split.length][split[0].split("").length];
        for (int i = 0; i < split.length; i++) {
            char[] charArray = split[i].toCharArray();
            String[] stringArray = new String[charArray.length];
            for (int y = 0; y < charArray.length; y++) {
                stringArray[y] = String.valueOf(charArray[y]);
            }

            matrix[i] = stringArray;
        }
        return matrix;
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