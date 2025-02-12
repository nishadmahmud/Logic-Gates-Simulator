package fluff.lgs.gate;

import java.util.*;

public class QuineMcCluskey {
    private static class Minterm {
        final int value;
        final String binaryRep;
        final Set<Integer> represents;
        boolean used;

        Minterm(int value, int numVars) {
            this.value = value;
            this.binaryRep = padBinary(Integer.toBinaryString(value), numVars);
            this.represents = new HashSet<>();
            this.represents.add(value);
            this.used = false;
        }

        Minterm(String binary, Set<Integer> represents) {
            this.value = -1; // Combined term
            this.binaryRep = binary;
            this.represents = represents;
            this.used = false;
        }

        private String padBinary(String binary, int length) {
            while (binary.length() < length) {
                binary = "0" + binary;
            }
            return binary;
        }

        boolean canCombineWith(Minterm other) {
            if (binaryRep.length() != other.binaryRep.length()) return false;
            
            int differences = 0;
            for (int i = 0; i < binaryRep.length(); i++) {
                if (binaryRep.charAt(i) != other.binaryRep.charAt(i)) {
                    differences++;
                }
            }
            return differences == 1;
        }

        Minterm combine(Minterm other) {
            StringBuilder combined = new StringBuilder();
            Set<Integer> newRepresents = new HashSet<>(represents);
            newRepresents.addAll(other.represents);
            
            for (int i = 0; i < binaryRep.length(); i++) {
                if (binaryRep.charAt(i) == other.binaryRep.charAt(i)) {
                    combined.append(binaryRep.charAt(i));
                } else {
                    combined.append('-');
                }
            }
            
            return new Minterm(combined.toString(), newRepresents);
        }
    }

    public static String simplify(boolean[] truthTable, List<String> variables) {
        int numVars = variables.size();
        
        // Step 1: Find minterms
        List<Minterm> minterms = new ArrayList<>();
        for (int i = 0; i < truthTable.length; i++) {
            if (truthTable[i]) {
                minterms.add(new Minterm(i, numVars));
            }
        }

        // If all 1s or all 0s, return constant
        if (minterms.size() == 0) return "0";
        if (minterms.size() == (1 << numVars)) return "1";

        // Step 2: Find prime implicants
        List<Minterm> primeImplicants = findPrimeImplicants(minterms);

        // Step 3: Build and solve coverage table
        List<Minterm> essentialPrimeImplicants = findEssentialPrimeImplicants(primeImplicants, minterms);

        // Step 4: Convert to boolean expression
        return convertToExpression(essentialPrimeImplicants, variables);
    }

    private static List<Minterm> findPrimeImplicants(List<Minterm> minterms) {
        List<Minterm> primeImplicants = new ArrayList<>();
        List<Minterm> currentGroup = new ArrayList<>(minterms);
        
        while (!currentGroup.isEmpty()) {
            Set<String> combinedTerms = new HashSet<>();
            List<Minterm> nextGroup = new ArrayList<>();
            
            for (int i = 0; i < currentGroup.size(); i++) {
                boolean combined = false;
                
                for (int j = i + 1; j < currentGroup.size(); j++) {
                    if (currentGroup.get(i).canCombineWith(currentGroup.get(j))) {
                        combined = true;
                        currentGroup.get(i).used = true;
                        currentGroup.get(j).used = true;
                        
                        Minterm newTerm = currentGroup.get(i).combine(currentGroup.get(j));
                        if (!combinedTerms.contains(newTerm.binaryRep)) {
                            nextGroup.add(newTerm);
                            combinedTerms.add(newTerm.binaryRep);
                        }
                    }
                }
                
                if (!combined && !currentGroup.get(i).used) {
                    primeImplicants.add(currentGroup.get(i));
                }
            }
            
            if (nextGroup.isEmpty()) break;
            currentGroup = nextGroup;
        }
        
        return primeImplicants;
    }

    private static List<Minterm> findEssentialPrimeImplicants(List<Minterm> primeImplicants, List<Minterm> originalMinterms) {
        List<Minterm> essential = new ArrayList<>();
        Set<Integer> coveredMinterms = new HashSet<>();
        
        // Find essential prime implicants
        for (Minterm original : originalMinterms) {
            int count = 0;
            Minterm lastCovering = null;
            
            for (Minterm prime : primeImplicants) {
                if (prime.represents.contains(original.value)) {
                    count++;
                    lastCovering = prime;
                }
            }
            
            if (count == 1 && !essential.contains(lastCovering)) {
                essential.add(lastCovering);
                coveredMinterms.addAll(lastCovering.represents);
            }
        }
        
        // Add remaining prime implicants if needed
        while (coveredMinterms.size() < originalMinterms.size()) {
            Minterm bestPrime = null;
            int maxUncovered = 0;
            
            for (Minterm prime : primeImplicants) {
                if (!essential.contains(prime)) {
                    int uncovered = 0;
                    for (int minterm : prime.represents) {
                        if (!coveredMinterms.contains(minterm)) {
                            uncovered++;
                        }
                    }
                    if (uncovered > maxUncovered) {
                        maxUncovered = uncovered;
                        bestPrime = prime;
                    }
                }
            }
            
            if (bestPrime == null) break;
            essential.add(bestPrime);
            coveredMinterms.addAll(bestPrime.represents);
        }
        
        return essential;
    }

    private static String convertToExpression(List<Minterm> implicants, List<String> variables) {
        if (implicants.isEmpty()) return "0";
        
        List<String> terms = new ArrayList<>();
        for (Minterm implicant : implicants) {
            StringBuilder term = new StringBuilder();
            boolean first = true;
            
            for (int i = 0; i < implicant.binaryRep.length(); i++) {
                char bit = implicant.binaryRep.charAt(i);
                if (bit != '-') {
                    if (!first) term.append(" • ");
                    if (bit == '0') term.append("!");
                    term.append(variables.get(i));
                    first = false;
                }
            }
            
            if (term.length() == 0) term.append("1");
            terms.add(term.toString());
        }
        
        return String.join(" + ", terms);
    }
} 