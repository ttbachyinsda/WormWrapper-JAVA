import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Parameter;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.bson.Document;

import javax.print.Doc;
import java.io.IOException;
import java.net.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_getPoint{
    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        Set<String> havechoice = new HashSet<>();
        String ykid = info.getString("ykid");
        String index_url = "http://120.55.238.158/api/statistic/inout?uid=251464826&id=" + ykid;
        int try_num = 0;
        int max_num = 100;
        Timestamp pret = new Timestamp(System.currentTimeMillis());
        if (info.containsKey("forcestop")){
            Document doc;
            if (info.containsKey("result")) {
                doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                        .append("result", info.getString("result")+"|").append("forcestop", "forcestop");
            } else {
                doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                        .append("result", "").append("forcestop", "forcestop");
            }
            return doc;
        }
        while (true){
            String[] random_proxy = ProxyChooser.chooseproxy(havechoice,false);
            havechoice.add(random_proxy[2]);
            try{
                //System.out.println(ykid+" "+"getpoint");
                Timestamp next = new Timestamp(System.currentTimeMillis());
                if (next.getTime()-pret.getTime() > 120000)
                {
                    System.out.println("GETPOINT TIMEOUT");
                    String result = "";
                    //genmap(result);
                    ThreadPool.TotalTrynum += try_num;
                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc;
                    if (info.containsKey("result")) {
                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                                .append("result", info.getString("result")+"|"+result.trim());
                    } else {
                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                                .append("result", result.trim());
                    }
//                    collection.insertOne(doc, new SingleResultCallback<Void>() {
//                        @Override
//                        public void onResult(Void aVoid, Throwable throwable) {
//                            Main.havesent ++;
//                        }
//                    });
                    return doc;
                }

                RawResponse tap = Requests.get(index_url).headers(Parameter.of("User-Agent", ProxyChooser.chooseagent())).timeout(500).proxy(Proxies.httpProxy(random_proxy[0], Integer.parseInt(random_proxy[1]))).send();
                if (tap.getStatusCode() != 200)
                {
                    throw new ResultErrorException("code error");
                }
                String accresult = tap.readToText();
                String inx = "\"gold\": \\d+";
                String outx = "\"point\": \\d+";
                Pattern pin = Pattern.compile(inx);
                Pattern pout = Pattern.compile(outx);
                Matcher min = pin.matcher(accresult);
                Matcher mout = pout.matcher(accresult);
                if (!min.find()) throw new ResultErrorException("Result of getpoint inx is Error. "+accresult);
                if (!mout.find()) throw new ResultErrorException("Result of getpoint outx is Error. "+accresult);
                String[] sin = min.group().split(":");
                String[] sout = mout.group().split(":");
                String result = sin[sin.length-1]+sout[sout.length-1];
                //genmap(result);
//                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
//                    ProxyChooser.proxymap.replace(random_proxy[2], min(0,ProxyChooser.proxymap.get(random_proxy[2])-1));
                ThreadPool.TotalTrynum += try_num;
                ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Document doc;
                if (info.containsKey("result")) {
                    doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                            .append("result", info.getString("result")+"|"+result.trim());
                } else {
                    doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                            .append("result", result.trim());
                }
//                collection.insertOne(doc, new SingleResultCallback<Void>() {
//                    @Override
//                    public void onResult(Void aVoid, Throwable throwable) {
//                        Main.havesent ++;
//                    }
//                });
                return doc;
            }catch (Exception e) {
                //e.printStackTrace();
//                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
//                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])+1);
                try_num += 1;
                if (try_num >= max_num+1) {
                    System.out.println("GETPOINT TIMEOUT");
                    String result = "";
                    //genmap(result);
                    ThreadPool.TotalTrynum += try_num;
                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc;
                    if (info.containsKey("result")) {
                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                                .append("result", info.getString("result")+"|"+result.trim());
                    } else {
                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                                .append("result", result.trim());
                    }
//                    collection.insertOne(doc, new SingleResultCallback<Void>() {
//                        @Override
//                        public void onResult(Void aVoid, Throwable throwable) {
//                            Main.havesent ++;
//                        }
//                    });
                    return doc;
                }
            }
        }
    }
    public static void main(String[] args){
    }
}
