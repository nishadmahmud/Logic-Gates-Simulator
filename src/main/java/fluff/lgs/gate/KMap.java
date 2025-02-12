package fluff.lgs.gate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KMap {
    private final int numVars;
    private final boolean[][] map;
    private final int rows;
    private final int cols;

    public KMap(int numVars) {
        this.numVars = numVars;
        
        // Calculate dimensions for K-map
        switch (numVars) {
            case 1:
                rows = 2;
                cols = 1;
                break;
            case 2:
                rows = 2;
                cols = 2;
                break;
            case 3:
                rows = 2;
                cols = 4;  // For 3 variables, we need 2x4 grid
                break;
            case 4:
                rows = 4;
                cols = 4;
                break;
            default:
                throw new IllegalArgumentException("K-map supports 1 to 4 variables only");
        }
        
        map = new boolean[rows][cols];
    }

    public void fillFromTruthTable(boolean[] truthTable) {
        // Convert linear truth table to 2D K-map layout using Gray code
        for (int i = 0; i < truthTable.length; i++) {
            int[] pos = getKMapPosition(i);
            map[pos[0]][pos[1]] = truthTable[i];
        }
    }

    private int[] getKMapPosition(int index) {
        int row, col;
        
        switch (numVars) {
            case 1:
                row = index;
                col = 0;
                break;
            case 2:
                row = index >> 1;
                col = index & 1;
                break;
            case 3:
                // For 3 variables: first bit -> row, other two bits -> col (in Gray code)
                row = index >> 2;
                col = grayCode(index & 3);
                break;
            case 4:
                // For 4 variables: first two bits -> row, last two bits -> col (both in Gray code)
                row = grayCode(index >> 2);
                col = grayCode(index & 3);
                break;
            default:
                throw new IllegalArgumentException("Invalid number of variables");
        }
        
        return new int[]{row, col};
    }

    private int grayCode(int n) {
        return n ^ (n >> 1);
    }

    public String getMinimalExpression(List<String> variables) {
        // First check if expression is constant
        boolean isAllOnes = true;
        boolean isAllZeros = true;
        
        for (int i = 0; i < (1 << numVars); i++) {
            int[] pos = getKMapPosition(i);
            if (map[pos[0]][pos[1]]) {
                isAllZeros = false;
            } else {
                isAllOnes = false;
            }
            if (!isAllZeros && !isAllOnes) break;
        }
        
        if (isAllOnes) return "1";
        if (isAllZeros) return "0";
        
        // Find groups of adjacent 1s
        List<Group> groups = findGroups();
        
        // Minimize the groups
        List<Group> essentialGroups = findEssentialGroups(groups);
        
        // Convert groups to expression
        return groupsToExpression(essentialGroups, variables);
    }

    private static class Group {
        final int startIndex;
        final int size;
        final boolean[] dontCare;  // Which variables don't matter for this group
        
        Group(int startIndex, int size, int numVars) {
            this.startIndex = startIndex;
            this.size = size;
            this.dontCare = new boolean[numVars];
        }
        
        boolean covers(int minterm) {
            for (int i = 0; i < dontCare.length; i++) {
                if (!dontCare[i] && ((minterm >> i) & 1) != ((startIndex >> i) & 1)) {
                    return false;
                }
            }
            return true;
        }
    }

    private List<Group> findGroups() {
        List<Group> groups = new ArrayList<>();
        
        // Try all possible group sizes (powers of 2)
        for (int size = 1 << numVars; size >= 1; size /= 2) {
            for (int i = 0; i < (1 << numVars); i++) {
                if (isValidGroup(i, size)) {
                    Group group = new Group(i, size, numVars);
                    markDontCareVariables(group);
                    groups.add(group);
                }
            }
        }
        
        return groups;
    }

    private boolean isValidGroup(int start, int size) {
        // Check if all cells in potential group are 1s
        for (int i = 0; i < size; i++) {
            int index = start + i;
            if (index >= (1 << numVars)) return false;
            int[] pos = getKMapPosition(index);
            if (!map[pos[0]][pos[1]]) return false;
        }
        return true;
    }

    private void markDontCareVariables(Group group) {
        for (int var = 0; var < numVars; var++) {
            boolean canVary = true;
            int mask = 1 << var;
            for (int i = 0; i < group.size; i++) {
                int index = group.startIndex + i;
                if ((index & mask) != (group.startIndex & mask)) {
                    canVary = false;
                    break;
                }
            }
            group.dontCare[var] = canVary;
        }
    }

    private List<Group> findEssentialGroups(List<Group> groups) {
        // Find minimal set of groups that cover all 1s
        List<Group> essential = new ArrayList<>();
        Set<Integer> coveredMinterms = new HashSet<>();
        
        // Add groups until all 1s are covered
        while (coveredMinterms.size() < countOnes()) {
            Group bestGroup = null;
            int bestNewCovered = 0;
            
            for (Group group : groups) {
                int newCovered = countNewCovered(group, coveredMinterms);
                if (newCovered > bestNewCovered) {
                    bestNewCovered = newCovered;
                    bestGroup = group;
                }
            }
            
            if (bestGroup != null) {
                essential.add(bestGroup);
                updateCovered(bestGroup, coveredMinterms);
            } else {
                break;
            }
        }
        
        return essential;
    }

    private int countOnes() {
        int count = 0;
        for (int i = 0; i < (1 << numVars); i++) {
            int[] pos = getKMapPosition(i);
            if (map[pos[0]][pos[1]]) count++;
        }
        return count;
    }

    private String groupsToExpression(List<Group> groups, List<String> variables) {
        if (groups.isEmpty()) return "0";
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < groups.size(); i++) {
            if (i > 0) sb.append(" + ");
            appendGroup(sb, groups.get(i), variables);
        }
        return sb.toString();
    }

    private void appendGroup(StringBuilder sb, Group group, List<String> variables) {
        boolean first = true;
        for (int var = 0; var < numVars; var++) {
            if (!group.dontCare[var]) {
                if (!first) sb.append(" • ");
                first = false;
                if ((group.startIndex & (1 << var)) == 0) sb.append("!");
                sb.append(variables.get(var));
            }
        }
        if (first) sb.append("1");  // All variables are don't care
    }

    private int countNewCovered(Group group, Set<Integer> coveredMinterms) {
        int count = 0;
        for (int i = 0; i < (1 << numVars); i++) {
            int[] pos = getKMapPosition(i);
            if (map[pos[0]][pos[1]] && !coveredMinterms.contains(i) && group.covers(i)) {
                count++;
            }
        }
        return count;
    }

    private void updateCovered(Group group, Set<Integer> coveredMinterms) {
        for (int i = 0; i < (1 << numVars); i++) {
            int[] pos = getKMapPosition(i);
            if (map[pos[0]][pos[1]] && group.covers(i)) {
                coveredMinterms.add(i);
            }
        }
    }
} 
