class Solution {
    public int maximumSaleItems(int[][] items, int budget) {
        int n = items.length;
        int[] freeCopies = new int[n];

        // Har item khareedne par milne wali free copies.
        for (int i = 0; i < n; i++) {
            int factor = items[i][0];

            for (int j = 0; j < n; j++) {
                if (i != j && items[j][0] % factor == 0) {
                    freeCopies[i]++;
                }
            }
        }

        // dp[b] = exactly b kharch karke milne wali maximum copies.
        int NEG = -1_000_000_000;
        int[] dp = new int[budget + 1];
        java.util.Arrays.fill(dp, NEG);
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            int price = items[i][1];
            int reward = freeCopies[i];

            // Is item ke transition mein sirf pichhle items ke states use hon.
            int[] old = dp.clone();

            // Item ko skip karne ka option.
            int[] next = old.clone();

            for (int r = 0; r < price && r <= budget; r++) {
                int best = NEG;

                for (int b = r; b <= budget; b += price) {
                    int t = (b - r) / price;

                    // Current b ko destination maan kar, pehle ke states se
                    // kam se kam ek copy khareedne ka best result dekhein.
                    if (best != NEG) {
                        next[b] = Math.max(next[b], reward + t + best);
                    }

                    // Current state ko baad ke destinations ke liye source banayein.
                    if (old[b] != NEG) {
                        best = Math.max(best, old[b] - t);
                    }
                }
            }

            dp = next;
        }

        int answer = 0;
        for (int copies : dp) {
            answer = Math.max(answer, copies);
        }
        return answer;
    }
}