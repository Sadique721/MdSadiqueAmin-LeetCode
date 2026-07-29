class Solution {
    public boolean isPalindrome(int x) {
        int n = x;
        if(x < 0){
            return false;
        }
        int revNum = 0;
        while(n != 0){
            revNum = (revNum * 10 + (n % 10));
            n /= 10;
        }
        if(revNum == x){
            return true;
        }else{
            return false;
        }
    }
}