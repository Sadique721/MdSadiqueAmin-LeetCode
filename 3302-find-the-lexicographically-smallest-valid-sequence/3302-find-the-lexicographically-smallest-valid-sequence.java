class Solution {
    public int[] validSequence(String word1, String word2) {

        int n = word1.length();
        int m = word2.length();

        /*
         * exact[j] = latest index in word1 from which
         * word2[j...] can be matched exactly.
         */
        int[] exact = new int[m + 1];
        exact[m] = n;

        int p = n - 1;

        for (int j = m - 1; j >= 0; j--) {

            while (p >= 0 && word1.charAt(p) != word2.charAt(j)) {
                p--;
            }

            if (p < 0) {
                break;
            }

            exact[j] = p;
            p--;
        }

        /*
         * Previous occurrence of the same character.
         */
        int[] prevSame = new int[n];
        int[] last = new int[26];

        java.util.Arrays.fill(last, -1);

        for (int i = 0; i < n; i++) {
            int c = word1.charAt(i) - 'a';

            prevSame[i] = last[c];
            last[c] = i;
        }

        /*
         * runStart[i] = beginning of the consecutive run
         * of word1.charAt(i).
         */
        int[] runStart = new int[n];

        for (int i = 0; i < n; i++) {
            if (i > 0 && word1.charAt(i) == word1.charAt(i - 1)) {
                runStart[i] = runStart[i - 1];
            } else {
                runStart[i] = i;
            }
        }

        /*
         * one[j] = latest index in word1 from which
         * word2[j...] can be matched with at most one mismatch.
         */
        int[] one = new int[m + 1];
        one[m] = n;

        int[] lastBefore = last.clone();

        int bound = n;

        for (int j = m - 1; j >= 0; j--) {

            int target = word2.charAt(j) - 'a';

            int best = -1;

            /*
             * Case 1:
             * Current character matches.
             * The remaining suffix may contain the mismatch.
             */
            if (bound > 0) {
                best = lastBefore[target];
            }

            /*
             * Case 2:
             * Current character is the one mismatch.
             * Remaining suffix must match exactly.
             */
            int exactBound = exact[j + 1];

            if (exactBound > 0) {

                int x = exactBound - 1;

                int candidate;

                if (word1.charAt(x) != word2.charAt(j)) {
                    candidate = x;
                } else {
                    candidate = runStart[x] - 1;
                }

                best = Math.max(best, candidate);
            }

            one[j] = best;

            /*
             * Move the boundary left so lastBefore represents
             * positions strictly before one[j].
             */
            int newBound = Math.max(best, 0);

            for (int i = bound - 1; i >= newBound; i--) {
                int c = word1.charAt(i) - 'a';
                lastBefore[c] = prevSame[i];
            }

            bound = newBound;
        }

        /*
         * Greedily construct lexicographically smallest indices.
         */
        int[] answer = new int[m];

        int previous = -1;
        boolean mismatchUsed = false;

        for (int j = 0; j < m; j++) {

            boolean found = false;

            for (int i = previous + 1; i < n; i++) {

                boolean same =
                    word1.charAt(i) == word2.charAt(j);

                boolean possible;

                if (same) {

                    /*
                     * If mismatch is already used,
                     * suffix must be exact.
                     *
                     * Otherwise suffix may use one mismatch.
                     */
                    int limit = mismatchUsed
                            ? exact[j + 1]
                            : one[j + 1];

                    possible = limit > i;

                } else {

                    /*
                     * Current character consumes the
                     * single allowed mismatch.
                     */
                    possible =
                        !mismatchUsed &&
                        exact[j + 1] > i;
                }

                if (possible) {

                    answer[j] = i;
                    previous = i;

                    if (!same) {
                        mismatchUsed = true;
                    }

                    found = true;
                    break;
                }
            }

            if (!found) {
                return new int[0];
            }
        }

        return answer;
    }
}