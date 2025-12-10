package org.example.cli.puzzle10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle10Part2 {

    public static void main(String[] args) throws IOException {
        Path inputPath = Path.of("src/main/java/org/example/cli/puzzle10/10.input");
        List<String> lines = Files.readAllLines(inputPath);

        long totalPresses = 0;

        for (String line : lines) {
            if (line.isBlank()) continue;
            totalPresses += solveMachine(line);
        }

        System.out.println("Total presses: " + totalPresses);
    }

    private static long solveMachine(String line) {
        // Parse joltage requirements: {3,5,4,7}
        Pattern joltagePattern = Pattern.compile("\\{([0-9,]+)}");
        Matcher joltageMatcher = joltagePattern.matcher(line);
        if (!joltageMatcher.find()) {
            throw new IllegalArgumentException("Invalid line format (joltage): " + line);
        }
        String joltageStr = joltageMatcher.group(1);
        int[] targets = Arrays.stream(joltageStr.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        int numCounters = targets.length;

        // Parse buttons: (...)
        Pattern buttonPattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher buttonMatcher = buttonPattern.matcher(line);

        List<int[]> buttons = new ArrayList<>();
        while (buttonMatcher.find()) {
            String content = buttonMatcher.group(1);
            int[] buttonVec = parseButtonVector(content, numCounters);
            buttons.add(buttonVec);
        }

        int numButtons = buttons.size();

        // Remove buttons that do nothing (all zeros)
        // They only add to cost, so count should be 0.
        // We can just filter them out, but we need to track indices if we were outputting specific counts.
        // For min sum, we just drop them.
        List<int[]> activeButtons = new ArrayList<>();
        for (int[] b : buttons) {
            boolean allZero = true;
            for (int val : b) {
                if (val != 0) {
                    allZero = false;
                    break;
                }
            }
            if (!allZero) {
                activeButtons.add(b);
            }
        }
        
        if (activeButtons.isEmpty()) {
            // If no active buttons, and targets are all 0, cost is 0.
            // If targets are not all 0, impossible.
            for (int t : targets) {
                if (t != 0) return 0; // Or error? Assuming inputs have solutions per problem description, but minimizing cost implies solution existence.
                // If no solution, return 0 (as valid presses? No, effectively "infinity" or impossible).
                // But for this puzzle, let's assume solution exists or return 0 if impossible.
            }
            return 0;
        }
        
        buttons = activeButtons;
        numButtons = buttons.size();

        // Calculate Upper Bounds for each button press count
        // x_j <= min(target_i) for all i where button_j affects counter_i
        long[] upperBounds = new long[numButtons];
        Arrays.fill(upperBounds, Long.MAX_VALUE);

        for (int j = 0; j < numButtons; j++) {
            int[] btn = buttons.get(j);
            for (int i = 0; i < numCounters; i++) {
                if (btn[i] > 0) {
                    // btn[i] is always 1 in this problem, but generalizing slightly
                    upperBounds[j] = Math.min(upperBounds[j], targets[i] / btn[i]);
                }
            }
        }

        // Setup Matrix for Gaussian Elimination
        // Rows: numCounters
        // Cols: numButtons + 1 (last col is RHS)
        double[][] matrix = new double[numCounters][numButtons + 1];
        for (int i = 0; i < numCounters; i++) {
            for (int j = 0; j < numButtons; j++) {
                matrix[i][j] = buttons.get(j)[i];
            }
            matrix[i][numButtons] = targets[i];
        }

        return solveSystem(matrix, numCounters, numButtons, upperBounds);
    }

    private static int[] parseButtonVector(String content, int size) {
        int[] vec = new int[size];
        if (content.isBlank()) return vec;
        String[] parts = content.split(",");
        for (String part : parts) {
            int index = Integer.parseInt(part.trim());
            if (index >= 0 && index < size) {
                vec[index] = 1;
            }
        }
        return vec;
    }

    private static long solveSystem(double[][] matrix, int rows, int cols, long[] upperBounds) {
        // Gaussian elimination to RREF
        int pivotRow = 0;
        int[] pivotColForVariable = new int[cols]; // Maps var index -> row index where it is pivot, or -1
        Arrays.fill(pivotColForVariable, -1);
        
        List<Integer> pivotVars = new ArrayList<>();
        List<Integer> freeVars = new ArrayList<>();

        for (int col = 0; col < cols && pivotRow < rows; col++) {
            // Find pivot
            int sel = -1;
            for (int row = pivotRow; row < rows; row++) {
                if (Math.abs(matrix[row][col]) > 1e-9) {
                    sel = row;
                    break;
                }
            }

            if (sel == -1) {
                continue;
            }

            // Swap rows
            double[] tmp = matrix[pivotRow];
            matrix[pivotRow] = matrix[sel];
            matrix[sel] = tmp;

            // Normalize
            double val = matrix[pivotRow][col];
            for (int j = col; j <= cols; j++) {
                matrix[pivotRow][j] /= val;
            }

            // Eliminate
            for (int i = 0; i < rows; i++) {
                if (i != pivotRow) {
                    double f = matrix[i][col];
                    if (Math.abs(f) > 1e-9) {
                        for (int j = col; j <= cols; j++) {
                            matrix[i][j] -= f * matrix[pivotRow][j];
                        }
                    }
                }
            }

            pivotColForVariable[col] = pivotRow;
            pivotRow++;
        }

        // Identify free and pivot variables
        for (int j = 0; j < cols; j++) {
            if (pivotColForVariable[j] != -1) {
                pivotVars.add(j);
            } else {
                freeVars.add(j);
            }
        }
        
        // Check for consistency in zero rows
        for (int i = pivotRow; i < rows; i++) {
            if (Math.abs(matrix[i][cols]) > 1e-5) {
                // 0 = non-zero, impossible
                System.err.println("Impossible configuration found.");
                return 0; // Or handle as error
            }
        }

        // If no free variables, unique solution (check integer)
        if (freeVars.isEmpty()) {
            long cost = 0;
            for (int j = 0; j < cols; j++) {
                double val = matrix[pivotColForVariable[j]][cols];
                long lVal = Math.round(val);
                if (Math.abs(val - lVal) > 1e-5 || lVal < 0) {
                    System.err.println("Non-integer or negative solution for unique system.");
                    return 0;
                }
                cost += lVal;
            }
            return cost;
        }

        // Iterate free variables
        // This is a recursive search
        return findMinCost(matrix, pivotColForVariable, freeVars, upperBounds, 0, new long[cols]);
    }

    private static long findMinCost(double[][] matrix, int[] pivotColForVariable, 
                                    List<Integer> freeVars, long[] upperBounds, 
                                    int freeVarIdx, long[] currentAssignment) {
        int cols = currentAssignment.length;
        
        if (freeVarIdx == freeVars.size()) {
            // All free vars assigned, calculate pivots
            long currentCost = 0;
            
            // First sum free vars cost
            for (int fIdx : freeVars) {
                currentCost += currentAssignment[fIdx];
            }

            for (int j = 0; j < cols; j++) {
                if (pivotColForVariable[j] != -1) {
                    // Calculate pivot value: RHS - sum(coeff * freeVal)
                    int row = pivotColForVariable[j];
                    double val = matrix[row][cols]; // RHS
                    
                    for (int f : freeVars) {
                        val -= matrix[row][f] * currentAssignment[f];
                    }
                    
                    long lVal = Math.round(val);
                    if (Math.abs(val - lVal) > 1e-5 || lVal < 0) {
                        return Long.MAX_VALUE; // Invalid assignment
                    }
                    currentAssignment[j] = lVal;
                    currentCost += lVal;
                }
            }
            return currentCost;
        }

        int varIdx = freeVars.get(freeVarIdx);
        long minFound = Long.MAX_VALUE;
        long limit = upperBounds[varIdx];

        // Optimization: if limit is huge, we might need better logic, 
        // but given the problem constraints, it should be small enough.
        // Also, checking ranges of pivot vars could tighten this loop.
        
        for (long val = 0; val <= limit; val++) {
            currentAssignment[varIdx] = val;
            long res = findMinCost(matrix, pivotColForVariable, freeVars, upperBounds, freeVarIdx + 1, currentAssignment);
            if (res < minFound) {
                minFound = res;
            }
        }
        
        return minFound;
    }
}
