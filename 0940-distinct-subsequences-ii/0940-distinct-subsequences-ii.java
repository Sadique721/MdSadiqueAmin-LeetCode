class Solution {
    public int distinctSubseqII(String s) {

        int MOD = 1_000_000_007;

        long total = 1;
        long[] last = new long[26];

        for (char ch : s.toCharArray()) {

            long newTotal = (2 * total - last[ch - 'a'] + MOD) % MOD;

            last[ch - 'a'] = total;
            total = newTotal;
        }

        return (int) ((total - 1 + MOD) % MOD);
    }
}