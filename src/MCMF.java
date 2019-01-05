import java.util.ArrayList;
//计算费用流
public class MCMF {

    //预定义的最大数据量
    public static final int MAX = 10001;

    public ArrayList<Edge>[] g; //图
    public static int node_count; //图节点数
    public static int edge_count; //图边数
    public static int[] dis = new int[MAX]; //最短路数组
    public static int[] capa = new int[MAX]; //增广路流量
    public static int[] pre = new int[MAX]; //增广路中某点的上一步
    public static boolean[] vis = new boolean[MAX]; //标记数组，用于入队判断
    public int n, k;
    public ArrayList<Video.characteristic_value> n_cv; //n个的特征值
    public ArrayList<Video.characteristic_value> k_cv; //k个的特征值


    //边信息
    public class Edge{
        public int from; //起点
        public int to; //终点
        public int cap; //容量
        public int cost; //费用

        Edge(int from, int to, int cab, int cost) {
            this.cap = cab;
            this.cost = cost;
            this.from = from;
            this.to = to;
        }
    }

    //建图函数
    @SuppressWarnings("unchecked")
    public void BuildG(int n, int k, ArrayList n_cv, ArrayList k_cv) {
        node_count = n + k + 2;
        edge_count = n + k + n * k;
        this.n_cv = n_cv;
        this.k_cv = k_cv;
        this.k = k;
        this.n = n;

       // System.out.println("debug-buildG-nk" + n + " " +  k + " "+ n_cv + " " + k_cv);

        g = new ArrayList[node_count];
        for (int i = 0; i < node_count; i++) {
            g[i] = new ArrayList<>();
        }

        //源点到k
        //System.out.println("debug-buildG-edge");
        for (int i = 1; i <= k ; i++) {
            g[0].add(new Edge(0, i, 1, 0));
            g[i].add(new Edge(i, 0, 0,0));
         //   System.out.println("k" + g[i] + " " + g[i+1]);
        }
        //System.out.println("look at me");
        //k到n
        for(int i = 1; i <= k; i++ ) {
            for (int j = k+1; j <= k + n; j++) {
                int val = Judge(i, j);
                g[i].add(new Edge(i, j, 1, val));
                g[j].add(new Edge(j, i, 0, -1 * val));
           //     System.out.println("k-n" + g[i] + " "+ g[i+1] + " " + re);
            }
        }
        //n到汇点
        for (int i = k + 1; i < k + n + 1; i++) {
            g[i].add(new Edge(i, k+n+1, 1, 0));
            g[k+n+1].add(new Edge(k+n+1, i, 0, 0));
         //   System.out.println("n" + g[i] + " "+ g[i+1]);
        }
    }

    //特征值匹配计算，采用欧氏距离计算
    public int Judge(int from, int to){
        Video video = new Video(n, k, n_cv, k_cv);
        int rate = 0;
       // System.out.println("debug-buildG-rate-ft" + from + " "+ to);
        to = to - k;
        rate = video.cal(to, from);
       // System.out.println("debug-buildG-rate" + from + " " + to + " " + rate);
        return rate;
    }

    //增广路
    public boolean Spfa(int s, int t) {

        ArrayList<Integer> q = new ArrayList<>();
        int[] route_count = new int[node_count];
        for(int i = 0; i < node_count; i++) {
            pre[i] = -1;
            vis[i] = false;
            dis[i] = Integer.MAX_VALUE;
            capa[i] = Integer.MAX_VALUE;
        }
        pre[s] = s;
        vis[s] = true;
        dis[s] = 0;
        route_count[s]++;
        q.add(s);
        while (!q.isEmpty()) {
            int node = q.get(0);
            //System.out.println(node + " " + g[node].size() );
            q.remove(0);
            vis[node] = false;
            for (int i = 0; i < g[node].size(); i++) {
                Edge temp_edge = g[node].get(i);
                //System.out.println(temp_edge.from + "" +temp_edge.to + "" + temp_edge.cost);
                if (temp_edge.cap > 0 && dis[temp_edge.to] > temp_edge.cost + dis[node]) {
                    //System.out.println(node + "" + temp_edge.to);
                    dis[temp_edge.to] = temp_edge.cost + dis[node];
                    pre[temp_edge.to] = node;
                    capa[temp_edge.to] = Math.min(capa[node], temp_edge.cap);
  //                  System.out.println("inloop" + node + "" + temp_edge.to + "" + dis[temp_edge.to] + capa[temp_edge.to]);
                    if (!vis[temp_edge.to]) {
                        vis[temp_edge.to] = true;
                        q.add(temp_edge.to);
                        route_count[temp_edge.to]++;
                        if (route_count[temp_edge.to] > node_count) {
                            return false; //有环
                        }
                    }
                }
            }
        }
//        System.out.println("ds" + dis[9] + "" + capa[9]);
        if (pre[t] != -1 && dis[t] != Integer.MAX_VALUE) {
            return true;
        }
        return false;
    }

    public int mcmf(int n, int k, ArrayList n_cv, ArrayList k_cv) {
        BuildG(n, k, n_cv, k_cv);
        int s = 0, t = node_count - 1;
        int ret = 0;
        while (true) {
            if (!Spfa(s, t)) {
                break;
            }
            ret += dis[t] * capa[t];
           // System.out.println("look at me" + dis[t] + " " + capa[t]);
            int end = t, start = t;
            while(end != s) {
                end = start;
                start = pre[end];
                //System.out.println(start);
                int i = 0, j = 0;
                //System.out.println("——>"+end);
                for(; i < g[start].size(); i++) {
                  //  System.out.println(g[start].size());
                    if(g[start].get(i).to == end)
                        break;
                }
                //System.out.println(start);
               //System.out.println("here" + g[start].get(i).to + "ℹ" + i + "size" + g[start].size());
                g[start].get(i).cap -= capa[t]; //正向边减少残量
                for(; j < g[end].size(); j++) {
                    if(g[end].get(j).to == start)
                        break;
                }
                //System.out.println(end);
                //System.out.println("end" + g[end].get(j).to + "ℹ" + j + "size" + g[end].size());
                g[end].get(j).cap += capa[t]; //反向边增加残量
               // System.out.println("ret" + ret);
                end = start;
            }
            //System.out.println("——>"+start);
        }
        return ret;
    }

    public void answer() {
        for (int i = 0; i < node_count; i++) {
            for (int j = 0; j < g[i].size(); j++) {
                if (g[i].get(j).cap < 1) {
                    if (g[i].get(j).from < 6 && g[i].get(j).to > 0 && g[i].get(j).from > 0 ) {
                        int nn = g[i].get(j).to-k;
                        System.out.println("k" + i + "->n" + nn);
                    }
                }
            }
        }
    }
}

