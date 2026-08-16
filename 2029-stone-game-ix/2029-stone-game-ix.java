class Solution {
    public boolean stoneGameIX(int[] stones) {

        int[] cnt = new int[3];

        for (int stone : stones) {
            cnt[stone % 3]++;
        }

        // Stones divisible by 3 do not change the remainder.
        // They can only be used to change the turn.
        if (cnt[0] % 2 == 0) {
            return cnt[1] > 0 && cnt[2] > 0;
        }

        return Math.abs(cnt[1] - cnt[2]) > 2;
    }
}