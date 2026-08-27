class Solution {
    public String lexGreaterPermutation(String s, String target) {

        int[] freq = new int[26];

        for (char ch : s.toCharArray()) {
            freq[ch - 'a']++;
        }

        StringBuilder ans = new StringBuilder();

        if (dfs(0, false, target, freq, ans)) {
            return ans.toString();
        }

        return "";
    }

    private boolean dfs(int idx, boolean greater, String target,
                        int[] freq, StringBuilder ans) {

        if (idx == target.length()) {
            return greater;
        }

        if (greater) {
            fillRemaining(freq, ans);
            return true;
        }

        int need = target.charAt(idx) - 'a';

        for (int c = need; c < 26; c++) {

            if (freq[c] == 0) continue;

            freq[c]--;
            ans.append((char) ('a' + c));

            if (c > need) {
                fillRemaining(freq, ans);
                return true;
            }

            if (dfs(idx + 1, false, target, freq, ans)) {
                return true;
            }

            ans.deleteCharAt(ans.length() - 1);
            freq[c]++;
        }

        return false;
    }

    private void fillRemaining(int[] freq, StringBuilder ans) {

        for (int c = 0; c < 26; c++) {
            while (freq[c] > 0) {
                ans.append((char) ('a' + c));
                freq[c]--;
            }
        }
    }
}