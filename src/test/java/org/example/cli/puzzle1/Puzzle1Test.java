package org.example.cli.puzzle1;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Puzzle1Test {

    @Test
    void testExample() {
        List<String> input = List.of(
            "L68",
            "L30",
            "R48",
            "L5",
            "R60",
            "L55",
            "L1",
            "L99",
            "R14",
            "L82"
        );

        Puzzle1.Solution solution = Puzzle1.solve(input);

        assertEquals(3, solution.part1(), "Part 1 example result should be 3");
        assertEquals(6, solution.part2(), "Part 2 example result should be 6");
    }
}
