/**
 * Created by zjkgf on 2017/7/31.
 */
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

/**
 * Created by zjkgf on 2017/7/29.
 */
public class S_goodVoice{
    public static MongoCollection<Document> collection;
    public static Document run(Document info, boolean with_proxy) {
        String ykid = info.getString("ykid");
        String index_url = "http://service.inke.com/api/live/themesearch?uid=251464826&keyword=666ABA8214206E5B";
        int try_num = 0;
        int max_num = 100;
        while (true){
            String[] random_proxy = ProxyChooser.chooseproxy();
            try{

                RawResponse tap = Requests.get(index_url).headers(Parameter.of("User-Agent", ProxyChooser.chooseagent())).timeout(500).proxy(Proxies.httpProxy(random_proxy[0], Integer.parseInt(random_proxy[1]))).send();
                if (tap.getStatusCode() != 200)
                {
                    throw new ResultErrorException("code error");
                }
                String accresult = tap.readToText();
                String result;
                if (accresult.indexOf("\"id\": "+ykid) != -1)
                    result = "True";
                else
                    result = "False";
                //genmap(result);
                if (ProxyChooser.proxymap.containsKey(random_proxy[2]))
                    ProxyChooser.proxymap.replace(random_proxy[2], ProxyChooser.proxymap.get(random_proxy[2])-1);
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                Document doc = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                        .append("result", result.trim());
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
                    String result = "False";
                    //genmap(result);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    Document doc = new Document("timestamp", info.getString("ts")).append("ykid", ykid)
                            .append("result", result.trim());
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