package org.example.cli.puzzle3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle3Part2 {
    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle3/3.input");
        if (!Files.exists(inputPath)) {
            System.err.println("Input file not found: " + inputPath.toAbsolutePath());
            return;
        }

        List<String> lines = Files.readAllLines(inputPath);
        
        long totalJoltage = 0;
        
        for (String line : lines) {
            String maxJoltageStr = getMaxSubsequence(line, 12);
            if (!maxJoltageStr.isEmpty()) {
                totalJoltage += Long.parseLong(maxJoltageStr);
            }
        }
        
        System.out.println("Total Output Joltage Part 2: " + totalJoltage);
    }
    
    private static String getMaxSubsequence(String line, int length) {
        if (line == null || line.length() < length) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        int currentIdx = -1; // The index of the last selected digit
        
        for (int i = 0; i < length; i++) {
            // Number of digits needed AFTER the current one we are about to pick
            int remainingNeeded = length - 1 - i;
            
            // The range of valid indices for the next digit
            // Start: immediately after the last selected digit
            // End: we must leave enough characters for the remaining needed digits
            int start = currentIdx + 1;
            int end = line.length() - 1 - remainingNeeded;
            
            char maxDigit = '/'; // Ascii smaller than '0'
            int maxDigitIdx = -1;
            
            for (int j = start; j <= end; j++) {
                char c = line.charAt(j);
                if (Character.isDigit(c)) {
                    if (c > maxDigit) {
                        maxDigit = c;
                        maxDigitIdx = j;
                        // Optimization: 9 is the largest possible digit
                        if (maxDigit == '9') {
                            break;
                        }
                    }
                }
            }
            
            if (maxDigitIdx != -1) {
                result.append(maxDigit);
                currentIdx = maxDigitIdx;
            } else {
                // This should not happen given the length check, but good for safety
                break;
            }
        }
        
        return result.toString();
    }
}
