
class Solution {
    public int minimumChanges(String s, int k) {

        int n = s.length();
        int INF = 1_000_000_000;

        // Step 1: Precompute minimum changes for every substring.
        int[][] cost = new int[n][n];

        for (int len = 2; len <= n; len++) {

            for (int l = 0; l + len <= n; l++) {

                int r = l + len - 1;
                cost[l][r] = INF;

                // Try every proper divisor of substring length.
                for (int d = 1; d < len; d++) {

                    if (len % d != 0) {
                        continue;
                    }

                    int changes = 0;
                    int groupSize = len / d;

                    // Check palindrome in each group.
                    for (int offset = 0; offset < d; offset++) {

                        for (int p = 0; p < groupSize / 2; p++) {

                            int left = l + offset + p * d;
                            int right = l + offset
                                    + (groupSize - 1 - p) * d;

                            if (s.charAt(left) != s.charAt(right)) {
                                changes++;
                            }
                        }
                    }

                    cost[l][r] = Math.min(cost[l][r], changes);
                }
            }
        }

        // Step 2: DP - partition into exactly k substrings.
        int[][] dp = new int[k + 1][n + 1];

        for (int i = 0; i <= k; i++) {
            java.util.Arrays.fill(dp[i], INF);
        }

        dp[0][0] = 0;

        for (int parts = 1; parts <= k; parts++) {

            for (int end = 2 * parts; end <= n; end++) {

                for (int start = 2 * (parts - 1);
                     start <= end - 2; start++) {

                    if (dp[parts - 1][start] == INF) {
                        continue;
                    }

                    dp[parts][end] = Math.min(
                        dp[parts][end],
                        dp[parts - 1][start] + cost[start][end - 1]
                    );
                }
            }
        }

        return dp[k][n];
    }
}
