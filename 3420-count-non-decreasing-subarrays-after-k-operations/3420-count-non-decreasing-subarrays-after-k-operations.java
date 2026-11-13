
import java.util.*;

class Solution {
    public long countNonDecreasingSubarrays(int[] nums, int k) {
        int n = nums.length;
        int LOG = 1;

        while ((1 << LOG) <= n) {
            LOG++;
        }

        long[] prefix = new long[n + 1];

        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        // Next greater element
        int[] nextGreater = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() &&
                   nums[stack.peek()] <= nums[i]) {
                stack.pop();
            }

            nextGreater[i] = stack.isEmpty() ? n : stack.peek();
            stack.push(i);
        }

        int[][] jump = new int[LOG][n + 1];
        long[][] cost = new long[LOG][n + 1];

        // Important: initialize sentinel index
        for (int j = 0; j < LOG; j++) {
            jump[j][n] = n;
        }

        for (int i = 0; i < n; i++) {
            int next = nextGreater[i];

            jump[0][i] = next;
            cost[0][i] =
                (long) nums[i] * (next - i)
                - (prefix[next] - prefix[i]);
        }

        // Binary lifting
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                int mid = jump[j - 1][i];

                jump[j][i] = jump[j - 1][mid];
                cost[j][i] =
                    cost[j - 1][i] + cost[j - 1][mid];
            }
        }

        long answer = 0;
        int right = 0;

        // Sliding window
        for (int left = 0; left < n; left++) {
            while (right < n &&
                   getCost(left, right, nums, prefix,
                           jump, cost, LOG) <= k) {
                right++;
            }

            answer += right - left;
        }

        return answer;
    }

    private long getCost(
        int left,
        int right,
        int[] nums,
        long[] prefix,
        int[][] jump,
        long[][] cost,
        int LOG
    ) {
        long operations = 0;
        int current = left;

        for (int j = LOG - 1; j >= 0; j--) {
            int next = jump[j][current];

            if (next <= right + 1) {
                operations += cost[j][current];
                current = next;
            }
        }

        if (current <= right) {
            operations +=
                (long) nums[current] * (right - current + 1)
                - (prefix[right + 1] - prefix[current]);
        }

        return operations;
    }
}
