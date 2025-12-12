package org.example.cli.puzzle1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle1 {
    public record Solution(long part1, long part2) {}

    public static void main(String[] args) {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle1/1.input");
        try {
            List<String> lines = Files.readAllLines(inputPath);
            Solution solution = solve(lines);
            System.out.println("Part 1: " + solution.part1());
            System.out.println("Part 2: " + solution.part2());
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    public static Solution solve(List<String> lines) {
        int currentPos = 50;
        long part1Count = 0;
        long part2Count = 0;

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            char dir = line.charAt(0);
            int steps = Integer.parseInt(line.substring(1));

            for (int i = 0; i < steps; i++) {
                if (dir == 'R') {
                    currentPos = (currentPos + 1) % 100;
                } else if (dir == 'L') {
                    currentPos = (currentPos - 1 + 100) % 100;
                }

                // Part 2: Count every time the dial points at 0 during (or at the end of) a rotation
                if (currentPos == 0) {
                    part2Count++;
                }
            }

            // Part 1: Count times the dial is left pointing at 0 AFTER a rotation
            if (currentPos == 0) {
                part1Count++;
            }
        }

        return new Solution(part1Count, part2Count);
    }
}