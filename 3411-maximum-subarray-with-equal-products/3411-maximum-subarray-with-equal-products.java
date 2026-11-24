class Solution {
    // prime indices: 0->2, 1->3, 2->5, 3->7
    private static final int[][] EXP = new int[11][4];

    static {
        // pre���compute exponents for 1…10
        for (int x = 2; x <= 10; x++) {
            int[] e = EXP[x];
            e[0] = countFactor(x, 2); // exponent of 2
            e[1] = countFactor(x, 3); // exponent of 3
            e[2] = countFactor(x, 5); // exponent of 5
            e[3] = countFactor(x, 7); // exponent of 7
        }
        // EXP[1] remains {0,0,0,0}
    }

    private static int countFactor(int n, int p) {
        int cnt = 0;
        while (n % p == 0) {
            n /= p;
            cnt++;
        }
        return cnt;
    }

    public int maxLength(int[] nums) {
        int n = nums.length;
        int best = 0;

        for (int i = 0; i < n; i++) {
            int[] sum = new int[4];
            int[] max = new int[4];
            int[] min = new int[4];
            // initialize min with large number
            for (int k = 0; k < 4; k++) min[k] = Integer.MAX_VALUE;

            for (int j = i; j < n; j++) {
                int[] e = EXP[nums[j]];   // <<< correction

                for (int k = 0; k < 4; k++) {
                    sum[k] += e[k];
                    if (e[k] > max[k]) max[k] = e[k];
                    if (e[k] < min[k]) min[k] = e[k];
                }

                boolean ok = true;
                for (int k = 0; k < 4; k++) {
                    if (sum[k] != max[k] + min[k]) {
                        ok = false;
                        break;
                    }
                }
                if (ok) best = Math.max(best, j - i + 1);
            }
        }
        return best;
    }
}