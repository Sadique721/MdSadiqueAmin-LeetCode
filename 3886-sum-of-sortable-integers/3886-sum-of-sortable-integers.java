import java.util.Arrays;

class Solution {
    public int sortableIntegers(int[] nums) {
        int n = nums.length;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int answer = 0;

        for (int k = 1; k <= n; k++) {
            if (n % k != 0) {
                continue;
            }

            boolean valid = true;

            for (int start = 0; start < n && valid; start += k) {
                if (!canRotate(nums, sorted, start, k)) {
                    valid = false;
                }
            }

            if (valid) {
                answer += k;
            }
        }

        return answer;
    }

    private boolean canRotate(int[] nums, int[] sorted, int start, int k) {
        // Find whether sorted[start..start+k-1] occurs in
        // nums[start..start+k-1] concatenated with itself.
        int[] pattern = new int[k];
        System.arraycopy(sorted, start, pattern, 0, k);

        int[] lps = new int[k];
        for (int i = 1, len = 0; i < k; ) {
            if (pattern[i] == pattern[len]) {
                lps[i++] = ++len;
            } else if (len > 0) {
                len = lps[len - 1];
            } else {
                lps[i++] = 0;
            }
        }

        int matched = 0;

        for (int i = 0; i < 2 * k - 1; i++) {
            int value = nums[start + (i % k)];

            while (matched > 0 && value != pattern[matched]) {
                matched = lps[matched - 1];
            }

            if (value == pattern[matched]) {
                matched++;
            }

            if (matched == k) {
                return true;
            }
        }

        return false;
    }
}