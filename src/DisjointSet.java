public class DisjointSet {
    private int[] p, sz;
    private int count;
    public DisjointSet(int n) {
        p = new int[n]; sz = new int[n]; count = n;
        for(int i=0; i<n; i++) { p[i] = i; sz[i] = 1; }
    }
    public int find(int x) {
        if(p[x] == x) return x;
        return p[x] = find(p[x]);
    }
    public boolean union(int a, int b) {
        int rootA = find(a), rootB = find(b);
        if(rootA == rootB) return false;
        if(sz[rootA] < sz[rootB]) { p[rootA] = rootB; sz[rootB] += sz[rootA]; }
        else { p[rootB] = rootA; sz[rootA] += sz[rootB]; }
        count--;
        return true;
    }
    public boolean connected(int a, int b) { return find(a) == find(b); }
    public int count() { return count; }
}