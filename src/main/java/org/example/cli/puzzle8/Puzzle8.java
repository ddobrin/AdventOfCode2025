package org.example.cli.puzzle8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle8 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle8/8.input");
        List<String> lines = Files.readAllLines(inputPath);
        
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) continue;
            String[] parts = line.split(",");
            long x = Long.parseLong(parts[0].trim());
            long y = Long.parseLong(parts[1].trim());
            long z = Long.parseLong(parts[2].trim());
            points.add(new Point(i, x, y, z));
        }

        solve(points);
    }

    private static void solve(List<Point> points) {
        List<Pair> allPairs = new ArrayList<>();
        int n = points.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                long distSq = distanceSq(p1, p2);
                allPairs.add(new Pair(p1, p2, distSq));
            }
        }

        // Sort by distance ascending
        allPairs.sort(Comparator.comparingLong(Pair::distSq));

        // Part 1
        UnionFind dsu1 = new UnionFind(n);
        int limit = Math.min(1000, allPairs.size());
        for (int i = 0; i < limit; i++) {
            Pair p = allPairs.get(i);
            dsu1.union(p.u.id, p.v.id);
        }

        // Calculate circuit sizes
        Map<Integer, Integer> rootCounts = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu1.find(i);
            rootCounts.put(root, rootCounts.getOrDefault(root, 0) + 1);
        }

        List<Integer> sizes = new ArrayList<>(rootCounts.values());
        sizes.sort(Collections.reverseOrder());

        long part1Ans = 0;
        if (sizes.size() >= 3) {
            part1Ans = (long) sizes.get(0) * sizes.get(1) * sizes.get(2);
        } else {
            // Fallback if fewer than 3 circuits (unlikely given the problem)
            part1Ans = sizes.stream().mapToLong(i -> i).reduce(1, (a, b) -> a * b);
        }
        System.out.println("Part 1: " + part1Ans);

        // Part 2
        UnionFind dsu2 = new UnionFind(n);
        int components = n;
        long part2Ans = 0;

        for (Pair p : allPairs) {
            if (dsu2.union(p.u.id, p.v.id)) {
                components--;
                if (components == 1) {
                    part2Ans = p.u.x * p.v.x;
                    break;
                }
            }
        }
        System.out.println("Part 2: " + part2Ans);
    }

    private static long distanceSq(Point p1, Point p2) {
        long dx = p1.x - p2.x;
        long dy = p1.y - p2.y;
        long dz = p1.z - p2.z;
        return dx * dx + dy * dy + dz * dz;
    }

    record Point(int id, long x, long y, long z) {}
    record Pair(Point u, Point v, long distSq) {}

    static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);
            if (rootI != rootJ) {
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootI] > rank[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++;
                }
                return true;
            }
            return false;
        }
    }
}