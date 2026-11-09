import java.util.*;

class Solution {
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
        TreeSet<Integer> available = new TreeSet<>();

        // Min-heap: [finishTime, serverId]
        PriorityQueue<long[]> busy = new PriorityQueue<>(
            (a, b) -> Long.compare(a[0], b[0])
        );

        int[] count = new int[k];

        for (int i = 0; i < k; i++) {
            available.add(i);
        }

        int maxRequests = 0;

        for (int i = 0; i < arrival.length; i++) {
            // Release servers whose requests have completed
            while (!busy.isEmpty() && busy.peek()[0] <= arrival[i]) {
                long[] server = busy.poll();
                available.add((int) server[1]);
            }

            // Drop request if all servers are busy
            if (available.isEmpty()) {
                continue;
            }

            int preferred = i % k;

            // Find next available server, with wraparound
            Integer server = available.ceiling(preferred);

            if (server == null) {
                server = available.first();
            }

            available.remove(server);

            count[server]++;
            maxRequests = Math.max(maxRequests, count[server]);

            busy.offer(new long[]{
                (long) arrival[i] + load[i],
                server
            });
        }

        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            if (count[i] == maxRequests) {
                result.add(i);
            }
        }

        return result;
    }
}