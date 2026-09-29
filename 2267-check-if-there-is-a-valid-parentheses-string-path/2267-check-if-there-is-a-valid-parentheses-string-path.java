class Solution {
    public boolean hasValidPath(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int len = m + n - 1;

        // Valid parentheses string must have even length.
        if ((len & 1) == 1) {
            return false;
        }

        // Must start with '(' and end with ')'.
        if (grid[0][0] != '(' || grid[m - 1][n - 1] != ')') {
            return false;
        }

        int maxBalance = len / 2;

        // dp[j][balance]
        // represents the previous row.
        boolean[][] dp = new boolean[n][maxBalance + 1];

        for (int i = 0; i < m; i++) {
            boolean[][] next = new boolean[n][maxBalance + 1];

            for (int j = 0; j < n; j++) {

                // Starting cell.
                if (i == 0 && j == 0) {
                    next[0][1] = true;
                    continue;
                }

                for (int balance = 0; balance <= maxBalance; balance++) {

                    int prevBalance;

                    if (grid[i][j] == '(') {
                        prevBalance = balance - 1;
                    } else {
                        prevBalance = balance + 1;
                    }

                    if (prevBalance < 0 || prevBalance > maxBalance) {
                        continue;
                    }

                    // From above.
                    if (i > 0 && dp[j][prevBalance]) {
                        next[j][balance] = true;
                    }

                    // From left.
                    if (j > 0 && next[j - 1][prevBalance]) {
                        next[j][balance] = true;
                    }
                }
            }

            dp = next;
        }

        return dp[n - 1][0];
    }
}