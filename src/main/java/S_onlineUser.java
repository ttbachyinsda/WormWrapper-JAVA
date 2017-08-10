import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Parameter;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.bson.Document;

import javax.print.Doc;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

/**
 * Created by zjkgf on 2017/7/31.
 */
public class S_onlineUser{

    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        String ykid = info.getString("ykid");
        String roomid = Weapon.getroomid(info);
        String index_url = "http://120.55.238.158/api/live/info?uid=251464826&id=" + roomid;
        int start = 0;
        StringBuffer result = new StringBuffer();
        int try_num = 0;
        int max_num = 100;
        String realurl = index_url + "&start="+start;
        String accresult = "";
        Timestamp pret = new Timestamp(System.currentTimeMillis());

        while (true) {
            String[] random_proxy = ProxyChooser.chooseproxy();
            try {

                //System.out.println(ykid+" "+"onlineuser");

                Timestamp next = new Timestamp(System.currentTimeMillis());
                if (next.getTime()-pret.getTime() > 120000)
                {
                    System.out.println("ONLINEUSER RTIMEOUT");
                    //genmap(result);
                    ThreadPool.TotalTrynum += try_num;
                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                            .append("result", result.toString().trim()).append("ts", ts.toString());
                    collection.insertOne(doc, new SingleResultCallback<Void>() {
                        @Override
                        public void onResult(Void aVoid, Throwable throwable) {
                            Main.havesent ++;
                        }
                    });
                    return doc;
                }

                RawResponse tap = Requests.get(index_url).headers(Parameter.of("User-Agent", ProxyChooser.chooseagent())).timeout(500).proxy(Proxies.httpProxy(random_proxy[0], Integer.parseInt(random_proxy[1]))).send();
                if (tap.getStatusCode() != 200)
                {
                    throw new ResultErrorException("code error");
                }
                accresult = tap.readToText();
                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])-1);
                break;
            } catch (Exception e) {
                //e.printStackTrace();
                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])+1);
                try_num += 1;
                if (try_num >= max_num + 1) {
                    System.out.println("ONLINEUSER TIMEOUT");
                    break;
                }
            }
        }
        List<String> lin = new ArrayList<String>();
        String inx = "\"online_users\": \\d+";
        Pattern pin = Pattern.compile(inx);
        Matcher min = pin.matcher(accresult);

        while (min.find())
        {
            String[] sin = min.group().split(" ");
            lin.add(sin[sin.length-1]);
        }
        for (String element:lin){
            result.append(element);
            result.append(' ');
        }
        //genmap(result.toString());
        ThreadPool.TotalTrynum += try_num;
        ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Document doc = new Document("timestamp", info.getString("ts")).append("ykid", ykid).append("roomid", roomid)
                .append("result", result.toString().trim()).append("ts", ts.toString());
        collection.insertOne(doc, new SingleResultCallback<Void>() {
            @Override
            public void onResult(Void aVoid, Throwable throwable) {
                Main.havesent ++;
            }
        });
        return doc;
    }
    public static void main(String[] args){
    }
}