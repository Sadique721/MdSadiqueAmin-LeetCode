class Solution {
    public int minSumOfLengths(int[] arr, int target) {
        int n = arr.length;
        int INF = 1_000_000_000;

        int[] best = new int[n];
        java.util.Arrays.fill(best, INF);

        java.util.HashMap<Integer, Integer> map = new java.util.HashMap<>();
        map.put(0, -1);

        int sum = 0;
        int ans = INF;

        for (int i = 0; i < n; i++) {
            sum += arr[i];

            best[i] = (i > 0) ? best[i - 1] : INF;

            if (map.containsKey(sum - target)) {
                int start = map.get(sum - target);
                int len = i - start;

                if (start >= 0 && best[start] != INF) {
                    ans = Math.min(ans, len + best[start]);
                }

                best[i] = Math.min(best[i], len);
            }

            map.put(sum, i);
        }

        return ans == INF ? -1 : ans;
    }
}