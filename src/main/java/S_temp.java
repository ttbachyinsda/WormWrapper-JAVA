import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoCollection;
import net.dongliu.requests.Parameter;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.bson.Document;

import javax.print.Doc;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;

/**
 * Created by zjkgf on 2017/8/10.
 */
public class S_temp {
    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        Set<String> havechoice = new HashSet<>();
        String ykid = info.getString("ykid");
        String index_url = "http://service.inke.com/api/live/now_publish?cv=IK3.7.20_Android&uid=251464826&id=" + ykid;
        int try_num = 0;
        int max_num = 300;
        String result, trueresult;
        Timestamp pret = new Timestamp(System.currentTimeMillis());
        while (true){
            String hostname = "106.39.160.135";
            int port = 8888;
//            String[] random_proxy = ProxyChooser.chooseproxy(havechoice);
//            havechoice.add(random_proxy[2]);
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
                    Document doc;
                    if (info.containsKey("result")) {
                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                                .append("nowpublish", result.trim()).append("result", info.getString("result")+"|"+"False").append("forcestop","forcestop");
                    } else {
                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                                .append("nowpublish", result.trim()).append("result", "False").append("forcestop", "forcestop");
                    }
//                    collection.insertOne(doc, new SingleResultCallback<Void>() {
//                        @Override
//                        public void onResult(Void aVoid, Throwable throwable) {
//                            Main.havesent ++;
//                        }
//                    });
                    return doc;
                }

                RawResponse tap = Requests.get(index_url).headers(Parameter.of("User-Agent", ProxyChooser.chooseagent())).proxy(Proxies.httpProxy(hostname, port)).timeout(500).send();
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
//                if (ProxyChooser.proxymap.containsKey(random_proxy[2])) {
//                    //ProxyChooser.proxymap.replace(random_proxy[2], max(-1000000, ProxyChooser.proxymap.get(random_proxy[2]) - 40));
//                }
                //genmap(result);
                ThreadPool.TotalTrynum += try_num;
                ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Document doc;
                String resultstr;
                if (info.containsKey("result"))
                    resultstr = info.getString("result")+"|"+trueresult;
                else
                    resultstr = trueresult;
                if (trueresult.equals("True")) {
                    doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                            .append("nowpublish", result.trim()).append("result", resultstr);
                } else {
                    doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
                            .append("nowpublish", result.trim()).append("result", resultstr).append("forcestop","forcestop");
                }
                Document doc2 = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                        .append("nowpublish", trueresult).append("ts", ts.toString());
//                collection.insertOne(doc2, new SingleResultCallback<Void>() {
//                    @Override
//                    public void onResult(Void aVoid, Throwable throwable) {
//                        Main.havesent ++;
//                    }
//                });
                Timestamp tsss = new Timestamp(System.currentTimeMillis());
                System.out.println(tsss.toString() + " 1");
                return doc;
            }catch (Exception e) {
                Timestamp tsss = new Timestamp(System.currentTimeMillis());
                System.out.println(tsss.toString() + " -1");
                e.printStackTrace();
                return new Document();
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e1) {
//                    e1.printStackTrace();
//                }
//                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
//                {
//                    //ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])+5);
//                }

//                try_num += 1;
//                if (try_num >= max_num+1) {
//                    System.out.println("NOWPUBLISH TIMEOUT");
//                    result = "";
//                    trueresult = "False";
//                    //genmap(result);
//                    ThreadPool.TotalTrynum += try_num;
//                    ThreadPool.MaxTrynum = max(try_num, ThreadPool.MaxTrynum);
//                    Timestamp ts = new Timestamp(System.currentTimeMillis());
//                    Document doc;
//                    if (info.containsKey("result")) {
//                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
//                                .append("nowpublish", result.trim()).append("result", info.getString("result")+"|"+trueresult)
//                                .append("forcestop", "forcestop");
//                    } else {
//                        doc = new Document("ts", info.getString("ts")).append("ykid", ykid)
//                                .append("nowpublish", result.trim()).append("result", trueresult).append("forcestop", "forcestop");
//                    }
//                    Document doc2 = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
//                            .append("nowpublish", trueresult).append("ts", ts.toString());
////                    collection.insertOne(doc2, new SingleResultCallback<Void>() {
////                        @Override
////                        public void onResult(Void aVoid, Throwable throwable) {
////                            Main.havesent ++;
////                        }
////                    });
//                    return doc;
//                }
            }
        }
    }
    public static void main(String[] args){
        Document d = new Document("ykid", "119190953").append("ts", String.valueOf(System.currentTimeMillis()));
        int num = 0;
        while (true){
            num ++;
            System.out.println(num);
            run(d, false);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
