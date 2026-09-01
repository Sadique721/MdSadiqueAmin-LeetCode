class Solution {
    public int minMoves(String[] classroom, int energy) {

        int m = classroom.length;
        int n = classroom[0].length();

        int sr = 0, sc = 0;
        int[][] id = new int[m][n];

        for (int[] row : id) Arrays.fill(row, -1);

        int cnt = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                char ch = classroom[i].charAt(j);

                if (ch == 'S') {
                    sr = i;
                    sc = j;
                } else if (ch == 'L') {
                    id[i][j] = cnt++;
                }
            }
        }

        if (cnt == 0) return 0;

        int target = (1 << cnt) - 1;

        int states = m * n * (energy + 1) * (1 << cnt);
        boolean[] vis = new boolean[states];

        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{sr, sc, energy, 0, 0});

        vis[index(sr, sc, energy, 0, n, energy, cnt)] = true;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!q.isEmpty()) {

            int[] cur = q.poll();

            int r = cur[0];
            int c = cur[1];
            int e = cur[2];
            int mask = cur[3];
            int dist = cur[4];

            if (mask == target) return dist;

            if (e == 0) continue;

            for (int k = 0; k < 4; k++) {

                int nr = r + dr[k];
                int nc = c + dc[k];

                if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;

                char cell = classroom[nr].charAt(nc);

                if (cell == 'X') continue;

                int ne = e - 1;
                int nmask = mask;

                if (cell == 'L') {
                    nmask |= (1 << id[nr][nc]);
                }

                if (cell == 'R') {
                    ne = energy;
                }

                int idx = index(nr, nc, ne, nmask, n, energy, cnt);

                if (!vis[idx]) {
                    vis[idx] = true;
                    q.offer(new int[]{nr, nc, ne, nmask, dist + 1});
                }
            }
        }

        return -1;
    }

    private int index(int r, int c, int e, int mask,
                      int cols, int energy, int litter) {

        int cell = r * cols + c;
        return (((cell * (energy + 1)) + e) << litter) | mask;
    }
}