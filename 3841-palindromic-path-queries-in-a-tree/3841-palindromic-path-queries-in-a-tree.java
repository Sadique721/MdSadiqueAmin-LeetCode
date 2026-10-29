import java.util.*;

class Solution {
    private List<Integer>[] adj;
    private int[] parent, depth, size, heavy, head, pos, value;
    private int timer;
    private int[] seg;
    private int base;

    public List<Boolean> palindromePath(int n, int[][] edges, String s, String[] queries) {
        adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        parent = new int[n];
        depth = new int[n];
        size = new int[n];
        heavy = new int[n];
        head = new int[n];
        pos = new int[n];
        value = new int[n];
        Arrays.fill(heavy, -1);

        for (int i = 0; i < n; i++) {
            value[i] = 1 << (s.charAt(i) - 'a');
        }

        dfs(0, -1);
        timer = 0;
        decompose(0, 0);

        base = 1;
        while (base < n) {
            base <<= 1;
        }

        seg = new int[base << 1];
        for (int i = 0; i < n; i++) {
            seg[base + pos[i]] = value[i];
        }

        for (int i = base - 1; i > 0; i--) {
            seg[i] = seg[i << 1] ^ seg[i << 1 | 1];
        }

        List<Boolean> answer = new ArrayList<>();

        for (String q : queries) {
            String[] parts = q.split(" ");

            if (parts[0].equals("update")) {
                int u = Integer.parseInt(parts[1]);
                char c = parts[2].charAt(0);
                value[u] = 1 << (c - 'a');
                update(pos[u], value[u]);
            } else {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);
                int mask = 0;

                while (head[u] != head[v]) {
                    if (depth[head[u]] < depth[head[v]]) {
                        int temp = u;
                        u = v;
                        v = temp;
                    }

                    mask ^= rangeQuery(pos[head[u]], pos[u]);
                    u = parent[head[u]];
                }

                if (depth[u] > depth[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }

                mask ^= rangeQuery(pos[u], pos[v]);
                answer.add((mask & (mask - 1)) == 0);
            }
        }

        return answer;
    }

    private void dfs(int u, int p) {
        parent[u] = p;
        size[u] = 1;
        int maxSize = 0;

        for (int v : adj[u]) {
            if (v == p) continue;

            depth[v] = depth[u] + 1;
            dfs(v, u);
            size[u] += size[v];

            if (size[v] > maxSize) {
                maxSize = size[v];
                heavy[u] = v;
            }
        }
    }

    private void decompose(int u, int h) {
        head[u] = h;
        pos[u] = timer++;

        if (heavy[u] != -1) {
            decompose(heavy[u], h);
        }

        for (int v : adj[u]) {
            if (v != parent[u] && v != heavy[u]) {
                decompose(v, v);
            }
        }
    }

    private void update(int index, int val) {
        int i = base + index;
        seg[i] = val;

        for (i >>= 1; i > 0; i >>= 1) {
            seg[i] = seg[i << 1] ^ seg[i << 1 | 1];
        }
    }

    private int rangeQuery(int left, int right) {
        int result = 0;
        left += base;
        right += base;

        while (left <= right) {
            if ((left & 1) == 1) result ^= seg[left++];
            if ((right & 1) == 0) result ^= seg[right--];

            left >>= 1;
            right >>= 1;
        }

        return result;
    }
}