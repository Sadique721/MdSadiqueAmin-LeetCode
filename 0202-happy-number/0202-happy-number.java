import java.util.*;

class Solution {

    private int nextNumber(int n) {
        int sum = 0;

        while (n > 0) {
            int digit = n % 10;
            sum += digit * digit;
            n /= 10;
        }

        return sum;
    }

    public boolean isHappy(int n) {

        HashSet<Integer> seen = new HashSet<>();

        while (n != 1 && !seen.contains(n)) {
            seen.add(n);
            n = nextNumber(n);
        }

        return n == 1;
    }
}