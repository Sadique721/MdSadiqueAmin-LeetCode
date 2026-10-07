import java.util.*;

class Solution {
    public List<String> removeInvalidParentheses(String s) {

        List<String> result = new ArrayList<>();

        // BFS queue
        Queue<String> queue = new LinkedList<>();
        queue.offer(s);

        // Avoid duplicate strings
        Set<String> visited = new HashSet<>();
        visited.add(s);

        boolean found = false;

        while (!queue.isEmpty()) {

            int size = queue.size();

            // Process one BFS level
            for (int i = 0; i < size; i++) {

                String current = queue.poll();

                // Valid string found
                if (isValid(current)) {
                    result.add(current);
                    found = true;
                }

                // If valid strings found at this level,
                // don't generate next level.
                if (found) {
                    continue;
                }

                // Try removing one character
                for (int j = 0; j < current.length(); j++) {

                    // Only parentheses need to be removed
                    if (current.charAt(j) != '(' &&
                        current.charAt(j) != ')') {
                        continue;
                    }

                    String next =
                        current.substring(0, j)
                        + current.substring(j + 1);

                    if (visited.add(next)) {
                        queue.offer(next);
                    }
                }
            }

            // Minimum removals found
            if (found) {
                break;
            }
        }

        return result;
    }

    private boolean isValid(String s) {

        int balance = 0;

        for (char ch : s.toCharArray()) {

            if (ch == '(') {
                balance++;
            } 
            else if (ch == ')') {
                balance--;

                // More closing brackets than opening brackets
                if (balance < 0) {
                    return false;
                }
            }
        }

        return balance == 0;
    }
}