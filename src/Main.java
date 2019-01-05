import java.util.ArrayList;
        import java.util.Scanner;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList n_cv = new ArrayList();
        ArrayList k_cv = new ArrayList();
        System.out.println("输入n k:");
        int n = sc.nextInt();
        int k = sc.nextInt();
        n_cv.add(-1);
        k_cv.add(-1);
        System.out.println("输入n个特征值:");
        for (int i = 0; i < n; i++) {
            double p, s, f, d;
            p = sc.nextDouble();
            f = sc.nextDouble();
            s = sc.nextDouble();
            d = sc.nextDouble();
            n_cv.add(new Video().new characteristic_value(p, f, s, d));
        }
        System.out.println("输入k个特征值:");
        for (int i = 0; i < k; i++) {
            double p, s, f, d;
            p = sc.nextDouble();
            f = sc.nextDouble();
            s = sc.nextDouble();
            d = sc.nextDouble();
            k_cv.add(new Video().new characteristic_value(p, f, s, d));
        }
        //System.out.println("size:" + n_cv.size() + " " + k_cv.size());
        Video video = new Video(n, k, n_cv, k_cv);
        MCMF mcmf = new MCMF();
        int ret = mcmf.mcmf(n, k, n_cv, k_cv);
        //System.out.println(ret);
        mcmf.answer();
    }
}

/*
1 1 1 1
0.1 0.3 0.5 0.6
0.387 0.262 0.632 0.333

0.1 0.2 0.3 0.4
0.4 0.3 0.2 0.1
0.999 0.11 0.1 0.77
0 0 0 0
1 1 1 1
**/
