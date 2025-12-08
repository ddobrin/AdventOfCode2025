package org.example.cli.puzzle6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Puzzle6 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle6/6.input");
        List<String> lines = Files.readAllLines(inputPath);

        long[] results = solve(lines);
        System.out.println("Part 1: " + results[0]);
        System.out.println("Part 2: " + results[1]);
    }

    public static long[] solve(List<String> inputLines) {
        List<String> lines = new ArrayList<>(inputLines);
        // Remove trailing empty lines if any
        while (!lines.isEmpty() && lines.get(lines.size() - 1).trim().isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        if (lines.isEmpty()) {
            return new long[]{0, 0};
        }

        // Pad lines to max width
        int maxWidth = lines.stream().mapToInt(String::length).max().orElse(0);
        List<String> paddedLines = lines.stream()
                .map(s -> String.format("%-" + maxWidth + "s", s))
                .collect(Collectors.toList());

        // Identify separator columns
        // A separator column is one where ALL lines (including the operator line at the bottom) have a space.
        List<Integer> separatorIndices = new ArrayList<>();
        separatorIndices.add(-1); // Start before the first column

        for (int col = 0; col < maxWidth; col++) {
            boolean isSeparator = true;
            for (String line : paddedLines) {
                if (col < line.length() && line.charAt(col) != ' ') {
                    isSeparator = false;
                    break;
                }
            }
            if (isSeparator) {
                separatorIndices.add(col);
            }
        }
        separatorIndices.add(maxWidth); // End after the last column

        List<Block> blocks = new ArrayList<>();
        // Extract blocks based on separators
        // We look for consecutive ranges between separators that have content.
        for (int i = 0; i < separatorIndices.size() - 1; i++) {
            int startCol = separatorIndices.get(i) + 1;
            int endCol = separatorIndices.get(i + 1);

            if (startCol < endCol) {
                // Check if this range is actually empty (consecutive separators)
                // If we implemented separators correctly, adjacent indices in separatorIndices list
                // might correspond to adjacent columns if there are multiple spaces.
                // We only care if there is content in between.
                
                // Extract the block content
                List<String> blockLines = new ArrayList<>();
                boolean hasContent = false;
                for (String line : paddedLines) {
                    String sub = line.substring(startCol, endCol);
                    blockLines.add(sub);
                    if (!sub.trim().isEmpty()) {
                        hasContent = true;
                    }
                }
                
                if (hasContent) {
                    blocks.add(new Block(blockLines));
                }
            }
        }

        long part1Total = 0;
        long part2Total = 0;

        for (Block block : blocks) {
            part1Total += block.solvePart1();
            part2Total += block.solvePart2();
        }

        return new long[]{part1Total, part2Total};
    }

    static class Block {
        List<String> lines;
        char operator;
        List<String> numberLines;

        public Block(List<String> lines) {
            this.lines = lines;
            // The last line contains the operator
            // We need to find the non-empty character in the last line of the block
            // However, the "block" extracted might contain trailing spaces in lines.
            // The operator is in the last row of the input grid.
            String lastLine = lines.get(lines.size() - 1);
            if (lastLine.contains("*")) {
                this.operator = '*';
            } else if (lastLine.contains("+")) {
                this.operator = '+';
            } else {
                throw new IllegalStateException("No operator found in block: " + lastLine);
            }
            
            this.numberLines = lines.subList(0, lines.size() - 1);
        }

        public long solvePart1() {
            List<Long> numbers = new ArrayList<>();
            Pattern p = Pattern.compile("\\d+");
            for (String line : numberLines) {
                Matcher m = p.matcher(line);
                while (m.find()) {
                    numbers.add(Long.parseLong(m.group()));
                }
            }
            return calculate(numbers, operator);
        }

        public long solvePart2() {
            List<Long> numbers = new ArrayList<>();
            // Iterate columns Right to Left
            int width = numberLines.get(0).length();
            
            for (int col = width - 1; col >= 0; col--) {
                StringBuilder sb = new StringBuilder();
                for (String line : numberLines) {
                    if (col < line.length()) {
                        char c = line.charAt(col);
                        if (Character.isDigit(c)) {
                            sb.append(c);
                        }
                    }
                }
                if (sb.length() > 0) {
                    numbers.add(Long.parseLong(sb.toString()));
                }
            }
            return calculate(numbers, operator);
        }

        private long calculate(List<Long> numbers, char op) {
            if (numbers.isEmpty()) return 0;
            long result = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++) {
                if (op == '+') {
                    result += numbers.get(i);
                } else if (op == '*') {
                    result *= numbers.get(i);
                }
            }
            return result;
        }
    }
}