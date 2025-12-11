package org.example.cli.puzzle11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Puzzle11 {
    
    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle11/11.input");
        List<String> lines = Files.readAllLines(inputPath);
        
        long pathCount = solve(lines);
        System.out.println("Total paths from you to out: " + pathCount);
    }

    public static long solve(List<String> lines) {
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Long> memo = new HashMap<>();

        for (String line : lines) {
            parseLine(line, graph);
        }

        return countPaths("you", graph, memo);
    }
    
    private static void parseLine(String line, Map<String, List<String>> graph) {
        if (line.isBlank()) return;
        String[] parts = line.split(": ");
        String source = parts[0];
        if (parts.length > 1) {
            String[] dests = parts[1].split(" ");
            graph.put(source, Arrays.asList(dests));
        } else {
            graph.put(source, Collections.emptyList());
        }
    }
    
    private static long countPaths(String current, Map<String, List<String>> graph, Map<String, Long> memo) {
        if ("out".equals(current)) {
            return 1;
        }
        if (memo.containsKey(current)) {
            return memo.get(current);
        }
        
        long count = 0;
        List<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                count += countPaths(neighbor, graph, memo);
            }
        }
        
        memo.put(current, count);
        return count;
    }
}
