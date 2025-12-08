package org.example.cli.puzzle7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Puzzle7 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle7/7.input");
        if (!Files.exists(inputPath)) {
            // Fallback if running from a different working directory
             inputPath = Path.of("src/main/java/org/example/cli/puzzle7/7.input");
        }
        
        List<String> lines = Files.readAllLines(inputPath);
        char[][] grid = parseGrid(lines);

        long part1 = solvePart1(grid);
        System.out.println("Part 1: " + part1);

        long part2 = solvePart2(grid);
        System.out.println("Part 2: " + part2);
    }

    private static char[][] parseGrid(List<String> lines) {
        if (lines.isEmpty()) return new char[0][0];
        int height = lines.size();
        int width = lines.get(0).length();
        char[][] grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    public static long solvePart1(char[][] grid) {
        if (grid.length == 0) return 0;
        int height = grid.length;
        int width = grid[0].length;

        // Find S
        int startRow = -1;
        int startCol = -1;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[r][c] == 'S') {
                    startRow = r;
                    startCol = c;
                    break;
                }
            }
            if (startRow != -1) break;
        }

        if (startRow == -1) {
             // If no S found, return 0 or throw? Assuming valid input.
             return 0;
        }

        Set<Integer> activeCols = new HashSet<>();
        activeCols.add(startCol);

        long splitCount = 0;

        // Simulate row by row
        for (int r = startRow + 1; r < height; r++) {
            Set<Integer> nextRowCols = new HashSet<>();
            
            for (int col : activeCols) {
                // Determine content at current beam position
                char c = grid[r][col];
                
                if (c == '^') {
                    splitCount++;
                    // Split into left and right
                    // Check bounds for new beams
                    if (col - 1 >= 0) {
                        nextRowCols.add(col - 1);
                    }
                    if (col + 1 < width) {
                        nextRowCols.add(col + 1);
                    }
                } else {
                    // Continues straight
                    nextRowCols.add(col);
                }
            }
            activeCols = nextRowCols;
            if (activeCols.isEmpty()) break;
        }

        return splitCount;
    }

    public static long solvePart2(char[][] grid) {
        if (grid.length == 0) return 0;
        int height = grid.length;
        int width = grid[0].length;

        // Find S
        int startRow = -1;
        int startCol = -1;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (grid[r][c] == 'S') {
                    startRow = r;
                    startCol = c;
                    break;
                }
            }
            if (startRow != -1) break;
        }
        
        if (startRow == -1) return 0;

        // Map of column index -> count of timelines
        Map<Integer, Long> activeCols = new HashMap<>();
        activeCols.put(startCol, 1L);

        long completedTimelines = 0;

        for (int r = startRow + 1; r < height; r++) {
            Map<Integer, Long> nextRowCols = new HashMap<>();
            
            for (Map.Entry<Integer, Long> entry : activeCols.entrySet()) {
                int col = entry.getKey();
                long count = entry.getValue();
                
                char c = grid[r][col];
                
                if (c == '^') {
                    // Split left
                    if (col - 1 >= 0) {
                        nextRowCols.merge(col - 1, count, Long::sum);
                    } else {
                        completedTimelines += count;
                    }
                    
                    // Split right
                    if (col + 1 < width) {
                        nextRowCols.merge(col + 1, count, Long::sum);
                    } else {
                        completedTimelines += count;
                    }
                } else {
                    // Straight
                    nextRowCols.merge(col, count, Long::sum);
                }
            }
            activeCols = nextRowCols;
        }

        // All remaining active beams exit the bottom
        for (long count : activeCols.values()) {
            completedTimelines += count;
        }

        return completedTimelines;
    }
}
