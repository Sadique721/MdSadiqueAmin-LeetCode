class Solution {
    public int largestOverlap(int[][] img1, int[][] img2) {

        List<int[]> one1 = new ArrayList<>();
        List<int[]> one2 = new ArrayList<>();

        int n = img1.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (img1[i][j] == 1) one1.add(new int[]{i, j});
                if (img2[i][j] == 1) one2.add(new int[]{i, j});
            }
        }

        HashMap<Long, Integer> map = new HashMap<>();
        int ans = 0;

        for (int[] a : one1) {
            for (int[] b : one2) {

                int dx = b[0] - a[0];
                int dy = b[1] - a[1];

                long key = (((long) dx) << 32) | (dy & 0xffffffffL);

                int cnt = map.getOrDefault(key, 0) + 1;
                map.put(key, cnt);

                ans = Math.max(ans, cnt);
            }
        }

        return ans;
    }
}