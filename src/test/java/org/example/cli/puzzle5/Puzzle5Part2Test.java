package org.example.cli.puzzle5;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Puzzle5Part2Test {

    @Test
    void testExampleCase() {
        List<Puzzle5Part2.Range> ranges = new ArrayList<>();
        ranges.add(new Puzzle5Part2.Range(3, 5));
        ranges.add(new Puzzle5Part2.Range(10, 14));
        ranges.add(new Puzzle5Part2.Range(16, 20));
        ranges.add(new Puzzle5Part2.Range(12, 18));

        long result = Puzzle5Part2.calculateFreshIngredients(ranges);
        assertEquals(14, result);
    }
}
