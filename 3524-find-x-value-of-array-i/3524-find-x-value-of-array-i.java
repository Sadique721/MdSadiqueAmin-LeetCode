class Solution {
    public long[] resultArray(int[] nums, int k) {
        long[] ans = new long[k];
        long[] dp = new long[k];

        for (int num : nums) {
            long[] next = new long[k];
            int cur = num % k;

            // Start a new subarray
            next[cur]++;

            // Extend previous subarrays
            for (int r = 0; r < k; r++) {
                next[(r * cur) % k] += dp[r];
            }

            // Add to final answer
            for (int r = 0; r < k; r++) {
                ans[r] += next[r];
            }

            dp = next;
        }

        return ans;
    }
}