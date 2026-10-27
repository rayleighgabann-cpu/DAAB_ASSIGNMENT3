import java.util.*;
public class Escape {
    public record Result(int[] path, int visited, int deadEnds) {}
    public static Result escape(Input.Walls w, int[] broken) {
        int n = w.cells();
        int cols = w.cols();
        int[][] open = Graphs.open(w, broken);

        int deadEnds = 0;
        for(int i = 0; i < n; i++) if(open[i].length == 1) deadEnds++;

        int[] stack = new int[n];
        int[] edgeIdx = new int[n];
        int top = 0;
        stack[0] = 0;

        boolean[] visited = new boolean[n];
        visited[0] = true;
        int visitedCount = 1;

        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        boolean found = (n == 1);
        int visitedAtExit = found ? 1 : 0;

        int[] dirs = {-cols, 1, cols, -1};

        while(top >= 0 && !found) {
            int u = stack[top];
            if(edgeIdx[top] < 4) {
                int d = edgeIdx[top]++;
                int target = u + dirs[d];

                boolean isValid = false;
                if(d == 0 && u >= cols) isValid = true;
                else if(d == 1 && u % cols != cols - 1) isValid = true;
                else if(d == 2 && u < n - cols) isValid = true;
                else if(d == 3 && u % cols != 0) isValid = true;

                if(isValid) {
                    boolean isOpen = false;
                    for(int v : open[u]) if(v == target) { isOpen = true; break; }

                    if(isOpen && !visited[target]) {
                        visited[target] = true;
                        visitedCount++;
                        parent[target] = u;
                        if(target == n - 1) {
                            found = true;
                            visitedAtExit = visitedCount;
                            break;
                        }
                        top++;
                        stack[top] = target;
                        edgeIdx[top] = 0;
                    }
                }
            } else {
                top--;
            }
        }

        int curr = n - 1;
        int pathLen = 0;
        while(curr != -1) { pathLen++; curr = parent[curr]; }
        int[] path = new int[pathLen];
        curr = n - 1;
        for(int i = pathLen - 1; i >= 0; i--) { path[i] = curr; curr = parent[curr]; }

        return new Result(path, visitedAtExit, deadEnds);
    }
}