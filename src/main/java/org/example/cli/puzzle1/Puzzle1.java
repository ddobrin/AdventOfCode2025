package org.example.cli.puzzle1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Puzzle1 {

    public static void main(String[] args) {
        Path inputPath = Paths.get("src/main/java/org/example/puzzle1/1.input");
        try {
            long result = solve(inputPath);
            System.out.println("Password: " + result);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static long solve(Path inputPath) throws IOException {
        int currentPosition = 50;
        long zeroCount = 0;

        try (Stream<String> lines = Files.lines(inputPath)) {
            for (String line : (Iterable<String>) lines::iterator) {
                if (line.isBlank()) {
                    continue;
                }

                char direction = line.charAt(0);
                int amount = Integer.parseInt(line.substring(1));

                if (direction == 'R') {
                    currentPosition = (currentPosition + amount) % 100;
                } else if (direction == 'L') {
                    currentPosition = (currentPosition - amount) % 100;
                    if (currentPosition < 0) {
                        currentPosition += 100;
                    }
                } else {
                    throw new IllegalArgumentException("Unknown direction: " + direction);
                }

                if (currentPosition == 0) {
                    zeroCount++;
                }
            }
        }
        return zeroCount;
    }
}
