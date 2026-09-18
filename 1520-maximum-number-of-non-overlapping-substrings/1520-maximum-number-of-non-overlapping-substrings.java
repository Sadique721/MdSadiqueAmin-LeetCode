class Solution {
    public List<String> maxNumOfSubstrings(String s) {
        int n = s.length();
        int[] first = new int[26];
        int[] last = new int[26];

        Arrays.fill(first, n);

        for (int i = 0; i < n; i++) {
            int c = s.charAt(i) - 'a';
            first[c] = Math.min(first[c], i);
            last[c] = i;
        }

        List<String> ans = new ArrayList<>();
        int end = -1;

        for (int i = 0; i < n; i++) {
            if (i != first[s.charAt(i) - 'a']) continue;

            int right = expand(s, i, first, last);
            if (right == -1) continue;

            if (i > end) {
                ans.add(s.substring(i, right + 1));
            } else {
                ans.set(ans.size() - 1, s.substring(i, right + 1));
            }
            end = right;
        }

        return ans;
    }

    private int expand(String s, int start, int[] first, int[] last) {
        int right = last[s.charAt(start) - 'a'];

        for (int i = start; i <= right; i++) {
            int c = s.charAt(i) - 'a';

            if (first[c] < start) return -1;
            right = Math.max(right, last[c]);
        }

        return right;
    }
}