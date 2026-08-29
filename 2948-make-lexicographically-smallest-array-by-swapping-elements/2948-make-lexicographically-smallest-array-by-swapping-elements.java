class Solution {
    public int[] lexicographicallySmallestArray(int[] nums, int limit) {

        int n = nums.length;
        int[][] arr = new int[n][2];

        for (int i = 0; i < n; i++) {
            arr[i][0] = nums[i];
            arr[i][1] = i;
        }

        Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));

        int[] ans = new int[n];
        int i = 0;

        while (i < n) {

            int j = i;

            while (j + 1 < n && arr[j + 1][0] - arr[j][0] <= limit) {
                j++;
            }

            int size = j - i + 1;
            int[] idx = new int[size];

            for (int k = 0; k < size; k++) {
                idx[k] = arr[i + k][1];
            }

            Arrays.sort(idx);

            for (int k = 0; k < size; k++) {
                ans[idx[k]] = arr[i + k][0];
            }

            i = j + 1;
        }

        return ans;
    }
}