import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zjkgf on 2017/8/15.
 */
public class TestThread {
    public static final int MaxThreadNum = 120;
    public static ExecutorService pool = Executors.newFixedThreadPool(MaxThreadNum);
    public static final Map<Integer, Integer> c = new ConcurrentHashMap<>();
    public static int[] a = new int[2000];
    public static int[] b = new int[2000];
    public static Lock lock = new ReentrantLock();
    public static void main(String[] args){
        class tempthread extends Thread{
            int num;
            public tempthread(int num){
                this.num = num;
            }
            @Override
            public void run() {
                synchronized (c) {
                    for (int element : c.keySet()) {
                        int k = c.get(element);
                        if (k < 20) {
                            a[num] = element;
                            b[num] = k;
                            c.replace(element, k + 1);
                            return;
                        }
                    }
                }
            }
        }

        for (int i=0;i<100;i++)
            c.put(i, 0);
        for (int i=0;i<2000;i++){
            pool.execute(new tempthread(i));
        }
        pool.shutdown();
        while (!pool.isTerminated())
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int element:c.keySet()){
            System.out.println(String.valueOf(element) + " " + String.valueOf(c.get(element)));
        }
        for (int i=0;i<2000;i++){
            System.out.println(String.valueOf(a[i])+" "+String.valueOf(b[i]));
        }
    }
}
