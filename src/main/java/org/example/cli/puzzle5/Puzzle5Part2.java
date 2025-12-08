package org.example.cli.puzzle5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Puzzle5Part2 {

    record Range(long start, long end) implements Comparable<Range> {
        @Override
        public int compareTo(Range other) {
            return Long.compare(this.start, other.start);
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

        for (String line : lines) {
            if (line.isBlank()) {
                // For Part 2, we stop reading after the blank line as available IDs are irrelevant
                break;
            }

            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            ranges.add(new Range(start, end));
        }

        long totalFreshCount = calculateFreshIngredients(ranges);

        System.out.println("Total fresh ingredients count: " + totalFreshCount);
    }

    public static long calculateFreshIngredients(List<Range> ranges) {
        if (ranges.isEmpty()) {
            return 0;
        }

        // Sort ranges by start time
        // Note: We copy the list to avoid modifying the original list if that matters, 
        // but here modifying the passed list is acceptable or we can assume the caller passes a mutable list.
        // To be safe and clean, let's sort the passed list.
        ranges.sort(Comparator.naturalOrder());

        List<Range> mergedRanges = new ArrayList<>();
        Range current = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            Range next = ranges.get(i);

            // Check if ranges overlap or are adjacent
            if (next.start <= current.end + 1) {
                // Merge them
                current = new Range(current.start, Math.max(current.end, next.end));
            } else {
                mergedRanges.add(current);
                current = next;
            }
        }
        mergedRanges.add(current);

        long totalFreshCount = 0;
        for (Range range : mergedRanges) {
            totalFreshCount += (range.end - range.start + 1);
        }
        return totalFreshCount;
    }
}
