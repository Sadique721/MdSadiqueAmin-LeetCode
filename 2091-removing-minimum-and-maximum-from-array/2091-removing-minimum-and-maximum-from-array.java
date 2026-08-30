class Solution {
    public int minimumDeletions(int[] nums) {

        int n = nums.length;

        int minIdx = 0, maxIdx = 0;

        for (int i = 1; i < n; i++) {
            if (nums[i] < nums[minIdx]) minIdx = i;
            if (nums[i] > nums[maxIdx]) maxIdx = i;
        }

        int left = Math.min(minIdx, maxIdx);
        int right = Math.max(minIdx, maxIdx);

        int front = right + 1;
        int back = n - left;
        int both = (left + 1) + (n - right);

        return Math.min(front, Math.min(back, both));
    }
}