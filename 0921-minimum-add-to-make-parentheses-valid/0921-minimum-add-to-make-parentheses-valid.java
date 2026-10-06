class Solution {
    public int minAddToMakeValid(String s) {
        int open = 0;
        int additions = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                open++;
            } else {
                if (open > 0) {
                    open--;
                } else {
                    // Need to insert '(' before this ')'
                    additions++;
                }
            }
        }

        // Remaining '(' need matching ')'
        return additions + open;
    }
}