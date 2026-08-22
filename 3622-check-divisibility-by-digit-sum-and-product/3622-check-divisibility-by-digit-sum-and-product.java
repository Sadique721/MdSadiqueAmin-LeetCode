class Solution {
    public boolean checkDivisibility(int n) {

        int num = n;
        int sum = 0;
        int product = 1;

        while (num > 0) {
            int digit = num % 10;
            sum += digit;
            product *= digit;
            num /= 10;
        }

        return n % (sum + product) == 0;
    }
}