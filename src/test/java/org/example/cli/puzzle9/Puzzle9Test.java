package org.example.cli.puzzle9;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Puzzle9Test {

    @Test
    void testExample() {
        List<Puzzle9.Point> points = List.of(
            new Puzzle9.Point(7, 1),
            new Puzzle9.Point(11, 1),
            new Puzzle9.Point(11, 7),
            new Puzzle9.Point(9, 7),
            new Puzzle9.Point(9, 5),
            new Puzzle9.Point(2, 5),
            new Puzzle9.Point(2, 3),
            new Puzzle9.Point(7, 3)
        );

        long part1 = Puzzle9.solvePart1(points);
        assertEquals(50, part1, "Part 1 failed");

        long part2 = Puzzle9.solvePart2(points);
        assertEquals(24, part2, "Part 2 failed");
    }
}