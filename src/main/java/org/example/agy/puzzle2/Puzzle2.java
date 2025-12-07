package org.example.agy.puzzle2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Puzzle2 {

    public static void main(String[] args) {
        try {
            Path inputPath = Paths.get("src/main/java/org/example/agy/puzzle2/2.input");
            if (!Files.exists(inputPath)) {
                System.err.println("Input file not found at: " + inputPath.toAbsolutePath());
                return;
            }

            String content = Files.readString(inputPath).trim();
            long totalSum = solve(content);
            System.out.println("Total sum of invalid IDs: " + totalSum);

        } catch (IOException e) {
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
            if (isInvalid(i)) {
                sum += i;
            }
        }
        return sum;
    }

    private static boolean isInvalid(long id) {
        String s = String.valueOf(id);
        int len = s.length();

        // Try all possible pattern lengths
        // Pattern length must be at most len / 2 to repeat at least twice
        for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
            if (len % patternLen == 0) {
                String pattern = s.substring(0, patternLen);
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
