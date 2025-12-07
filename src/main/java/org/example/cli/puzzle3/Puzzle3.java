package org.example.cli.puzzle3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Puzzle3 {
    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle3/3.input");
        if (!Files.exists(inputPath)) {
            System.err.println("Input file not found: " + inputPath.toAbsolutePath());
            return;
        }

        List<String> lines = Files.readAllLines(inputPath);
        
        long totalJoltage = 0;
        
        for (String line : lines) {
            totalJoltage += getMaxJoltage(line);
        }
        
        System.out.println("Total Output Joltage: " + totalJoltage);
    }
    
    private static int getMaxJoltage(String line) {
        if (line == null || line.length() < 2) {
            return 0;
        }
        
        int max = 0;
        // Iterate through all pairs (i, j) where i < j
        for (int i = 0; i < line.length() - 1; i++) {
            for (int j = i + 1; j < line.length(); j++) {
                char c1 = line.charAt(i);
                char c2 = line.charAt(j);
                
                if (Character.isDigit(c1) && Character.isDigit(c2)) {
                    int digit1 = Character.getNumericValue(c1);
                    int digit2 = Character.getNumericValue(c2);
                    
                    int currentJoltage = digit1 * 10 + digit2;
                    if (currentJoltage > max) {
                        max = currentJoltage;
                    }
                }
            }
        }
        return max;
    }
}
