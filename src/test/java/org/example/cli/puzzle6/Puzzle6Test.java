package org.example.cli.puzzle6;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Puzzle6Test {

    @Test
    public void testExample() {
        // The example from the problem description
        // Note: The indentation matters (spaces). I am trying to replicate the visual grid.
        List<String> input = List.of(
            "123 328  51 64 ",
            " 45 64  387 23 ",
            "  6 98  215 314",
            "*   +   *   +  "
        );
        
        long[] results = Puzzle6.solve(input);
        
        // Part 1: 4277556
        assertEquals(4277556L, results[0], "Part 1 failed");
        
        // Part 2: 3263827
        assertEquals(3263827L, results[1], "Part 2 failed");
    }
}