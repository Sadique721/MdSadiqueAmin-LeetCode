class Solution {
    public int totalNumbers(int[] digits) {
        boolean[] seen = new boolean[1000];
        int n = digits.length;
        int count = 0;

        for (int i = 0; i < n; i++) {
            if (digits[i] == 0) continue; // No leading zero

            for (int j = 0; j < n; j++) {
                if (j == i) continue;

                for (int k = 0; k < n; k++) {
                    if (k == i || k == j) continue;
                    if ((digits[k] & 1) == 1) continue; // Last digit must be even

                    int num = digits[i] * 100 + digits[j] * 10 + digits[k];

                    if (!seen[num]) {
                        seen[num] = true;
                        count++;
                    }
                }
            }
        }

        return count;
    }
}