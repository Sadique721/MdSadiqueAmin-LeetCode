class Solution {
    public int longestValidParentheses(String s) {
        int n = s.length();
        int[] dp = new int[n];
        int max = 0;

        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {

                // Case: "()"
                if (s.charAt(i - 1) == '(') {
                    dp[i] = 2 + (i >= 2 ? dp[i - 2] : 0);
                }

                // Case: "(...)" where previous char is ')'
                else {
                    int j = i - dp[i - 1] - 1;

                    if (j >= 0 && s.charAt(j) == '(') {
                        dp[i] = dp[i - 1] + 2
                                + (j >= 1 ? dp[j - 1] : 0);
                    }
                }

                max = Math.max(max, dp[i]);
            }
        }

        return max;
    }
}