import java.util.*;

class Solution {

    static class State {
        long weight;
        int[] idx;

        State(long weight, int[] idx) {
            this.weight = weight;
            this.idx = idx;
        }
    }

    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        int[][] arr = new int[n][4];
        for (int i = 0; i < n; i++) {
            arr[i][0] = intervals.get(i).get(0); // start
            arr[i][1] = intervals.get(i).get(1); // end
            arr[i][2] = intervals.get(i).get(2); // weight
            arr[i][3] = i;                       // original index
        }

        Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));

        int[] starts = new int[n];
        for (int i = 0; i < n; i++) starts[i] = arr[i][0];

        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            next[i] = upperBound(starts, arr[i][1]);
        }

        State[][] dp = new State[n + 1][5];

        for (int k = 0; k <= 4; k++) dp[n][k] = new State(0, new int[0]);

        for (int i = n - 1; i >= 0; i--) {
            dp[i][0] = new State(0, new int[0]);

            for (int k = 1; k <= 4; k++) {

                State skip = dp[i + 1][k];
                State child = dp[next[i]][k - 1];

                int[] merged = insertSorted(child.idx, arr[i][3]);
                State take = new State(child.weight + arr[i][2], merged);

                dp[i][k] = better(skip, take);
            }
        }

        return dp[0][4].idx;
    }

    private static int upperBound(int[] starts, int target) {
        int l = 0, r = starts.length;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (starts[m] <= target) l = m + 1;
            else r = m;
        }
        return l;
    }

    private static int[] insertSorted(int[] arr, int val) {
        int[] res = new int[arr.length + 1];
        int i = 0, j = 0;

        while (i < arr.length && arr[i] < val) res[j++] = arr[i++];
        res[j++] = val;
        while (i < arr.length) res[j++] = arr[i++];

        return res;
    }

    private static State better(State a, State b) {
        if (a.weight != b.weight) {
            return a.weight > b.weight ? a : b;
        }

        int m = Math.min(a.idx.length, b.idx.length);
        for (int i = 0; i < m; i++) {
            if (a.idx[i] != b.idx[i]) {
                return a.idx[i] < b.idx[i] ? a : b;
            }
        }

        return a.idx.length <= b.idx.length ? a : b;
    }
}