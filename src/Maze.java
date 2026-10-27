import java.util.*;
public class Maze {
    public static int[] breakWalls(Input.Walls w) {
        DisjointSet d = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int idx = 0;
        for(int i = 0; i < w.count(); i++) {
            if(d.union(w.a()[i], w.b()[i])) {
                broken[idx++] = i;
                if(idx == broken.length) break;
            }
        }
        return Arrays.copyOf(broken, idx);
    }
    public static int[] cheapestMaze(Input.Walls w) {
        Integer[] order = new Integer[w.count()];
        for(int i = 0; i < w.count(); i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> w.cost()[i]));
        DisjointSet d = new DisjointSet(w.cells());
        int[] broken = new int[w.cells() - 1];
        int idx = 0;
        for(int i : order) {
            if(d.union(w.a()[i], w.b()[i])) {
                broken[idx++] = i;
                if(idx == broken.length) break;
            }
        }
        return Arrays.copyOf(broken, idx);
    }
    public static long primCost(Input.Walls w) {
        int n = w.cells();
        int[] head = new int[n]; Arrays.fill(head, -1);
        int[] to = new int[w.count() * 2];
        int[] cost = new int[w.count() * 2];
        int[] next = new int[w.count() * 2];
        int edgeCnt = 0;
        for(int i = 0; i < w.count(); i++) {
            int u = w.a()[i], v = w.b()[i], c = w.cost()[i];
            to[edgeCnt] = v; cost[edgeCnt] = c; next[edgeCnt] = head[u]; head[u] = edgeCnt++;
            to[edgeCnt] = u; cost[edgeCnt] = c; next[edgeCnt] = head[v]; head[v] = edgeCnt++;
        }
        boolean[] inTree = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        long totalCost = 0;
        int connected = 0;
        pq.add(new int[]{0, 0});
        while(!pq.isEmpty() && connected < n) {
            int[] curr = pq.poll();
            int u = curr[0];
            if(inTree[u]) continue;
            inTree[u] = true;
            totalCost += curr[1];
            connected++;
            for(int e = head[u]; e != -1; e = next[e]) {
                if(!inTree[to[e]]) {
                    pq.add(new int[]{to[e], cost[e]});
                }
            }
        }
        return totalCost;
    }
}