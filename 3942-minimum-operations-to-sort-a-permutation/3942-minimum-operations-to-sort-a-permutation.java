import java.util.Arrays;
import java.util.Queue;
import java.util.ArrayDeque;

class Solution {
    public int minOperations(int[] nums) {
        int n = nums.length;
        int zeroIndex = -1;

        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                zeroIndex = i;
                break;
            }
        }

        int target0 = zeroIndex;
        boolean valid0 = true;

        for (int i = 0; i < n; i++) {
            if (nums[(target0 + i) % n] != i) {
                valid0 = false;
                break;
            }
        }

        int target1 = (n - 1 - zeroIndex + n) % n;
        boolean valid1 = true;

        for (int i = 0; i < n; i++) {
            int index = n - 1 - ((target1 + i) % n);
            if (nums[index] != i) {
                valid1 = false;
                break;
            }
        }

        if (!valid0 && !valid1) {
            return -1;
        }

        int total = 2 * n;
        int[] dist = new int[total];
        Arrays.fill(dist, -1);

        Queue<Integer> queue = new ArrayDeque<>();
        dist[0] = 0;
        queue.offer(0);

        int goal0 = target0;
        int goal1 = n + target1;

        while (!queue.isEmpty()) {
            int state = queue.poll();
            int type = state / n;
            int shift = state % n;

            if ((valid0 && state == goal0) ||
                (valid1 && state == goal1)) {
                return dist[state];
            }

            int rotate = type * n + (shift + 1) % n;
            if (dist[rotate] == -1) {
                dist[rotate] = dist[state] + 1;
                queue.offer(rotate);
            }

            int reverse = (1 - type) * n + (n - shift) % n;
            if (dist[reverse] == -1) {
                dist[reverse] = dist[state] + 1;
                queue.offer(reverse);
            }
        }

        return -1;
    }
}