package org.example.cli.puzzle4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Puzzle4 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle4/4.input");
        if (!Files.exists(inputPath)) {
            System.err.println("Input file not found at: " + inputPath.toAbsolutePath());
            return;
        }

        List<String> lines = Files.readAllLines(inputPath);

        if (lines.isEmpty()) {
            System.out.println("Input file is empty.");
            return;
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        long totalRemoved = 0;
        long part1Removed = 0;
        int round = 0;

        while (true) {
            List<int[]> toRemove = new ArrayList<>();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == '@') {
                        if (isAccessible(grid, r, c, rows, cols)) {
                            toRemove.add(new int[]{r, c});
                        }
                    }
                }
            }

            if (toRemove.isEmpty()) {
                break;
            }

            if (round == 0) {
                part1Removed = toRemove.size();
            }

            totalRemoved += toRemove.size();

            for (int[] p : toRemove) {
                grid[p[0]][p[1]] = '.';
            }
            
            round++;
        }

        System.out.println("Part 1: Accessible rolls (initial): " + part1Removed);
        System.out.println("Part 2: Total removed rolls: " + totalRemoved);
    }

    private static boolean isAccessible(char[][] grid, int r, int c, int rows, int cols) {
        int neighborCount = 0;
        // 8 directions
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int nr = r + dr[i];
            int nc = c + dc[i];

            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                if (grid[nr][nc] == '@') {
                    neighborCount++;
                }
            }
        }

        return neighborCount < 4;
    }
}