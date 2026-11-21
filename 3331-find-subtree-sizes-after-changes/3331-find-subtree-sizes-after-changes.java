import java.util.*;

class Solution {
    public int[] findSubtreeSizes(int[] parent, String s) {
        int n = parent.length;

        ArrayList<Integer>[] children = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            children[i] = new ArrayList<>();
        }

        for (int i = 1; i < n; i++) {
            children[parent[i]].add(i);
        }

        int[] newParent = parent.clone();
        int[] last = new int[26];
        int[] oldLast = new int[n];
        Arrays.fill(last, -1);

        // Iterative DFS on the original tree
        int[] stack = new int[2 * n + 5];
        int top = 0;
        stack[top++] = 0;

        ArrayList<Integer> order = new ArrayList<>(n);

        while (top > 0) {
            int entry = stack[--top];

            if (entry >= 0) {
                int node = entry;
                int c = s.charAt(node) - 'a';

                oldLast[node] = last[c];

                // Find the nearest matching ancestor
                if (last[c] != -1) {
                    newParent[node] = last[c];
                }

                last[c] = node;
                order.add(node);

                // Exit marker
                stack[top++] = -node - 1;

                // Visit original children
                for (int i = children[node].size() - 1; i >= 0; i--) {
                    stack[top++] = children[node].get(i);
                }

            } else {
                int node = -entry - 1;
                int c = s.charAt(node) - 'a';

                // Restore ancestor state when leaving this node
                last[c] = oldLast[node];
            }
        }

        // Calculate subtree sizes in the modified tree
        int[] ans = new int[n];
        Arrays.fill(ans, 1);

        for (int i = n - 1; i > 0; i--) {
            int node = order.get(i);
            ans[newParent[node]] += ans[node];
        }

        return ans;
    }
}