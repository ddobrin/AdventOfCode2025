package org.example.cli.puzzle7;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Puzzle7Test {

    @Test
    public void testExample() {
        List<String> input = List.of(
            ".......S.......",
            "...............",
            ".......^.......",
            "...............",
            "......^.^......",
            "...............",
            ".....^.^.^.....",
            "...............",
            "....^.^...^....",
            "...............",
            "...^.^...^.^...",
            "...............",
            "..^...^.....^..",
            "...............",
            ".^.^.^.^.^...^.",
            "..............."
        );
        
        char[][] grid = parseGrid(input);
        
        long result1 = Puzzle7.solvePart1(grid);
        assertEquals(21, result1);
        
        long result2 = Puzzle7.solvePart2(grid);
        assertEquals(40, result2);
    }
    
    private char[][] parseGrid(List<String> lines) {
        int height = lines.size();
        int width = lines.get(0).length();
        char[][] grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }
}
