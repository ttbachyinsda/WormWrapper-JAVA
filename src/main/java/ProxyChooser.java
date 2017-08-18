import net.dongliu.requests.Parameter;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.javatuples.*;

import java.net.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.min;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class ProxyChooser {
    public static final int threshold = 100;
    public static final int available = 60;
    public static final int waittime = 50000;
    public static boolean WITH_PROXY = true;
    public static boolean RUN = false;
    public static Timestamp prets;
    public static final Map<String, Pair<Integer,Integer>> proxymap = new ConcurrentHashMap<>();
    public static final Map<String, Integer> waitmap = new ConcurrentHashMap<>();
    private static List<String> proxies;
    private static String[] agents ={"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0"};

    public static String[] chooseproxy(Set<String> havechoose, boolean isnowpublish)
    {
        String minn = "";
        int mint = 1000000, minf = 0;
        String[] result = new String[3];
        if (isnowpublish) {
            synchronized (proxymap) {
                for (String element : proxymap.keySet()) {
                    Pair<Integer, Integer> cc = proxymap.get(element);
                    int c0 = cc.getValue0();
                    int c1 = cc.getValue1();
                    if (c0 < mint && c1 < available && !havechoose.contains(element)) {
                        minn = element;
                        mint = c0;
                        minf = c1;
                    }
                }
                result[0] = minn.split(":")[0];
                result[1] = minn.split(":")[1];
                result[2] = minn;
            }
        } else {
            for (String element : proxymap.keySet()) {
                Pair<Integer, Integer> cc = proxymap.get(element);
                int c0 = cc.getValue0();
                int c1 = cc.getValue1();
                if (c0 < mint && !havechoose.contains(element)) {
                    minn = element;
                    mint = c0;
                    minf = c1;
                }
            }
            result[0] = minn.split(":")[0];
            result[1] = minn.split(":")[1];
            result[2] = minn;
        }
        return result;
    }
    public static String chooseagent()
    {
        Random random = new Random();
        int num = random.nextInt(agents.length);
        return agents[num];
    }
    public static void removeredundantproxy(){
        class WaitThread extends Thread{
            public String element;
            public int delay;
            public WaitThread(String element, int delay){
                this.element = element;
                this.delay = delay;
            }
            @Override
            public void run() {
                try {
                    Thread.sleep(waittime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delay = delay - 10;
                synchronized (proxymap) {
                    if (!proxymap.containsKey(element))
                        proxymap.put(element, Pair.with((int) (delay), 0));
                    else
                        System.out.println("ERROR");
                }
                waitmap.remove(element);
            }
        }
        for (String element: proxymap.keySet()){
            Pair<Integer,Integer> cc = proxymap.get(element);
            if (cc.getValue0() > 100){
                proxymap.remove(element);
            } else if (cc.getValue1() >= available) {
                waitmap.put(element,cc.getValue0());
                ThreadPool.pool3.execute(new WaitThread(element, cc.getValue0()));
                proxymap.remove(element);
            }
        }
    }
    public static void getnewproxy() {
        RUN = true;
        String index_url= "http://svip.kuaidaili.com/api/getproxy/?orderid=910263180614597&num=200&b_pcchrome=1&b_pcie=1&b_pcff=1&b_android=1&b_iphone=1&b_ipad=1&protocol=1&method=2&an_an=1&an_ha=1&sort=1&sep=1";
        List<String> newproxies = new ArrayList<>();
        try {
            RawResponse tap = Requests.get(index_url).timeout(8000).send();
            if (tap.getStatusCode() != 200)
            {
                throw new ResultErrorException("code error");
            }
            String accresult = tap.readToText();
            if (accresult.contains("5秒钟"))
            {
                RUN = false;
                return;
            } else {
                String[] lists = accresult.split("\r\n");
                for (String element:lists) {
                    Thread c = new Thread() {
                        @Override
                        public void run() {
                            if (!waitmap.containsKey(element) && !proxymap.containsKey(element)) {
                                try {
                                    long pretime = System.currentTimeMillis();
                                    RawResponse testtap = Requests.get("http://www.inke.cn/").headers(Parameter.of("User-Agent", ProxyChooser.chooseagent())).timeout(1000).proxy(Proxies.httpProxy(element.split(":")[0], Integer.parseInt(element.split(":")[1]))).send();
                                    if (testtap.getStatusCode() != 200) {
                                        throw new ResultErrorException("code error");
                                    }
                                    long aftertime = System.currentTimeMillis();
                                    long delay = aftertime - pretime;
                                        delay /= 12;
                                        synchronized (proxymap) {
                                            if (!proxymap.containsKey(element))
                                                proxymap.put(element, Pair.with((int) (delay), 0));
                                        }
                                    //System.out.println(element + " " + String.valueOf(aftertime - pretime));
                                } catch (Exception e) {
                                    //System.out.println(element + " " + "-1");
                                }
                            }
                        }
                    };
                    ThreadPool.pool2.execute(c);
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
        RUN = false;
    }
    public static void main(String[] args)
    {
        getnewproxy();
        for (String element:proxymap.keySet()){
            System.out.println(element+" "+String.valueOf(proxymap.get(element)));
        }
        String[] c = chooseproxy(new HashSet<>(), true);
        System.out.println(c[0]+':'+c[1]);
        System.out.println(chooseagent());
    }
}
