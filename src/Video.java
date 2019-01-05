import java.util.ArrayList;
import static java.lang.Math.pow;

//视频片段类
public class Video {
    public static final int characteristic_value_len = 4;
    public int n;
    public int k;
    public  class characteristic_value {
        double p;
        double f;
        double s;
        double d;

        public characteristic_value(double p, double f, double s, double d) {
            this.p = p;
            this.f = f;
            this.d = d;
            this.s = s;
        }
    }

    //下标从1开始
    public ArrayList<characteristic_value> n_cv;
    public ArrayList<characteristic_value> k_cv;

    @SuppressWarnings("unchecked")
    Video(int n, int k, ArrayList n_cv, ArrayList k_cv) {
        this.n = n;
        this.k = k;
        this.n_cv = n_cv;
        this.k_cv = k_cv;
    }

    Video() {}

    public int cal(int a, int b) {
        characteristic_value node_a = n_cv.get(a);
        characteristic_value node_b = k_cv.get(b);
       // System.out.println("debug-video-cal" + node_a.s + " " + node_b.s);
        double p = pow(node_a.p - node_b.p, 2);
        double f = pow(node_a.f - node_b.f, 2);
        double s = pow(node_a.s - node_b.s, 2);
        double d = pow(node_a.d - node_b.d, 2);

        double dis = pow(p + s + d + f, 0.5);
        dis *= 1000;
        return (int)dis;
    }
}
