class Solution {
    public boolean checkString(String s) {
        boolean seenB = false;              // ab tak b dekh liya ?
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == 'a') {
                // agar ek 'a' milta hai aur pehle koi 'b' bhi mila, to badh
                if (seenB) return false;
            } else if (ch == 'b') {
                seenB = true;               // ek 'b' dekh liya
            }
        }
        return true;                        // koi “ba” nahi mila
    }
}