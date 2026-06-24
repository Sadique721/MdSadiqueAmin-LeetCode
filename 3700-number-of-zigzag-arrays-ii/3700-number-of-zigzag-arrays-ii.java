class Solution {

    static final long MOD = 1_000_000_007L;

    public int zigZagArrays(int n, int l, int r) {

        int m = r - l + 1;
        int size = 2 * m;

        long[][] T = new long[size][size];

        // UP states: 0..m-1
        // DOWN states: m..2m-1

        for (int x = 0; x < m; x++) {

            // UP(x) -> DOWN(y), y>x
            for (int y = x + 1; y < m; y++) {
                T[x][m + y] = 1;
            }

            // DOWN(x) -> UP(y), y<x
            for (int y = 0; y < x; y++) {
                T[m + x][y] = 1;
            }
        }

        long[][] power = matrixPower(T, n - 1);

        long[] init = new long[size];

        for (int i = 0; i < size; i++) {
            init[i] = 1;
        }

        long[] result = multiply(power, init);

        long ans = 0;

        for (long v : result) {
            ans = (ans + v) % MOD;
        }

        return (int) ans;
    }

    private long[][] matrixPower(long[][] mat, long exp) {

        int n = mat.length;

        long[][] res = new long[n][n];

        for (int i = 0; i < n; i++) {
            res[i][i] = 1;
        }

        while (exp > 0) {

            if ((exp & 1) == 1) {
                res = multiply(res, mat);
            }

            mat = multiply(mat, mat);

            exp >>= 1;
        }

        return res;
    }

    private long[][] multiply(long[][] A, long[][] B) {

        int n = A.length;

        long[][] C = new long[n][n];

        for (int i = 0; i < n; i++) {

            for (int k = 0; k < n; k++) {

                if (A[i][k] == 0) continue;

                long val = A[i][k];

                for (int j = 0; j < n; j++) {

                    if (B[k][j] == 0) continue;

                    C[i][j] =
                        (C[i][j] + val * B[k][j]) % MOD;
                }
            }
        }

        return C;
    }

    private long[] multiply(long[][] A, long[] v) {

        int n = A.length;

        long[] res = new long[n];

        for (int i = 0; i < n; i++) {

            long sum = 0;

            for (int j = 0; j < n; j++) {

                sum = (sum + A[i][j] * v[j]) % MOD;
            }

            res[i] = sum;
        }

        return res;
    }
}