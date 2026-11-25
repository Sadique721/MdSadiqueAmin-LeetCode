
import java.util.*;

class Solution {

    public int smallestUniqueSubarray(int[] nums) {

        int n = nums.length;

        int[] sa = buildSuffixArray(nums);
        int[] rank = new int[n];
        int[] lcp = new int[n];

        // Build inverse suffix ranks
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        // Kasai's algorithm
        int k = 0;

        for (int i = 0; i < n; i++) {

            int r = rank[i];

            if (r == n - 1) {
                k = 0;
                continue;
            }

            int j = sa[r + 1];

            while (i + k < n &&
                   j + k < n &&
                   nums[i + k] == nums[j + k]) {
                k++;
            }

            lcp[r] = k;

            if (k > 0) {
                k--;
            }
        }

        // Find minimum unique subarray length
        int answer = n;

        for (int r = 0; r < n; r++) {

            int previousLcp = (r > 0) ? lcp[r - 1] : 0;
            int nextLcp = (r < n - 1) ? lcp[r] : 0;

            int maxLcp = Math.max(previousLcp, nextLcp);

            int uniqueLength = maxLcp + 1;

            int start = sa[r];
            int remaining = n - start;

            if (uniqueLength <= remaining) {
                answer = Math.min(answer, uniqueLength);
            }
        }

        return answer;
    }

    // Suffix Array using doubling + counting sort
    private int[] buildSuffixArray(int[] nums) {

        int n = nums.length;
        int N = n + 1;

        int[] a = new int[N];

        for (int i = 0; i < n; i++) {
            a[i] = nums[i] + 1;
        }

        // Unique smallest sentinel
        a[n] = 0;

        int[] p = new int[N];
        int[] c = new int[N];

        int[] cnt = new int[Math.max(N, 100002)];

        // Initial counting sort
        for (int x : a) {
            cnt[x]++;
        }

        for (int i = 1; i < cnt.length; i++) {
            cnt[i] += cnt[i - 1];
        }

        for (int i = N - 1; i >= 0; i--) {
            p[--cnt[a[i]]] = i;
        }

        // Initial equivalence classes
        int classes = 1;
        c[p[0]] = 0;

        for (int i = 1; i < N; i++) {

            if (a[p[i]] != a[p[i - 1]]) {
                classes++;
            }

            c[p[i]] = classes - 1;
        }

        int[] pn = new int[N];
        int[] cn = new int[N];

        // Doubling algorithm
        for (int len = 1; len < N; len <<= 1) {

            for (int i = 0; i < N; i++) {

                pn[i] = p[i] - len;

                if (pn[i] < 0) {
                    pn[i] += N;
                }
            }

            Arrays.fill(cnt, 0, classes, 0);

            for (int i = 0; i < N; i++) {
                cnt[c[pn[i]]]++;
            }

            for (int i = 1; i < classes; i++) {
                cnt[i] += cnt[i - 1];
            }

            for (int i = N - 1; i >= 0; i--) {
                int cur = pn[i];
                p[--cnt[c[cur]]] = cur;
            }

            cn[p[0]] = 0;

            int newClasses = 1;

            for (int i = 1; i < N; i++) {

                int cur = p[i];
                int prev = p[i - 1];

                int midCur = (cur + len) % N;
                int midPrev = (prev + len) % N;

                if (c[cur] != c[prev] ||
                    c[midCur] != c[midPrev]) {

                    newClasses++;
                }

                cn[cur] = newClasses - 1;
            }

            int[] temp = c;
            c = cn;
            cn = temp;

            classes = newClasses;

            if (classes == N) {
                break;
            }
        }

        // Remove sentinel suffix
        int[] sa = new int[n];

        System.arraycopy(p, 1, sa, 0, n);

        return sa;
    }
}
