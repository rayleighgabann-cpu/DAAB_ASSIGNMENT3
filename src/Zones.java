import java.util.*;
public class Zones {
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] from = Graphs.slidesFrom(s);
        int[][] into = Graphs.slidesInto(s);

        int[] order = new int[n];
        int orderCnt = 0;
        boolean[] vis = new boolean[n];
        int[] stack = new int[n];
        int[] edgeIdx = new int[n];

        for(int i = 0; i < n; i++) {
            if(!vis[i]) {
                int top = 0;
                stack[0] = i;
                vis[i] = true;
                while(top >= 0) {
                    int u = stack[top];
                    if(edgeIdx[top] < from[u].length) {
                        int edge = from[u][edgeIdx[top]++];
                        int v = s.to()[edge];
                        if(!vis[v]) {
                            vis[v] = true;
                            top++;
                            stack[top] = v;
                            edgeIdx[top] = 0;
                        }
                    } else {
                        order[orderCnt++] = u;
                        top--;
                    }
                }
            }
        }

        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zoneId = 0;
        for(int i = n - 1; i >= 0; i--) {
            int start = order[i];
            if(zoneOf[start] == -1) {
                int currZone = zoneId++;
                int top = 0;
                stack[0] = start;
                zoneOf[start] = currZone;
                while(top >= 0) {
                    int u = stack[top--];
                    for(int edge : into[u]) {
                        int v = s.from()[edge];
                        if(zoneOf[v] == -1) {
                            zoneOf[v] = currZone;
                            stack[++top] = v;
                        }
                    }
                }
            }
        }
        return zoneOf;
    }

    public static long[] routes(Input.Slides s, int[] zoneOf, int zones) {
        long[][] minEdge = new long[zones][zones];
        for(int i = 0; i < zones; i++) Arrays.fill(minEdge[i], Long.MAX_VALUE);
        for(int i = 0; i < s.count(); i++) {
            int u = zoneOf[s.from()[i]];
            int v = zoneOf[s.to()[i]];
            if(u != v) {
                minEdge[u][v] = Math.min(minEdge[u][v], s.time()[i]);
            }
        }

        long[] dpStages = new long[zones];
        Arrays.fill(dpStages, 1);
        long[] dpCost = new long[zones];
        Arrays.fill(dpCost, Long.MAX_VALUE);
        dpCost[zoneOf[0]] = 0;

        long maxStages = 1;
        for(int u = 0; u < zones; u++) {
            for(int v = u + 1; v < zones; v++) {
                if(minEdge[u][v] != Long.MAX_VALUE) {
                    dpStages[v] = Math.max(dpStages[v], dpStages[u] + 1);
                    maxStages = Math.max(maxStages, dpStages[v]);
                    if(dpCost[u] != Long.MAX_VALUE) {
                        dpCost[v] = Math.min(dpCost[v], dpCost[u] + minEdge[u][v]);
                    }
                }
            }
        }

        return new long[]{maxStages, dpCost[zoneOf[s.n()-1]]};
    }
}