package org.example.cli.puzzle11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Puzzle11Part2 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle11/11.input");
        List<String> lines = Files.readAllLines(inputPath);
        
        long pathCount = solve(lines);
        System.out.println("Total paths from svr to out visiting both dac and fft: " + pathCount);
    }
    
    public static long solve(List<String> lines) {
        Map<String, List<String>> graph = new HashMap<>();
        for (String line : lines) {
            parseLine(line, graph);
        }
        
        // Case 1: svr -> dac -> fft -> out
        long svrToDac = countPaths("svr", "dac", graph, new HashMap<>());
        long dacToFft = countPaths("dac", "fft", graph, new HashMap<>());
        long fftToOut = countPaths("fft", "out", graph, new HashMap<>());
        long path1 = 0;
        if (svrToDac > 0 && dacToFft > 0 && fftToOut > 0) {
            path1 = svrToDac * dacToFft * fftToOut;
        }

        // Case 2: svr -> fft -> dac -> out
        long svrToFft = countPaths("svr", "fft", graph, new HashMap<>());
        long fftToDac = countPaths("fft", "dac", graph, new HashMap<>());
        long dacToOut = countPaths("dac", "out", graph, new HashMap<>());
        long path2 = 0;
        if (svrToFft > 0 && fftToDac > 0 && dacToOut > 0) {
            path2 = svrToFft * fftToDac * dacToOut;
        }

        return path1 + path2;
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
    
    private static long countPaths(String current, String target, Map<String, List<String>> graph, Map<String, Long> memo) {
        if (current.equals(target)) {
            return 1;
        }
        if (memo.containsKey(current)) {
            return memo.get(current);
        }
        
        long count = 0;
        List<String> neighbors = graph.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                count += countPaths(neighbor, target, graph, memo);
            }
        }
        
        memo.put(current, count);
        return count;
    }
}
