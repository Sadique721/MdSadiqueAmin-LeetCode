class Solution {
    public int minOperations(String s) {
        int n = s.length();

        boolean sorted = true;
        char mn = 'z', mx = 'a';

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            mn = (char) Math.min(mn, c);
            mx = (char) Math.max(mx, c);

            if (i > 0 && s.charAt(i - 1) > c) {
                sorted = false;
            }
        }

        if (sorted) {
            return 0;
        }

        if (n == 2) {
            return -1;
        }

        boolean minInProperPrefix = false;
        boolean maxInProperSuffix = false;

        for (int i = 0; i < n - 1; i++) {
            if (s.charAt(i) == mn) {
                minInProperPrefix = true;
                break;
            }
        }

        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == mx) {
                maxInProperSuffix = true;
                break;
            }
        }

        if (s.charAt(0) == mn || s.charAt(n - 1) == mx) {
            return 1;
        }

        if (!minInProperPrefix && !maxInProperSuffix) {
            return 3;
        }

        return 2;
    }
}