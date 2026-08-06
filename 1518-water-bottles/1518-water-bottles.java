class Solution {
    public int numWaterBottles(int numBottles, int numExchange) {
        int ans = numBottles;
        while(numBottles >= numExchange){
            int change = numBottles / numExchange;
            int remBot = numBottles % numExchange;
            ans += change;
            numBottles = change + remBot;
        }
        return ans;
    }
}