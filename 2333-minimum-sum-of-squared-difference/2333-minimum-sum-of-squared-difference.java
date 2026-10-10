class Solution {
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        long[] freq = new long[100001];
        long operations = (long) k1 + k2;

        for (int i = 0; i < nums1.length; i++) {
            int diff = Math.abs(nums1[i] - nums2[i]);
            freq[diff]++;
        }

        for (int i = 100000; i > 0 && operations > 0; i--) {
            if (freq[i] == 0) {
                continue;
            }

            long move = Math.min(freq[i], operations);
            freq[i] -= move;
            freq[i - 1] += move;
            operations -= move;
        }

        if (operations > 0) {
            return 0L;
        }

        long result = 0;

        for (int i = 1; i <= 100000; i++) {
            result += (long) i * i * freq[i];
        }

        return result;
    }
}