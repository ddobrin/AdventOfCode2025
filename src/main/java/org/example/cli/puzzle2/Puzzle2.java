package org.example.cli.puzzle2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Puzzle2 {

    public static void main(String[] args) {
        Path inputPath;
        if (args.length > 0) {
            inputPath = Path.of(args[0]);
        } else {
            inputPath = Path.of("src/main/java/org/example/cli/puzzle2/2.input");
        }
        try {
            if (!Files.exists(inputPath)) {
                System.err.println("Input file not found at: " + inputPath.toAbsolutePath());
                return;
            }
            String content = Files.readString(inputPath).trim();
            // Handle potential multiline wrapping if any, though the puzzle says single line.
            // Removing newlines just in case.
            content = content.replace("\n", "").replace("\r", "");
            
            long totalSum = solve(content);
            System.out.println("Total sum of invalid IDs (Part 2): " + totalSum);
            
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static long solve(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .mapToLong(Puzzle2::processRange)
                .sum();
    }

    private static long processRange(String range) {
        String[] parts = range.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format: " + range);
        }
        
        long start = Long.parseLong(parts[0]);
        long end = Long.parseLong(parts[1]);
        
        long sum = 0;
        for (long i = start; i <= end; i++) {
            if (isInvalidPart2(i)) {
                sum += i;
            }
        }
        return sum;
    }

    /**
     * Checks if the ID is invalid according to Part 2 rules.
     * An ID is invalid if it is made only of some sequence of digits repeated at least twice.
     */
    private static boolean isInvalidPart2(long id) {
        String s = String.valueOf(id);
        int len = s.length();
        
        // Try all possible lengths for the repeated pattern
        // The pattern length must be at most half the string length to be repeated at least twice.
        for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
            if (len % patternLen == 0) {
                String pattern = s.substring(0, patternLen);
                
                // Check if the string consists of this pattern repeated
                boolean matches = true;
                for (int k = patternLen; k < len; k += patternLen) {
                    if (!s.substring(k, k + patternLen).equals(pattern)) {
                        matches = false;
                        break;
                    }
                }
                
                if (matches) {
                    return true;
                }
            }
        }
        return false;
    }
}