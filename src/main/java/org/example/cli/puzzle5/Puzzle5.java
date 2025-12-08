package org.example.cli.puzzle5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Puzzle5 {

    record Range(long start, long end) {
        boolean contains(long value) {
            return value >= start && value <= end;
        }
    }

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle5/5.input");
        if (!Files.exists(inputPath)) {
            System.err.println("Input file not found: " + inputPath.toAbsolutePath());
            return;
        }

        List<String> lines = Files.readAllLines(inputPath);
        List<Range> ranges = new ArrayList<>();
        List<Long> availableIngredients = new ArrayList<>();

        boolean parsingRanges = true;

        for (String line : lines) {
            if (line.isBlank()) {
                parsingRanges = false;
                continue;
            }

            if (parsingRanges) {
                String[] parts = line.split("-");
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                ranges.add(new Range(start, end));
            } else {
                availableIngredients.add(Long.parseLong(line.trim()));
            }
        }

        long freshCount = 0;
        for (Long ingredientId : availableIngredients) {
            boolean isFresh = false;
            for (Range range : ranges) {
                if (range.contains(ingredientId)) {
                    isFresh = true;
                    break;
                }
            }
            if (isFresh) {
                freshCount++;
            }
        }

        System.out.println("Number of fresh ingredients: " + freshCount);
    }
}
