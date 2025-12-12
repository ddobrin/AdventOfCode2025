package org.example.cli.puzzle12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle12 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle12/12.input");
        List<String> lines = Files.readAllLines(inputPath);

        Map<Integer, Integer> shapeAreas = new HashMap<>();
        List<String> currentShapeLines = new ArrayList<>();
        Integer currentShapeId = null;

        int passingRegions = 0;

        // Use double backslashes for Java string literals
        Pattern shapeHeader = Pattern.compile("^(\\d+):$");
        Pattern regionLine = Pattern.compile("^(\\d+)x(\\d+):\\s*(.*)$");

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                if (currentShapeId != null && !currentShapeLines.isEmpty()) {
                    shapeAreas.put(currentShapeId, calculateArea(currentShapeLines));
                    currentShapeLines.clear();
                    currentShapeId = null;
                }
                continue;
            }

            Matcher mHeader = shapeHeader.matcher(line);
            if (mHeader.matches()) {
                // Save previous if exists
                if (currentShapeId != null && !currentShapeLines.isEmpty()) {
                    shapeAreas.put(currentShapeId, calculateArea(currentShapeLines));
                    currentShapeLines.clear();
                }
                currentShapeId = Integer.parseInt(mHeader.group(1));
                continue;
            }

            Matcher mRegion = regionLine.matcher(line);
            if (mRegion.matches()) {
                // Ensure last shape is saved
                if (currentShapeId != null && !currentShapeLines.isEmpty()) {
                    shapeAreas.put(currentShapeId, calculateArea(currentShapeLines));
                    currentShapeLines.clear();
                    currentShapeId = null;
                }

                long width = Long.parseLong(mRegion.group(1));
                long height = Long.parseLong(mRegion.group(2));
                long regionArea = width * height;

                String[] counts = mRegion.group(3).trim().split("\\s+");
                long presentsArea = 0;
                for (int shapeId = 0; shapeId < counts.length; shapeId++) {
                    long count = Long.parseLong(counts[shapeId]);
                    int area = shapeAreas.getOrDefault(shapeId, 0);
                    presentsArea += count * area;
                }

                if (presentsArea <= regionArea) {
                    passingRegions++;
                }
            } else {
                // Must be a shape line
                if (currentShapeId != null) {
                    currentShapeLines.add(line);
                }
            }
        }
        
         if (currentShapeId != null && !currentShapeLines.isEmpty()) {
             shapeAreas.put(currentShapeId, calculateArea(currentShapeLines));
         }

        System.out.println("Number of regions that can fit the presents: " + passingRegions);
    }

    private static int calculateArea(List<String> shapeLines) {
        int area = 0;
        for (String line : shapeLines) {
            for (char c : line.toCharArray()) {
                if (c == '#') {
                    area++;
                }
            }
        }
        return area;
    }
}