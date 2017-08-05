import net.dongliu.requests.Requests;

import java.net.*;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.min;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class ProxyChooser {
    private static String[] proxies= {"10.144.90.190:3128","10.142.42.177:3128","10.134.97.150:3128",
            "10.134.79.130:3128","10.134.62.238:3128","gpu.xiaoe.nm.ted:3128",
            "61.176.215.34:8080","220.194.213.52:8080","183.131.215.86:8080","60.169.19.66:9000","123.125.212.171:8080","61.176.215.7:8080","61.160.190.34:8888","171.8.79.91:8080",
            "218.60.55.3:8080","1.28.246.144:8080"};
    private static String[] agents ={"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0"};

    public static String[] chooseproxy()
    {
        Random random = new Random();
        int num = random.nextInt(proxies.length);
        String[] result = proxies[num].split(":");
        return result;
    }
    public static String chooseagent()
    {
        Random random = new Random();
        int num = random.nextInt(agents.length);
        return agents[num];
    }
    public static void getnewproxy() {
        String index_url= "http://cn-proxy.com/";
        String[] random_proxy = ProxyChooser.chooseproxy();
        URL url = null;
        try {
            url = new URL(index_url);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("gpu.xiaoe.nm.ted", 3128));
            URLConnection urlConnection;
            urlConnection = url.openConnection(proxy);
            urlConnection.setConnectTimeout(500);
            urlConnection.setRequestProperty("User-Agent", ProxyChooser.chooseagent());
            Scanner scan = new Scanner(urlConnection.getInputStream());
            StringBuffer accresult = new StringBuffer();
            while (scan.hasNextLine()){
                accresult.append(scan.nextLine());
                accresult.append("\n");
            }
            System.out.println(accresult);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args)
    {
        getnewproxy();
        chooseproxy();
        chooseagent();
    }
}
