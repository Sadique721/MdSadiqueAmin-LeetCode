class Solution {
    private static final int MOD = 1_000_000_007;
    private static final int RIGHT = 0;
    private static final int DOWN = 1;

    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // go[i][j][0] = destination when moving right
        // go[i][j][1] = destination when moving down
        int[][][] go = new int[m][n][2];

        // -1 represents an invalid move.
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                go[i][j][RIGHT] = -1;
                go[i][j][DOWN] = -1;
            }
        }

        // Precompute destinations in reverse order.
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {

                // Moving right
                int nr = i;
                int nc = j + 1;

                if (nc < n) {
                    if (grid[nr][nc] == 0) {
                        go[i][j][RIGHT] = nr * n + nc;
                    } else {
                        // Entered mirror moving right:
                        // turn down and continue.
                        go[i][j][RIGHT] = go[nr][nc][DOWN];
                    }
                }

                // Moving down
                nr = i + 1;
                nc = j;

                if (nr < m) {
                    if (grid[nr][nc] == 0) {
                        go[i][j][DOWN] = nr * n + nc;
                    } else {
                        // Entered mirror moving down:
                        // turn right and continue.
                        go[i][j][DOWN] = go[nr][nc][RIGHT];
                    }
                }
            }
        }

        int[][] dp = new int[m][n];
        dp[0][0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (dp[i][j] == 0) {
                    continue;
                }

                if (i == m - 1 && j == n - 1) {
                    continue;
                }

                // Move right
                int right = go[i][j][RIGHT];

                if (right != -1) {
                    int x = right / n;
                    int y = right % n;

                    dp[x][y] = (int) (
                        (dp[x][y] + (long) dp[i][j]) % MOD
                    );
                }

                // Move down
                int down = go[i][j][DOWN];

                if (down != -1) {
                    int x = down / n;
                    int y = down % n;

                    dp[x][y] = (int) (
                        (dp[x][y] + (long) dp[i][j]) % MOD
                    );
                }
            }
        }

        return dp[m - 1][n - 1];
    }
}