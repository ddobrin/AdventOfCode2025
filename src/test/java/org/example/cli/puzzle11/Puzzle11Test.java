package org.example.cli.puzzle11;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Puzzle11Test {

    @Test
    void testExampleCase() {
        List<String> input = Arrays.asList(
            "aaa: you hhh",
            "you: bbb ccc",
            "bbb: ddd eee",
            "ccc: ddd eee fff",
            "ddd: ggg",
            "eee: out",
            "fff: out",
            "ggg: out",
            "hhh: ccc fff iii",
            "iii: out"
        );
        
        long result = Puzzle11.solve(input);
        
        assertEquals(5, result, "The number of paths should be 5.");
    }
}
