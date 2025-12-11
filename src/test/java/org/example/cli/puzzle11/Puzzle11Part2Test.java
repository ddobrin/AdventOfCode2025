package org.example.cli.puzzle11;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Puzzle11Part2Test {

    @Test
    void testExampleCase() {
        List<String> input = Arrays.asList(
            "svr: aaa bbb",
            "aaa: fft",
            "fft: ccc",
            "bbb: tty",
            "tty: ccc",
            "ccc: ddd eee",
            "ddd: hub",
            "hub: fff",
            "eee: dac",
            "dac: fff",
            "fff: ggg hhh",
            "ggg: out",
            "hhh: out"
        );
        
        long result = Puzzle11Part2.solve(input);
        
        assertEquals(2, result, "The number of paths visiting both dac and fft should be 2.");
    }
}
