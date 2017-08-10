import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Parameter;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.bson.Document;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_nowPublish{

    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        String ykid = info.getString("ykid");
        String index_url = "http://service.inke.com/api/live/now_publish?cv=IK3.7.20_Android&uid=251464826&id=" + ykid;
        int try_num = 0;
        int max_num = 100;
        String result, trueresult;
        Timestamp pret = new Timestamp(System.currentTimeMillis());
        while (true){
            String[] random_proxy = ProxyChooser.chooseproxy();
            try{
                //System.out.println(ykid+" "+"nowpublish");
                Timestamp next = new Timestamp(System.currentTimeMillis());
                if (next.getTime()-pret.getTime() > 120000)
                {
                    System.out.println("NOWPUBLISH RTIMEOUT");
                    result = "";
                    //genmap(result);
                    ThreadPool.TotalTrynum += try_num;
                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                            .append("result", result.trim()).append("ts", ts.toString());
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
                String accresult = tap.readToText();
                if (accresult.indexOf("live") != -1) {
                    result = accresult.toString();
                    trueresult = "True";
                }
                else {
                    result = "";
                    trueresult = "False";
                }
                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])-1);
                //genmap(result);
                ThreadPool.TotalTrynum += try_num;
                ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Document doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                        .append("result", result.trim());
                Document doc2 = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                        .append("nowpublish", trueresult).append("ts", ts.toString());
                collection.insertOne(doc2, new SingleResultCallback<Void>() {
                    @Override
                    public void onResult(Void aVoid, Throwable throwable) {
                        Main.havesent ++;
                    }
                });
                return doc;
            }catch (Exception e) {
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])+1);
                try_num += 1;
                if (try_num >= max_num+1) {
                    System.out.println("NOWPUBLISH TIMEOUT");
                    result = "";
                    trueresult = "False";
                    //genmap(result);
                    ThreadPool.TotalTrynum += try_num;
                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                            .append("result", result.trim());
                    Document doc2 = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                            .append("nowpublish", trueresult).append("ts", ts.toString());
                    collection.insertOne(doc2, new SingleResultCallback<Void>() {
                        @Override
                        public void onResult(Void aVoid, Throwable throwable) {
                            Main.havesent ++;
                        }
                    });
                    return doc;
                }
            }
        }
    }
    public static void main(String[] args){
    }
}