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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_getFans{
    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        String ykid = info.getString("ykid");
        String index_url = "http://120.55.238.158/api/user/relation/numrelations?uid=251464826&id=" + ykid;
        int try_num = 0;
        int max_num = 100;
        Timestamp pret = new Timestamp(System.currentTimeMillis());
        while (true){
            String[] random_proxy = ProxyChooser.chooseproxy();
            try{
                //System.out.println(ykid+" "+"getfans");
                Timestamp next = new Timestamp(System.currentTimeMillis());
                if (next.getTime()-pret.getTime() > 120000)
                {
                    System.out.println("GETFANS RTIMEOUT");
                    String result = "";
                    //genmap(result);
                    ThreadPool.TotalTrynum += try_num;
                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("timestamp", info.getString("ts"))
                            .append("ykid", ykid).append("result", result.trim()).append("ts", ts.toString());
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
                String inx = "\"num_followers\":\\d+";
                String outx = "\"num_followings\":\\d+";
                Pattern pin = Pattern.compile(inx);
                Pattern pout = Pattern.compile(outx);
                Matcher min = pin.matcher(accresult);
                Matcher mout = pout.matcher(accresult);
                if (!min.find()) throw new ResultErrorException("Result of getFans inx is Error. "+accresult);
                if (!mout.find()) throw new ResultErrorException("Result of getFans outx is Error. "+accresult);
                String[] sin = min.group().split(":");
                String[] sout = mout.group().split(":");
                String result = sin[sin.length-1]+" "+sout[sout.length-1];
                //genmap(result);
                ThreadPool.TotalTrynum += try_num;
                ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])-1);
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
            }catch (Exception e) {
                //e.printStackTrace();
                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])+1);
                try_num += 1;
                if (try_num >= max_num+1) {
                    System.out.println("GETFANS TIMEOUT");
                    String result = "";
                    //genmap(result);
                    ThreadPool.TotalTrynum += try_num;
                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("timestamp", info.getString("ts"))
                            .append("ykid", ykid).append("result", result.trim()).append("ts", ts.toString());
                    collection.insertOne(doc, new SingleResultCallback<Void>() {
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