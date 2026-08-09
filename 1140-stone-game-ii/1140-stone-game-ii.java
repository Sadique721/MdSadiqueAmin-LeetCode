class Solution {
    public int stoneGameII(int[] piles) {

        int n = piles.length;

        int[] suffix = new int[n + 1];

        for (int i = n - 1; i >= 0; i--) {
            suffix[i] = suffix[i + 1] + piles[i];
        }

        int[][] dp = new int[n][n + 1];

        for (int i = n - 1; i >= 0; i--) {

            for (int M = 1; M <= n; M++) {

                if (i + 2 * M >= n) {
                    dp[i][M] = suffix[i];
                    continue;
                }

                int best = 0;

                for (int X = 1; X <= 2 * M; X++) {

                    int opponent = dp[i + X][Math.max(M, X)];

                    best = Math.max(
                        best,
                        suffix[i] - opponent
                    );
                }

                dp[i][M] = best;
            }
        }

        return dp[0][1];
    }
}