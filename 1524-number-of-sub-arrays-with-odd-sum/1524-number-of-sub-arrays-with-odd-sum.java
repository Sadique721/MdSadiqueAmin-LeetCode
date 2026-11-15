
class Solution {
    public int numOfSubarrays(int[] arr) {

        final int MOD = 1_000_000_007;

        long even = 1;
        long odd = 0;
        long prefixSum = 0;
        long ans = 0;

        for (int num : arr) {
            prefixSum += num;

            if (prefixSum % 2 == 0) {
                ans = (ans + odd) % MOD;
                even++;
            } else {
                ans = (ans + even) % MOD;
                odd++;
            }
        }

        return (int) ans;
    }
}
