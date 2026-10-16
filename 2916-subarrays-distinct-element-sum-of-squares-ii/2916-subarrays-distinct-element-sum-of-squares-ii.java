class Solution {
    private static final long MOD = 1_000_000_007L;

    private long[] sum;
    private long[] sumSq;
    private long[] lazy;
    private int n;

    public int sumCounts(int[] nums) {
        n = nums.length;

        sum = new long[4 * n];
        sumSq = new long[4 * n];
        lazy = new long[4 * n];

        int[] last = new int[100001];
        java.util.Arrays.fill(last, -1);

        long answer = 0;

        for (int i = 0; i < n; i++) {
            int left = last[nums[i]] + 1;

            update(1, 0, n - 1, left, i, 1);

            answer = (answer + sumSq[1]) % MOD;

            last[nums[i]] = i;
        }

        return (int) answer;
    }

    private void apply(int node, int length, long delta) {
        long oldSum = sum[node];

        sumSq[node] = (
            sumSq[node]
            + 2L * delta % MOD * oldSum % MOD
            + delta * delta % MOD * length % MOD
        ) % MOD;

        sum[node] = (oldSum + delta * length) % MOD;
        lazy[node] = (lazy[node] + delta) % MOD;
    }

    private void push(int node, int left, int right) {
        if (lazy[node] == 0 || left == right) {
            return;
        }

        int mid = left + (right - left) / 2;
        long delta = lazy[node];

        apply(node * 2, mid - left + 1, delta);
        apply(node * 2 + 1, right - mid, delta);

        lazy[node] = 0;
    }

    private void update(
        int node, int left, int right,
        int queryLeft, int queryRight, long delta
    ) {
        if (queryLeft > right || queryRight < left) {
            return;
        }

        if (queryLeft <= left && right <= queryRight) {
            apply(node, right - left + 1, delta);
            return;
        }

        push(node, left, right);

        int mid = left + (right - left) / 2;

        update(node * 2, left, mid, queryLeft, queryRight, delta);
        update(node * 2 + 1, mid + 1, right, queryLeft, queryRight, delta);

        sum[node] = (sum[node * 2] + sum[node * 2 + 1]) % MOD;
        sumSq[node] = (sumSq[node * 2] + sumSq[node * 2 + 1]) % MOD;
    }
}