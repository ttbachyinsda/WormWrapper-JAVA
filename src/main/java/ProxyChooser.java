import net.dongliu.requests.Parameter;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;

import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.min;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class ProxyChooser {
    public static boolean WITH_PROXY = true;
    public static Timestamp prets;

    private static List<String> proxies;
    private static String[] agents ={"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0"};

    public static String[] chooseproxy()
    {
        Random random = new Random();
        int num = random.nextInt(proxies.size());
        String[] result = proxies.get(num).split(":");
        return result;
    }
    public static String chooseagent()
    {
        Random random = new Random();
        int num = random.nextInt(agents.length);
        return agents[num];
    }
    public static void getnewproxy() {
        String index_url= "http://dev.kuaidaili.com/api/getproxy/?orderid=910224640141044&num=500&b_pcchrome=1&b_pcie=1&b_pcff=1&b_android=1&b_iphone=1&b_ipad=1&protocol=1&method=2&an_an=1&an_ha=1&sp1=1&sep=1";
        URL url = null;
        if (proxies == null){
            proxies = new ArrayList<>();
            proxies.add("218.26.219.186:8080");
            proxies.add("58.222.254.11:3128");
            proxies.add("61.185.219.126:3128");
        }
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
                return;
            } else {
                String[] lists = accresult.split("\r\n");
                for (String element:lists){
                    newproxies.add(element);
                }
                if (newproxies.size() > 0){
                    proxies = new ArrayList<>();
                    for (String element:newproxies){
                        proxies.add(element);
                    }
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }

    }
    public static void main(String[] args)
    {
        getnewproxy();
        String[] c = chooseproxy();
        System.out.println(c[0]+':'+c[1]);
        System.out.println(chooseagent());
    }
}
