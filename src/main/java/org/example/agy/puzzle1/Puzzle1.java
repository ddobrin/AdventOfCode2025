package org.example.agy.puzzle1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Puzzle1 {

    public static void main(String[] args) {
        try {
            // Read input file
            Path inputPath = Paths.get("src/main/java/org/example/agy/puzzle1/1.input");
            if (!Files.exists(inputPath)) {
                System.err.println("Input file not found at: " + inputPath.toAbsolutePath());
                return;
            }

            List<String> lines = Files.readAllLines(inputPath);
            int currentPosition = 50;
            int zeroCount = 0;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                char direction = line.charAt(0);
                int amount = Integer.parseInt(line.substring(1).trim());

                if (direction == 'R') {
                    currentPosition = (currentPosition + amount) % 100;
                } else if (direction == 'L') {
                    currentPosition = (currentPosition - amount) % 100;
                    if (currentPosition < 0) {
                        currentPosition += 100;
                    }
                } else {
                    System.err.println("Unknown direction: " + direction);
                    continue;
                }

                if (currentPosition == 0) {
                    zeroCount++;
                }
            }

            System.out.println("Password: " + zeroCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
