import java.util.*;

class Solution {

    class SlidingMedian {
        TreeMap<Integer, Integer> low = new TreeMap<>();
        TreeMap<Integer, Integer> high = new TreeMap<>();

        int lowSize = 0, highSize = 0;
        long lowSum = 0, highSum = 0;

        void add(int num) {
            if (low.isEmpty() || num <= low.lastKey()) {
                insert(low, num);
                lowSize++;
                lowSum += num;
            } else {
                insert(high, num);
                highSize++;
                highSum += num;
            }
            rebalance();
        }

        void remove(int num) {
            if (low.containsKey(num)) {
                erase(low, num);
                lowSize--;
                lowSum -= num;
            } else {
                erase(high, num);
                highSize--;
                highSum -= num;
            }
            rebalance();
        }

        void rebalance() {
            int target = (lowSize + highSize + 1) / 2;

            while (lowSize > target) {
                int num = low.lastKey();
                erase(low, num);
                lowSize--;
                lowSum -= num;

                insert(high, num);
                highSize++;
                highSum += num;
            }

            while (lowSize < target) {
                int num = high.firstKey();
                erase(high, num);
                highSize--;
                highSum -= num;

                insert(low, num);
                lowSize++;
                lowSum += num;
            }
        }

        long getCost() {
            long median = low.lastKey();

            return median * lowSize - lowSum
                    + highSum - median * highSize;
        }

        void insert(TreeMap<Integer, Integer> map, int num) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        void erase(TreeMap<Integer, Integer> map, int num) {
            int count = map.get(num);
            if (count == 1) {
                map.remove(num);
            } else {
                map.put(num, count - 1);
            }
        }
    }

    public long minOperations(int[] nums, int x, int k) {
        int n = nums.length;

        long[] cost = new long[n - x + 1];
        SlidingMedian median = new SlidingMedian();

        // Calculate minimum cost for every window of size x.
        for (int i = 0; i < n; i++) {
            median.add(nums[i]);

            if (i >= x) {
                median.remove(nums[i - x]);
            }

            if (i >= x - 1) {
                cost[i - x + 1] = median.getCost();
            }
        }

        // DP: minimum cost to select j non-overlapping windows
        // from the first i elements.
        long INF = Long.MAX_VALUE / 4;
        long[][] dp = new long[k + 1][n + 1];

        for (int j = 1; j <= k; j++) {
            Arrays.fill(dp[j], INF);
        }

        for (int j = 1; j <= k; j++) {
            for (int i = 1; i <= n; i++) {

                // Skip the current element.
                dp[j][i] = dp[j][i - 1];

                // Select a window of size x ending at i - 1.
                if (i >= x && dp[j - 1][i - x] != INF) {
                    long take = dp[j - 1][i - x]
                            + cost[i - x];

                    dp[j][i] = Math.min(dp[j][i], take);
                }
            }
        }

        return dp[k][n];
    }
}