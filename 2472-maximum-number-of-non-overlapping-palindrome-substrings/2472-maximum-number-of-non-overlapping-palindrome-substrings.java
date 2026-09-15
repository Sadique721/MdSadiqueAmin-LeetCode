class Solution {
    public int maxPalindromes(String s, int k) {
        int n = s.length();
        int[] dp = new int[n + 1];

        for (int center = 0; center < n; center++) {

            // Odd length palindromes
            expand(s, center, center, k, dp);

            // Even length palindromes
            expand(s, center, center + 1, k, dp);

            // Carry forward the best answer
            dp[center + 1] = Math.max(dp[center + 1], dp[center]);
        }

        return dp[n];
    }

    private void expand(String s, int l, int r, int k, int[] dp) {
        int n = s.length();

        while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
            if (r - l + 1 >= k) {
                dp[r + 1] = Math.max(dp[r + 1], dp[l] + 1);
            }
            l--;
            r++;
        }
    }
}