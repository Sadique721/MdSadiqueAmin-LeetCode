import java.util.HashSet;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        HashSet<Integer> seen = new HashSet<>();
        for (int num : nums) {
            // If the set already contains the number, we found a duplicate
            if (seen.contains(num)) {
                return true; 
            }
            // Otherwise, add the number to the set
            seen.add(num);
        }
        // If we finish the loop, no duplicates were found
        return false;
    }
}