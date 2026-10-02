class Solution {
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();

        backtrack("", 0, 0, n, ans);

        return ans;
    }

    private void backtrack(String s, int open, int close, int n,
                           List<String> ans) {

        if (s.length() == 2 * n) {
            ans.add(s);
            return;
        }

        // Add '(' if we still have opening brackets available.
        if (open < n) {
            backtrack(s + "(", open + 1, close, n, ans);
        }

        // Add ')' only when it can match an existing '('.
        if (close < open) {
            backtrack(s + ")", open, close + 1, n, ans);
        }
    }
}