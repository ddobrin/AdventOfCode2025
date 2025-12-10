package org.example.cli.puzzle10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Puzzle10 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle10/10.input");
        List<String> lines = Files.readAllLines(inputPath);

        long totalPresses = 0;

        for (String line : lines) {
            if (line.isBlank()) continue;
            totalPresses += solveMachine(line);
        }

        System.out.println("Total presses: " + totalPresses);
    }

    private static int solveMachine(String line) {
        // Parse target: [...]
        Pattern targetPattern = Pattern.compile("\\[([.#]+)\\]");
        Matcher targetMatcher = targetPattern.matcher(line);
        if (!targetMatcher.find()) {
            throw new IllegalArgumentException("Invalid line format (target): " + line);
        }
        String targetStr = targetMatcher.group(1);
        long targetMask = parseLights(targetStr);
        int numLights = targetStr.length();

        // Parse buttons: (...)
        // We can just find all occurrences of (...)
        Pattern buttonPattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher buttonMatcher = buttonPattern.matcher(line);
        
        List<Long> buttons = new ArrayList<>();
        while (buttonMatcher.find()) {
            String content = buttonMatcher.group(1);
            long buttonMask = parseButton(content);
            buttons.add(buttonMask);
        }

        int nButtons = buttons.size();
        int minPresses = Integer.MAX_VALUE;

        // Brute force all subsets of buttons
        // 2^nButtons iterations
        int limit = 1 << nButtons;
        for (int i = 0; i < limit; i++) {
            long currentMask = 0;
            int presses = 0;

            for (int j = 0; j < nButtons; j++) {
                if ((i >> j & 1) == 1) {
                    currentMask ^= buttons.get(j);
                    presses++;
                }
            }

            // We only care about the bits corresponding to the lights
            // Although with correct parsing, higher bits should be 0 anyway.
            if (currentMask == targetMask) {
                if (presses < minPresses) {
                    minPresses = presses;
                }
            }
        }
        
        if (minPresses == Integer.MAX_VALUE) {
             System.err.println("No solution found for line: " + line);
             return 0; // Or handle as error
        }

        return minPresses;
    }

    private static long parseLights(String s) {
        long mask = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '#') {
                mask |= (1L << i);
            }
        }
        return mask;
    }

    private static long parseButton(String content) {
        if (content.isBlank()) return 0;
        long mask = 0;
        String[] parts = content.split(",");
        for (String part : parts) {
            int index = Integer.parseInt(part.trim());
            mask |= (1L << index);
        }
        return mask;
    }
}
