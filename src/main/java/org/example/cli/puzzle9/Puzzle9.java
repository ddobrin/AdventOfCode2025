package org.example.cli.puzzle9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Puzzle9 {

    public static void main(String[] args) {
        String inputFile = "src/main/java/org/example/cli/puzzle9/9.input";
        // String inputFile = "src/main/java/org/example/cli/puzzle9/test.input"; // For debugging

        try {
            List<Point> polygon = parseInput(inputFile);
            
            System.out.println("Part 1: " + solvePart1(polygon));
            System.out.println("Part 2: " + solvePart2(polygon));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<Point> parseInput(String inputFile) throws IOException {
        List<Point> points = new ArrayList<>();
        List<String> lines = Files.readAllLines(Path.of(inputFile));
        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] parts = line.split(",");
            points.add(new Point(Long.parseLong(parts[0].trim()), Long.parseLong(parts[1].trim())));
        }
        return points;
    }

    static long solvePart1(List<Point> points) {
        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                maxArea = Math.max(maxArea, calculateArea(points.get(i), points.get(j)));
            }
        }
        return maxArea;
    }

    static long solvePart2(List<Point> polygon) {
        long maxArea = 0;
        int n = polygon.size();
        
        // Pre-calculate edges
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            edges.add(new Edge(polygon.get(i), polygon.get((i + 1) % n)));
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point p1 = polygon.get(i);
                Point p2 = polygon.get(j);
                long area = calculateArea(p1, p2);

                if (area <= maxArea) continue; // Optimization

                Rectangle rect = new Rectangle(p1, p2);
                if (isInside(rect, polygon, edges)) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    static long calculateArea(Point p1, Point p2) {
        return (Math.abs(p1.x - p2.x) + 1) * (Math.abs(p1.y - p2.y) + 1);
    }

    static boolean isInside(Rectangle rect, List<Point> polygon, List<Edge> edges) {
        // 1. Handle thin rectangles (width 0 or height 0)
        boolean isThin = (rect.minX == rect.maxX) || (rect.minY == rect.maxY);
        if (isThin) {
            // Check if the thin rectangle lies on a polygon boundary
            for (Edge edge : edges) {
                if (containsSegment(edge, rect)) {
                    return true;
                }
            }
        }

        // 2. Check if center is inside
        double centerX = (rect.minX + rect.maxX) / 2.0;
        double centerY = (rect.minY + rect.maxY) / 2.0;
        
        // If thin, center might be on an integer coordinate/line. Perturb slightly to avoid singularities.
        // If not thin, center is always (int + 0.5), so it's safe from integer vertices.
        if (isThin) {
            centerX += 0.001;
            centerY += 0.001;
        }
        
        if (!containsPoint(centerX, centerY, polygon)) {
            return false;
        }

        // 3. Check if any polygon edge intersects the INTERIOR of the rectangle
        for (Edge edge : edges) {
            if (intersectsInterior(edge, rect)) {
                return false;
            }
        }

        return true;
    }

    static boolean containsSegment(Edge edge, Rectangle rect) {
        if (rect.minX == rect.maxX) { // Vertical Rect
            if (edge.p1.x != edge.p2.x) return false; // Edge not vertical
            if (edge.p1.x != rect.minX) return false; // Not same X
            long ey1 = Math.min(edge.p1.y, edge.p2.y);
            long ey2 = Math.max(edge.p1.y, edge.p2.y);
            return (rect.minY >= ey1 && rect.maxY <= ey2);
        } else { // Horizontal Rect
            if (edge.p1.y != edge.p2.y) return false; // Edge not horizontal
            if (edge.p1.y != rect.minY) return false; // Not same Y
            long ex1 = Math.min(edge.p1.x, edge.p2.x);
            long ex2 = Math.max(edge.p1.x, edge.p2.x);
            return (rect.minX >= ex1 && rect.maxX <= ex2);
        }
    }

    // Ray casting algorithm
    static boolean containsPoint(double x, double y, List<Point> polygon) {
        boolean inside = false;
        int n = polygon.size();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Point pi = polygon.get(i);
            Point pj = polygon.get(j);
            
            // Check if ray intersects the edge
            // We cast ray to the right (increasing x)
            if (((pi.y > y) != (pj.y > y)) &&
                (x < (double)(pj.x - pi.x) * (y - pi.y) / (double)(pj.y - pi.y) + pi.x)) {
                inside = !inside;
            }
        }
        return inside;
    }

    static boolean intersectsInterior(Edge edge, Rectangle rect) {
        // Edge is vertical
        if (edge.p1.x == edge.p2.x) {
            long x = edge.p1.x;
            long y1 = Math.min(edge.p1.y, edge.p2.y);
            long y2 = Math.max(edge.p1.y, edge.p2.y);

            // Must be strictly between rect.minX and rect.maxX
            if (x > rect.minX && x < rect.maxX) {
                // And y intervals must overlap
                // Overlap if max(y1, rect.minY) < min(y2, rect.maxY)
                return Math.max(y1, rect.minY) < Math.min(y2, rect.maxY);
            }
        } 
        // Edge is horizontal
        else {
            long y = edge.p1.y;
            long x1 = Math.min(edge.p1.x, edge.p2.x);
            long x2 = Math.max(edge.p1.x, edge.p2.x);

            // Must be strictly between rect.minY and rect.maxY
            if (y > rect.minY && y < rect.maxY) {
                // And x intervals must overlap
                return Math.max(x1, rect.minX) < Math.min(x2, rect.maxX);
            }
        }
        return false;
    }

    record Point(long x, long y) {}
    record Edge(Point p1, Point p2) {}
    
    static class Rectangle {
        long minX, maxX, minY, maxY;

        Rectangle(Point p1, Point p2) {
            this.minX = Math.min(p1.x, p2.x);
            this.maxX = Math.max(p1.x, p2.x);
            this.minY = Math.min(p1.y, p2.y);
            this.maxY = Math.max(p1.y, p2.y);
        }
    }
}