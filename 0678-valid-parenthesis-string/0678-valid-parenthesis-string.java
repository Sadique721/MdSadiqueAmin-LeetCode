class Solution {
    public boolean checkValidString(String s) {
        int minOpen = 0;
        int maxOpen = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                minOpen++;
                maxOpen++;
            } 
            else if (c == ')') {
                minOpen--;
                maxOpen--;
            } 
            else { // '*'
                minOpen--; // '*' as ')'
                maxOpen++; // '*' as '('
            }

            // Even the maximum possible opening brackets cannot be valid
            if (maxOpen < 0) {
                return false;
            }

            // Minimum cannot go below 0
            minOpen = Math.max(minOpen, 0);
        }

        return minOpen == 0;
    }
}