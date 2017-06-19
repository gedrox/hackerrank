package ballsAndBoxes;

import java.util.Scanner;

public class RealSolution {

    static final int maxn = 405;
    static node[] e = new node[maxn * maxn];
    static int[] finl = new int[maxn],
            in = new int[maxn], A = new int[maxn];
    static int[][] B = new int[maxn][maxn];
    static int[] C = new int[maxn], d = new int[maxn];
    static int tot = 0;
    static int n, m, s, t, ans;

    public static void main(String... args) {
        tot = 1;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        for (int i = 1; i <= n; i++) A[i] = sc.nextInt();
        for (int i = 1; i <= m; i++) C[i] = sc.nextInt();
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++) B[i][j] = sc.nextInt();
        long t0 = System.currentTimeMillis();
        s = 0;
        t = n + m + 1;
        for (int i = 1; i <= n; i++) link(s, i, A[i], 0);
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= m; j++)
                link(i, n + j, 1, B[i][j]);
        for (int i = 1; i <= m; i++) {
            link(i + n, t, C[i], 0);
            link(i + n, t, 1, -1);
            for (int j = C[i] + 1; j < n; j++)
                link(i + n, t, 1, -2 * (j - C[i]) - 1);
        }
        min_cost();
        System.out.println(ans);
        System.err.println(System.currentTimeMillis() - t0);
    }

    static void link(int u, int v, int f, int c) {
        e[++tot] = new node(v, finl[u], f, c);
        finl[u] = tot;
        e[++tot] = new node(u, finl[v], 0, -c);
        finl[v] = tot;
    }

    static void min_cost() {
        int c = 0, c2 = 0;
        long s1 = 0, s2 = 0;
        for (; ; ) {
            c++;
            long t0 = System.currentTimeMillis();
            int[] que = new int[maxn * maxn];
            for (int i = s; i <= t; i++) {
                d[i] = -(1 << 30);
                in[i] = 0;
            }
            d[s] = 0;
            que[1] = s;
            int fi = 1, en = 1;
            for (; fi <= en; fi++) {
                int u = que[fi];
                for (int i = finl[u]; i > 0; i = e[i].next)
                    if (e[i].flow > 0 && d[u] + e[i].cost > d[e[i].to]) {
                        d[e[i].to] = d[u] + e[i].cost;
                        if (in[e[i].to] == 0) {
                            in[e[i].to] = 1;
                            que[++en] = e[i].to;
                        }
                    }
                in[u] = 0;
            }
            s1 += System.currentTimeMillis() - t0;
            if (d[t] <= 0) break;
            
            t0 = System.currentTimeMillis();
            dfs(s, 1 << 30);
            s2 += System.currentTimeMillis() - t0;
        }

        System.err.println(c);
        System.err.println(s1);
        System.err.println(s2);
    }

    static int dfs(int now, int flow) {
        if (now == t) {
            ans += flow * d[now];
            return flow;
        }
        int use = 0;
        in[now] = 1;
        for (int i = finl[now]; i > 0; i = e[i].next)
            if (in[e[i].to] == 0 && d[e[i].to] == d[now] + e[i].cost && e[i].flow > 0) {
                int tmp = dfs(e[i].to, Math.min(flow - use, e[i].flow));
                use += tmp;
                e[i].flow -= tmp;
                e[i ^ 1].flow += tmp;
                if (use == flow) return use;
            }
        return use;
    }

    static class node {
        int to, next, flow, cost;


        public node(int a, int b, int c, int d) {
            to = a;
            next = b;
            flow = c;
            cost = d;
        }
    }
}
